package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllUsers(UserFilterRequestDTO userFilterRequestDTO) {
        DataTableResponseDTO<User> users = userService.getAllUsers(userFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.getAll", users);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Integer userId) {

        User user = userService.getUserById(userId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.getById", user);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> addUser(@Valid @RequestBody UserRequestDTO user) throws Exception {
        User savedUser = userService.registerUser(user);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.create", savedUser);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable Integer userId, @Valid @RequestBody UserRequestDTO user) {
        User savedUser = userService.updateUser(userId, user);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.update", savedUser);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }

}
