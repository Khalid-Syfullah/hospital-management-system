package com.hospital.user;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return userMapper.toResponse(findActiveUser(id));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserRequest request) {
        User user = findActiveUser(id);
        userMapper.updateEntity(request, user);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void enableUser(UUID id) {
        User user = findActiveUser(id);
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User {} enabled", id);
    }

    @Transactional
    public void disableUser(UUID id) {
        User user = findActiveUser(id);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("User {} disabled", id);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = findActiveUser(id);
        user.softDelete();
        userRepository.save(user);
        log.info("User {} soft-deleted", id);
    }

    private User findActiveUser(UUID id) {
        return userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
