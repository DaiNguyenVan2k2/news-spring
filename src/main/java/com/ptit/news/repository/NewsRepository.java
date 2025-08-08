package com.ptit.news.repository;

import com.ptit.news.entity.News;
import com.ptit.news.dto.NewsViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByStatus(Boolean status);

// Phương thức này đã được sửa lỗi từ các lần trước
    List<News> findByStatusAndIsDeleted(Boolean status, boolean isDeleted);
    Page<News> findAllByIsDeletedFalse(Pageable pageable);

    Optional<News> findByIdAndIsDeletedFalse(Long id);

// Phương thức MỚI: Tìm kiếm bài viết theo tiêu đề (không phân biệt chữ hoa/thường)

// và đảm bảo bài viết chưa bị xóa mềm, có phân trang.

    Page<News> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title, Pageable pageable); // <-- Thêm dòng này

    @Query("SELECT new com.ptit.news.dto.NewsViewDTO(n.id, n.title, n.views) " +
            "FROM News n " +
            "WHERE n.isDeleted = false " +
            "ORDER BY n.views DESC")
    List<NewsViewDTO> findTopNewsByViews();
    long countByIsDeletedFalse();

    long countByCreatedAtAfterAndIsDeletedFalse(LocalDateTime createdAt);

}