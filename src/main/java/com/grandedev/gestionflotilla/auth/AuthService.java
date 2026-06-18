package com.grandedev.gestionflotilla.auth;

import com.grandedev.gestionflotilla.config.JwtService;
import com.grandedev.gestionflotilla.exception.EmailAlreadyExistsException;
import com.grandedev.gestionflotilla.exception.UsernameAlreadyExistsException;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Este endpoint queda protegido para ADMIN; el registro publico queda deshabilitado desde SecurityConfig.
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Nombre de usuario NO disponible. Intenta con otro.");
        }
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("El email que intentas proporcionar se encuentra en uso. Prueba con otro.");
        }

        var user = Usuario
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                // El rol viene del request para pruebas; en produccion conviene restringir quien puede crear ADMIN.
                .rol(request.getRol())
                .build();
        usuarioRepository.save(user);

        // Al registrarse devolvemos tokens para que Postman pueda probar endpoints protegidos enseguida.
        var jwtToken = jwtTokenService.generateToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .rol(user.getRol().name())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtTokenService.generateToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .rol(user.getRol().name())
                .build();
    }
}
