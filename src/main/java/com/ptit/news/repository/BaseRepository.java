package com.ptit.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Tìm tất cả entity chưa bị xóa
     */
    List<T> findByIsDeletedFalse();

    /**
     * Tìm entity theo ID và chưa bị xóa
     */
    Optional<T> findByIdAndIsDeletedFalse(ID id);

    /**
     * Đếm số lượng entity chưa bị xóa
     */
    long countByIsDeletedFalse();
}