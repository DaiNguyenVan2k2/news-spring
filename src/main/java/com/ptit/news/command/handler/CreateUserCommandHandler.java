package com.ptit.news.command.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.axonframework.commandhandling.CommandHandler;
import com.ptit.news.command.dto.CreateUserCommand;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.common.Response;
import com.ptit.news.exception.DuplicateResourceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CreateUserCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CommandHandler
    public Response<User> handle(CreateUserCommand command) {

        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", command.getEmail());
        }

        // Tạo user mới // Role mặt định = READER
        User user = User.builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .phone(command.getPhone())
                .avatar(command.getAvatar())
                .isEnabled(true)
                // .roles(Set.of(new Role(UserRole.READER.getValue())))
                .build();

        User savedUser = userRepository.save(user);

        return Response.Success(savedUser, "Tạo tài khoản thành công");

    }
}