package com.mechanicfinder.mechanicfindersystem.service;

import com.mechanicfinder.mechanicfindersystem.model.AppUser;
import com.mechanicfinder.mechanicfindersystem.model.Role;
import com.mechanicfinder.mechanicfindersystem.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{
    private final AppUserRepository appUserRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        return new User(appUser.getUsername(),
                appUser.getPassword(),
                mapRolesToAuthority(appUser.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthority(Collection<Role> roles){
        return roles
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    @Override
    public AppUser findAppUserByUserName(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    @Override
    public AppUser createCredentials(AppUser appUser) {
        return appUserRepository.save(appUser);
    }
}
