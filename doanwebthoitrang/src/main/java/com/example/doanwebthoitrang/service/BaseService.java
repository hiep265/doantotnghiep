package com.example.doanwebthoitrang.service;

import com.example.doanwebthoitrang.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public abstract class BaseService<T, ID> {
    protected final BaseRepository<T, ID> repository;

    protected BaseService(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
} 