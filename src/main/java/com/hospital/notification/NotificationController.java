package com.hospital.notification;

import com.hospital.common.PageResponse;
import com.hospital.notification.NotificationDtos.NotificationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationController(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    PageResponse<NotificationResponse> list(Pageable pageable) {
        return PageResponse.from("Notifications fetched", repository.findAll(pageable).map(mapper::toResponse));
    }
}
