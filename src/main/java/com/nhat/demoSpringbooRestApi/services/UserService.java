package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.models.User;

public interface UserService {
    User registerUser(UserRequestDTO userDTO) throws Exception;

    DataTableResponseDTO<User> getAllUsers(UserFilterRequestDTO userFilterRequestDTO);

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    User updateUser(Integer userId, UserRequestDTO userDTO);

    void deleteUser(Integer userId);
}
