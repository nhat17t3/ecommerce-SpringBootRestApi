package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.RoleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Role;
import com.nhat.demoSpringbooRestApi.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getAllCategories() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id_1}")
    public ResponseEntity<Role> findRoleById(@PathVariable("id_1") int id) {
        Role role = roleService.findRoleById(id);
        return ResponseEntity.ok(role);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Role>> findRoleByName(@RequestParam(value="name", required=true) String key) {
//        List<Role> roles = roleService.findRoleByName(key);
//        return ResponseEntity.ok(roles);
//    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleRequestDTO requestDTO) {
        Role role = new Role();
        role.setName(requestDTO.getName());

        Role createdRole = roleService.createRole(role);

        return ResponseEntity.ok(createdRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable int id, @Valid @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(id, role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable int id) {
        String message = roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


}
