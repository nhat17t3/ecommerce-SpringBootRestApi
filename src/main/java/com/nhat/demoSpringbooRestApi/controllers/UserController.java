package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import com.nhat.demoSpringbooRestApi.dtos.UserListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<UserListResponseDTO> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        UserListResponseDTO userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<UserListResponseDTO>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {

        User user = userService.getUserById(userId);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequestDTO user) {

        User savedUser = userService.registerUser(user);

        return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @Valid @RequestBody UserRequestDTO user) {

        User savedUser = userService.updateUser(userId,user);

        return new ResponseEntity<User>(savedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {

        String message = userService.deleteUser(userId);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }


}
