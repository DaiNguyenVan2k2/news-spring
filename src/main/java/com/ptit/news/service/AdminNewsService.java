package com.ptit.news.service;


import com.ptit.news.dto.NewsDTO;
import com.ptit.news.repository.CategoryRepository;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.entity.Category;
import com.ptit.news.entity.News;
import com.ptit.news.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AdminNewsService { // Đã đổi tên lớp

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // Chuyển đổi Entity sang DTO
    private NewsDTO convertToDto(News news) {
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        dto.setStatus(news.isStatus());
        dto.setViews(news.getViews());
        dto.setPublishedAt(news.getPublishedAt());
        dto.setDeleted(news.getIsDeleted());

        if (news.getAuthor() != null) {
            dto.setAuthorId(news.getAuthor().getId());
            dto.setAuthorName(news.getAuthor().getFirstName() + " " + news.getAuthor().getLastName());
        }
        if (news.getCategory() != null) {
            dto.setCategoryId(news.getCategory().getId());
            dto.setCategoryName(news.getCategory().getContent());
        }
        return dto;
    }

    public Page<NewsDTO> getAllNews(Pageable pageable) { // Đã đổi tên hàm
        Page<News> newsPage = newsRepository.findAllByIsDeletedFalse(pageable);
        return newsPage.map(this::convertToDto);
    }

    public NewsDTO getNewsById(Long id) { // Đã đổi tên hàm
        News news = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));
        return convertToDto(news);
    }

    @Transactional
    public NewsDTO createNews(NewsDTO newsDTO) { // Đã đổi tên hàm
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setSummary(newsDTO.getSummary());
        news.setContent(newsDTO.getContent());
        news.setStatus(newsDTO.isStatus());
        news.setViews(0); // Mặc định khi tạo mới
        news.setPublishedAt(new Date()); // Hoặc lấy từ DTO nếu frontend gửi lên

        // Gán tác giả
        User author = userRepository.findById(newsDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        news.setAuthor(author);

        // Gán thể loại
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        news.setCategory(category);

        news.setIsDeleted(false);

        News savedNews = newsRepository.save(news);
        return convertToDto(savedNews);
    }

    @Transactional
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) { // Đã đổi tên hàm
        News existingNews = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));

        existingNews.setTitle(newsDTO.getTitle());
        existingNews.setSummary(newsDTO.getSummary());
        existingNews.setContent(newsDTO.getContent());
        existingNews.setStatus(newsDTO.isStatus());

        if (newsDTO.getAuthorId() != null && !existingNews.getAuthor().getId().equals(newsDTO.getAuthorId())) {
            User author = userRepository.findById(newsDTO.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            existingNews.setAuthor(author);
        }
        if (newsDTO.getCategoryId() != null && !existingNews.getCategory().getId().equals(newsDTO.getCategoryId())) {
            Category category = categoryRepository.findById(newsDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingNews.setCategory(category);
        }

        News updatedNews = newsRepository.save(existingNews);
        return convertToDto(updatedNews);
    }

    @Transactional
    public void deleteNews(Long id) { // Đã đổi tên hàm
        News news = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));
        news.setIsDeleted(true); // Soft delete
        newsRepository.save(news);
    }
}