package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    List<Role> getAllRoles ();

    Role findRoleById(int roleId);

    Role findRoleByName (String name);

    Role createRole (Role role);

    Role updateRole (int roleId, Role role);

    String deleteRole (int roleId);

}
