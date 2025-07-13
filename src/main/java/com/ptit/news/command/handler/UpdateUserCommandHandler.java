package com.ptit.news.command.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.axonframework.commandhandling.CommandHandler;
import com.ptit.news.command.dto.UpdateUserCommand;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UpdateUserCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @CommandHandler
    public Response<User> handle(UpdateUserCommand command) {
        try {
            User user = userRepository.findById(command.getId()).orElse(null);

            if (user == null) {
                return Response.Error("Không tìm thấy user");
            }

            // Cập nhật thông tin
            if (command.getFirstName() != null) {
                user.setFirstName(command.getFirstName());
            }
            if (command.getLastName() != null) {
                user.setLastName(command.getLastName());
            }
            if (command.getPhone() != null) {
                user.setPhone(command.getPhone());
            }
            if (command.getAvatar() != null) {
                user.setAvatar(command.getAvatar());
            }

            User updatedUser = userRepository.save(user);

            return Response.Success(updatedUser, "Cập nhật thông tin thành công");

        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi cập nhật thông tin");
        }
    }
}