package com.ptit.news.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ptit.news.service.MasterDataService;
import com.ptit.news.entity.Role;
import com.ptit.news.common.Response;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/master-data")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Tag(name = "Master Data", description = "Master data management APIs")
public class MasterDataController {

    @Autowired
    private MasterDataService masterDataService;

    @GetMapping("/roles")
    @Operation(summary = "Get all roles", description = "Retrieve all available roles in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response<List<Role>> getAllRoles() {
        try {
            List<Role> roles = masterDataService.getAllRoles();
            return Response.Success(roles, "Lấy danh sách roles thành công");
        } catch (Exception e) {
            log.error("Error getting roles: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi lấy danh sách roles");
        }
    }

    @PostMapping("/roles")
    public Response<String> createRole(@RequestParam String roleName) {
        try {
            if (masterDataService.roleExists(roleName)) {
                return Response.Error("Role đã tồn tại: " + roleName);
            }

            masterDataService.createRole(roleName);
            return Response.Success("Role created successfully", "Tạo role thành công: " + roleName);
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi tạo role");
        }
    }

    @GetMapping("/roles/check")
    public Response<Boolean> checkRoleExists(@RequestParam String roleName) {
        try {
            boolean exists = masterDataService.roleExists(roleName);
            return Response.Success(exists, "Kiểm tra role thành công");
        } catch (Exception e) {
            log.error("Error checking role: {}", e.getMessage(), e);
            return Response.Error("Lỗi khi kiểm tra role");
        }
    }
}