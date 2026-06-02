package com.grandedev.gestionflotilla.auth;

import com.grandedev.gestionflotilla.login.LoginRequestDTO;
import com.grandedev.gestionflotilla.config.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService implements IAuthService {
    private final JwtService jwtTokenService;

    AuthService(JwtService jwtTokenService){this.jwtTokenService = jwtTokenService;}

    @Override
    public String login(@RequestBody LoginRequestDTO loginRequestDTO){

    }

}
