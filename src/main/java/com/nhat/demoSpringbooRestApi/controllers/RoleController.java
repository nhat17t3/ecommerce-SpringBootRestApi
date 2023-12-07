package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.RoleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("role.success.getAll",roles);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findRoleById(@PathVariable("id") int id) {
        Role role = roleService.findRoleById(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("role.success.getById",role);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createRole(@Valid @RequestBody  RoleRequestDTO requestDTO) {
        Role createdRole = roleService.createRole(requestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("role.success.create",createdRole);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateRole(@PathVariable int id, @Valid @RequestBody RoleRequestDTO requestDTO) {
        Role updatedRole = roleService.updateRole(id, requestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("role.success.update",updatedRole);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("role.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }

}
