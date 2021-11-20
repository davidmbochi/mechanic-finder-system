package com.mechanicfinder.mechanicfindersystem.securityconfig;

import com.mechanicfinder.mechanicfindersystem.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class MechanicFinderAppSecurity extends WebSecurityConfigurerAdapter {
    private final AppUserService appUserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(appUserService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/task/all").permitAll()
                .antMatchers("/api/task/{taskName}").permitAll()
                .antMatchers("/api/task/task-form/{id}").hasAuthority("ROLE_MECHANIC")
                .antMatchers("/api/task/processTaskForm/{id}").hasAuthority("ROLE_MECHANIC")
                .antMatchers("/api/mechanic/register").permitAll()
                .antMatchers("/api/mechanic/process-mechanic-reg-form").permitAll()
                .antMatchers("/api/mechanic/{id}").hasAuthority("ROLE_MECHANIC")
                .antMatchers("/api/customer/register/{id}/{taskName}").permitAll()
                .antMatchers("/api/customer/process-customer-reg-form/{id}/{taskName}").hasAuthority("ROLE_CUSTOMER")
                .antMatchers("/api/customer/{id}").hasAuthority("ROLE_CUSTOMER")
                .and()
                .formLogin()
                .loginPage("/login-page")
                .loginProcessingUrl("/process-login")
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/api/task/all")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }
}
