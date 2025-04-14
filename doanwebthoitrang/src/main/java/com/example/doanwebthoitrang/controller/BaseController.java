package com.example.doanwebthoitrang.controller;

import com.example.doanwebthoitrang.DTO.ApiResponse;
import com.example.doanwebthoitrang.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public abstract class BaseController<T, ID> {
    protected final BaseService<T, ID> service;

    protected BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Page<T>>> findAll(Pageable pageable) {
        Page<T> entities = service.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(entities, "Retrieved successfully", HttpStatus.OK));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<T>> findById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        return entity.map(value -> ResponseEntity.ok(ApiResponse.success(value, "Retrieved successfully", HttpStatus.OK)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Entity not found", HttpStatus.NOT_FOUND)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<T>> create(@RequestBody T entity) {
        T savedEntity = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedEntity, "Created successfully", HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<T>> update(@PathVariable ID id, @RequestBody T entity) {
        if (!service.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Entity not found", HttpStatus.NOT_FOUND));
        }
        T updatedEntity = service.save(entity);
        return ResponseEntity.ok(ApiResponse.success(updatedEntity, "Updated successfully", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        if (!service.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Entity not found", HttpStatus.NOT_FOUND));
        }
        service.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Deleted successfully", HttpStatus.OK));
    }
} 