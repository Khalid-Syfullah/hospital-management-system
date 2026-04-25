package com.hospital.user;

import com.hospital.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse get(UUID id) {
        return UserResponse.from(find(id));
    }

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = find(id);
        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public UserResponse setEnabled(UUID id, boolean enabled) {
        User user = find(id);
        user.setEnabled(enabled);
        return UserResponse.from(repository.save(user));
    }

    private User find(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
