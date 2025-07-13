package com.ptit.news.command.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String avatar;
}