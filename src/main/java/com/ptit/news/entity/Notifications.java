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
@Table(name = "notifications")
public class Notifications extends BaseEntity {
    private Boolean isRead;
    private String title;
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}