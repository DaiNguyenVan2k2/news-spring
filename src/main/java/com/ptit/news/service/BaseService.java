package com.ptit.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {

    /**
     * Lưu entity
     */
    T save(T entity);

    /**
     * Lưu danh sách entity
     */
    List<T> saveAll(List<T> entities);

    /**
     * Tìm entity theo ID
     */
    Optional<T> findById(ID id);

    /**
     * Tìm tất cả entity
     */
    List<T> findAll();

    /**
     * Tìm tất cả entity với phân trang
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Tìm tất cả entity chưa bị xóa
     */
    List<T> findAllActive();

    /**
     * Tìm entity theo ID và chưa bị xóa
     */
    Optional<T> findByIdActive(ID id);

    /**
     * Xóa entity (soft delete)
     */
    void deleteById(ID id);

    /**
     * Xóa entity thật sự
     */
    void hardDeleteById(ID id);

    /**
     * Kiểm tra entity có tồn tại không
     */
    boolean existsById(ID id);

    /**
     * Đếm số lượng entity
     */
    long count();
}