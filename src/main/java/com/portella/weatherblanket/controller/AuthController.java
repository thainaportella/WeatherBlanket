package com.portella.weatherblanket.controller;

import com.portella.weatherblanket.entities.DTOs.AuthDataDTO;
import com.portella.weatherblanket.model.LoginResponse;
import com.portella.weatherblanket.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public LoginResponse logIn(@RequestBody AuthDataDTO data) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.user(), data.password());

        manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken(data.user());
        return new LoginResponse().accessToken(tokenJWT);
    }
}
