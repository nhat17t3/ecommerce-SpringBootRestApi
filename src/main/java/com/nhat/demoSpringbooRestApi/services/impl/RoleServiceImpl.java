package com.nhat.demoSpringbooRestApi.services.impl;

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
    public Role createRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public Role updateRole(int roleId, Role role) {
        Role existingRole = findRoleById(roleId);
        existingRole.setName(role.getName());
        return roleRepo.save(existingRole);
    }

    @Override
    public String deleteRole(int roleId) {
        Role existingRole = findRoleById(roleId);
        roleRepo.delete(existingRole);
        return "role has id =" + roleId + ": is deleted";
    }

}
