package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.RoleRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.repositories.RoleRepo;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role findRoleById(int roleId) {
        return roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepo.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    @Override
    public Role createRole(RoleRequestDTO roleRequestDTO) {
        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        return roleRepo.save(role);
    }

    @Override
    public Role updateRole(int roleId, RoleRequestDTO roleRequestDTO) {
        Role existingRole = findRoleById(roleId);
        existingRole.setName(roleRequestDTO.getName());
        return roleRepo.save(existingRole);
    }

    @Override
    public void deleteRole(int roleId) {
        Role existingRole = findRoleById(roleId);
        roleRepo.delete(existingRole);
    }

}
