package com.grandedev.gestionflotilla.seed;

import com.grandedev.gestionflotilla.config.AdminSeedProperties;
import com.grandedev.gestionflotilla.model.Rol;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SeedData.class);
    private final UsuarioRepository usuarioRepository;
    private final AdminSeedProperties seedProperties;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.telegram.chat-id}")
    private Long chatId;

    public SeedData(UsuarioRepository usuarioRepository,
                    AdminSeedProperties seedProperties,
                    PasswordEncoder passwordEncoder)
    {
        this.usuarioRepository = usuarioRepository;
        this.seedProperties = seedProperties;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void run(String ... args) throws Exception{

        logger.info("Iniciando siembra de datos \n");
        logger.info("Verificnado si existe admin principal");

        boolean existeAdmin =
                usuarioRepository
                        .existsByRol(Rol.ADMIN);

        if (!existeAdmin){
            Usuario admin = Usuario.builder()
                    .username(seedProperties.getUsername())
                    .password(
                            passwordEncoder.encode(
                                    seedProperties.getPassword()
                            )
                    )
                    .email(seedProperties.getEmail())
                    .telegramChatId(this.chatId)
                    .rol(Rol.ADMIN)
                    .build();

            usuarioRepository.save(admin);
            logger.info("Usuario administrador sembrado con exito");
        }
        else {
            logger.info("Ya hay un usuario principal administrador");
        }

    }



}
