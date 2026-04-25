package com.hospital.ward;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wards")
@PreAuthorize("hasAnyRole('ADMIN','NURSE','RECEPTIONIST')")
public class WardController {
    private final WardService service;
    public WardController(WardService service) { this.service = service; }
    @GetMapping PageResponse<WardResponse> listWards(Pageable pageable) { return PageResponse.of("Wards retrieved", service.listWards(pageable)); }
    @GetMapping("/{id}") ApiResponse<WardResponse> getWard(@PathVariable UUID id) { return ApiResponse.ok("Ward retrieved", service.getWard(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") ApiResponse<WardResponse> createWard(@Valid @RequestBody WardRequest request) { return ApiResponse.ok("Ward created", service.createWard(request)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ApiResponse<WardResponse> updateWard(@PathVariable UUID id, @Valid @RequestBody WardRequest request) { return ApiResponse.ok("Ward updated", service.updateWard(id, request)); }
    @GetMapping("/beds") PageResponse<BedResponse> listBeds(Pageable pageable) { return PageResponse.of("Beds retrieved", service.listBeds(pageable)); }
    @GetMapping("/beds/{id}") ApiResponse<BedResponse> getBed(@PathVariable UUID id) { return ApiResponse.ok("Bed retrieved", service.getBed(id)); }
    @PostMapping("/beds") ApiResponse<BedResponse> createBed(@Valid @RequestBody BedRequest request) { return ApiResponse.ok("Bed created", service.createBed(request)); }
    @PutMapping("/beds/{id}") ApiResponse<BedResponse> updateBed(@PathVariable UUID id, @Valid @RequestBody BedRequest request) { return ApiResponse.ok("Bed updated", service.updateBed(id, request)); }
    @DeleteMapping("/beds/{id}") ApiResponse<Void> deleteBed(@PathVariable UUID id) { service.deleteBed(id); return ApiResponse.ok("Bed deleted", null); }
}
