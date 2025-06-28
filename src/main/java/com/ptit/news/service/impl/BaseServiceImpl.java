package com.ptit.news.service.impl;

import com.ptit.news.entity.BaseEntity;
import com.ptit.news.repository.BaseRepository;
import com.ptit.news.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID> {

    protected final BaseRepository<T, ID> repository;

    public BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAllActive() {
        return repository.findByIsDeletedFalse();
    }

    @Override
    public Optional<T> findByIdActive(ID id) {
        return repository.findByIdAndIsDeletedFalse(id);
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> entityOpt = repository.findById(id);
        if (entityOpt.isPresent()) {
            T entity = entityOpt.get();
            entity.setIsDeleted(true);
            repository.save(entity);
        }
    }

    @Override
    public void hardDeleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}