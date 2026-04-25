package com.hospital.notification;

import com.hospital.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }
    @GetMapping PageResponse<NotificationResponse> list(Pageable pageable) { return PageResponse.of("Notifications retrieved", service.list(pageable)); }
}
