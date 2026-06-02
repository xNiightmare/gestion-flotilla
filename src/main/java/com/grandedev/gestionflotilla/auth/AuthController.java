package com.grandedev.gestionflotilla.auth;

import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.config.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//REST controller para la autenticacion de endpoints
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtTokenService;

    AuthController(JwtService jwtTokenService){ this.jwtTokenService = jwtTokenService;}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRequestDTO usuarioRequestDTO){


    } //Esto no puede ni debe estar aqui, viola la arquitectura seguida y pierde sentido toda la seguridad.
}
