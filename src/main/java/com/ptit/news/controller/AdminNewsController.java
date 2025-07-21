package com.ptit.news.controller;


import com.ptit.news.dto.NewsDTO;
import com.ptit.news.service.AdminNewsService; // Đã cập nhật import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {

    @Autowired
    private AdminNewsService adminNewsService;

    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<NewsDTO> newsPage = adminNewsService.getAllNews(pageable);
        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        NewsDTO news = adminNewsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) {
        NewsDTO createdNews = adminNewsService.createNews(newsDTO);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody NewsDTO newsDTO) {
        NewsDTO updatedNews = adminNewsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        adminNewsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}