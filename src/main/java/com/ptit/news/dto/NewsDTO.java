package com.ptit.news.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private boolean status;
    private Integer views;
    private Date publishedAt;
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private String categoryName;
    private boolean isDeleted;

}