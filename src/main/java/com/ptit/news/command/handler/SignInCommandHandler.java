package com.ptit.news.command.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.axonframework.commandhandling.CommandHandler;
import com.ptit.news.command.dto.SignInCommand;
import com.ptit.news.command.dto.AuthResponse;
import com.ptit.news.dto.UserResponse;
import com.ptit.news.entity.User;
import com.ptit.news.entity.Token;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.TokenRepository;
import com.ptit.news.service.JwtService;
import com.ptit.news.common.Response;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SignInCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @CommandHandler
    public Response<AuthResponse> handle(SignInCommand command) {
        try {
            String email = command.getEmail();
            String password = command.getPassword();
            Boolean isRemember = command.getIsRemember() != null && "true".equalsIgnoreCase(command.getIsRemember());

            // Xác thực bằng AuthenticationManager
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            // Lấy user sau khi xác thực thành công
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            // Kiểm tra user có enabled không
            if (!user.getIsEnabled()) {
                throw new BadCredentialsException("Tài khoản đã bị khóa");
            }

            // Tạo JWT token
            String accessToken = jwtService.generateToken(user, isRemember);

            log.info("Generated access token: {}", accessToken);

            // Lưu token vào database
            saveToken(accessToken, user);

            AuthResponse authResponse = AuthResponse.builder()
                    .token(accessToken)
                    .refreshToken(null)
                    .user(new UserResponse(user))
                    .build();

            return Response.Success(authResponse, "Đăng nhập thành công");

        } catch (BadCredentialsException e) {
            System.out.println("Chạy được vào breaking 1");
            throw e;
        } catch (Exception e) {
            log.error("Error during sign in: {}", e.getMessage(), e);
            System.out.println("Chạy được vào breaking 2");
            throw new BadCredentialsException("Lỗi khi đăng nhập");
        }
    }

    private void saveToken(String code, User user) {
        Token token = Token.builder()
                .code(code)
                .isSignOut(false)
                .user(user)
                .build();

        try {
            tokenRepository.save(token);
            log.info("Token saved successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error saving token: {}", e.getMessage(), e);
        }
    }
}