package com.ptit.news.repository;

import com.ptit.news.entity.News;
import com.ptit.news.dto.NewsViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // Đảm bảo đã import LocalDateTime
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Page<News> findAllByIsDeletedFalse(Pageable pageable);
    Optional<News> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT new com.ptit.news.dto.NewsViewDTO(n.id, n.title, n.views) " +
            "FROM News n " +
            "WHERE n.isDeleted = false " +
            "ORDER BY n.views DESC")
    List<NewsViewDTO> findTopNewsByViews();

    // --- Các phương thức mới cho Dashboard ---

    // Đếm tổng số bài viết không bị xóa
    long countByIsDeletedFalse();

    // Đếm số bài viết được tạo sau một thời điểm nhất định và không bị xóa
    long countByCreatedAtAfterAndIsDeletedFalse(LocalDateTime createdAt);
}