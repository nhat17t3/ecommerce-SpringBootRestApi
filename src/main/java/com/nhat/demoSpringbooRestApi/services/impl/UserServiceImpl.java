package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.UserRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import com.nhat.demoSpringbooRestApi.services.UserService;
import com.nhat.demoSpringbooRestApi.specifications.UserSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public DataTableResponseDTO<User> getAllUsers(UserFilterRequestDTO userFilterRequestDTO) {
        Sort sortByAndOrder = userFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(userFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(userFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(userFilterRequestDTO.getPageNumber(), userFilterRequestDTO.getPageSize(), sortByAndOrder);
        Specification<User> userSpecification = UserSpecifications.searchByCondition(userFilterRequestDTO);
        Page<User> pageUsers = userRepo.findAll(userSpecification, pageDetails);
        List<User> users = pageUsers.getContent();
        DataTableResponseDTO<User> userResponse = new DataTableResponseDTO<>();
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
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public User registerUser(UserRequestDTO userRequestDTO) throws Exception {
        Optional<User> existedUser = userRepo.findByEmail(userRequestDTO.getEmail());
        if (existedUser.isEmpty()) {
            User user = modelMapper.map(userRequestDTO, User.class);
            Set<Role> roles = new HashSet<>();
            for (String roleName : userRequestDTO.getRoles()) {
                Role role = roleService.findRoleByName(roleName);
                roles.add(role);
            }
            user.setRoles(roles);
            return userRepo.save(user);
        } else {
            throw new Exception("Email" + userRequestDTO.getEmail() + "existed");
        }
    }

    @Override
    public User updateUser(Integer userId, UserRequestDTO userRequestDTO) {
        User user = getUserById(userId);

        user.setName(userRequestDTO.getName());
        user.setPhone(userRequestDTO.getPhone());
        user.setAddress(userRequestDTO.getAddress());
//        user.setEmail(userRequestDTO.getEmail());
//        user.setPassword(userRequestDTO.getPassword());
        Set<Role> roles = new HashSet<>();
        for (String roleName : userRequestDTO.getRoles()) {
            Role role = roleService.findRoleByName(roleName);
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        User existingUser = getUserById(userId);
        userRepo.delete(existingUser);
    }
}
