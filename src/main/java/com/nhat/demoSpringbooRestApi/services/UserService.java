package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.UserListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.models.User;

public interface UserService {
    User registerUser(UserRequestDTO userDTO);

    UserListResponseDTO getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    User getUserById(Integer userId);

    User updateUser(Integer userId, UserRequestDTO userDTO);

    String deleteUser(Integer userId);
}
