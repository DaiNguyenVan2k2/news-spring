package com.ptit.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "request_authors")
public class RequestAuthor extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String profileUrl;
    private String sampleArticles;
    private String reason;
    private String status;
}