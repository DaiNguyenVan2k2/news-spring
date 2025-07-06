package com.ptit.news.command.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.ptit.news.dto.UserResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserResponse user;
    @Builder.Default
    private String tokenType = "Bearer";
}