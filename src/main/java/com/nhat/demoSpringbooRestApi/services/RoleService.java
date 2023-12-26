package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.RoleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    List<Role> getAllRoles ();

    Role findRoleById(int roleId);

    Role findRoleByName (String name);

    Role createRole (RoleRequestDTO roleRequestDTO);

    Role updateRole (int roleId, RoleRequestDTO roleRequestDTO);

    void deleteRole (int roleId);

}
