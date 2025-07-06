package com.ptit.news.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.ptit.news.entity.Role;
import com.ptit.news.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import com.ptit.news.common.enums.UserRole;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MasterDataService implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    /**
     * Khởi tạo các role cơ bản
     */
    private void initializeRoles() {
        log.info("Starting to initialize master data...");

        for (UserRole userRole : UserRole.values()) {
            String roleName = userRole.getValue();
            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();

                roleRepository.save(role);
                log.info("Created role: {}", roleName);
            } else {
                log.info("Role already exists: {}", roleName);
            }
        }

        log.info("Master data initialization completed!");
    }

    /**
     * Khởi tạo role cụ thể
     */
    public void createRole(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();

            roleRepository.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.warn("Role already exists: {}", roleName);
        }
    }

    /**
     * Lấy tất cả roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Kiểm tra role có tồn tại không
     */
    public boolean roleExists(String roleName) {
        return roleRepository.existsByName(roleName);
    }
}