package com.mechanicfinder.mechanicfindersystem.securityconfig;

import com.mechanicfinder.mechanicfindersystem.model.AppUser;
import com.mechanicfinder.mechanicfindersystem.model.Customer;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomMechanicFinderSuccessHandler implements AuthenticationSuccessHandler {
    private final AppUserService appUserService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, 
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_MECHANIC")){
                Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication1.getName();
                AppUser appUserByUserName = appUserService.findAppUserByUserName(username);
                Mechanic mechanic = appUserByUserName.getMechanic();
                redirectUrl = "/api/mechanic/"+mechanic.getId();
                break;

            }else if (authority.getAuthority().equals("ROLE_CUSTOMER")){
                Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication1.getName();
                AppUser appUserByUserName = appUserService.findAppUserByUserName(username);
                Customer customer = appUserByUserName.getCustomer();
                redirectUrl = "/api/customer/"+customer.getId();
                break;
            }
        }

        if (redirectUrl == null){
            throw new IllegalStateException();
        }
        new DefaultRedirectStrategy().sendRedirect(httpServletRequest,
                httpServletResponse,redirectUrl);
    }
}
