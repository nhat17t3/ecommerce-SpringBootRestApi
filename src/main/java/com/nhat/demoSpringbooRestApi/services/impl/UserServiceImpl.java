package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.UserListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import com.nhat.demoSpringbooRestApi.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @Override
    public UserListResponseDTO getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<User> pageUsers = userRepo.findAll(pageDetails);

        List<User> users = pageUsers.getContent();

//        List<UserRequestDTO> userRequestDTO = users.stream().map(user -> modelMapper.map(user, UserRequestDTO.class))
//                .collect(Collectors.toList());

        UserListResponseDTO userResponse = new UserListResponseDTO();

        userResponse.setContent(users);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());

        return userResponse;
    }


    @Override
    public User getUserById(Integer userId) {
        return userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public User registerUser(UserRequestDTO userRequestDTO) {
        User user = modelMapper.map(userRequestDTO, User.class);
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : userRequestDTO.getRoles()) {
            Role role = roleService.findRoleById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    @Override
    public User updateUser(Integer userId, UserRequestDTO userRequestDTO) {
        User user = getUserById(userId);

        user.setName(userRequestDTO.getName());
        user.setAge(userRequestDTO.getAge());
        user.setEmail(userRequestDTO.getEmail());
//        user.setPassword(userRequestDTO.getPassword());
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : userRequestDTO.getRoles()) {
            Role role = roleService.findRoleById(roleId);
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    @Override
    public String deleteUser(Integer userId) {
        User existingUser = getUserById(userId);
        userRepo.delete(existingUser);
        return "User with userId: " + userId + " deleted successfully !!!";

    }
}
