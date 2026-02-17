package com.portella.weatherblanket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Value("${api.security.login.username}")
    private String adminLogin;

    @Value("${api.security.login.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminLogin.equals(username)) {
            return User.builder()
                    .username(adminLogin)
                    .password(adminPassword)
                    .roles("USER")
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
