package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    AppUser findAppUserByUserName(String username);
    AppUser createCredentials(AppUser appUser);
}
