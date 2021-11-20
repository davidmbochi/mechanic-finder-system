package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleService{
    Role findRoleByRoleName(String roleName);
}
