package com.grandedev.gestionflotilla.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.dto.UsuarioDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; //Para cifrar la contrasenia

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioDTO> traerUsuarios(){
        return usuarioRepository.findAll().stream().map(Mapper::toUsuarioDTO).toList();
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO){

        Usuario usuario = Usuario.builder()
                .username(usuarioDTO.getUsername())
                .password(usuarioDTO.getPassword()) //TODO: Implementar medida de seguridad para password
                .rol(usuarioDTO.getRol())
                .build();
            return Mapper.toUsuarioDTO(usuarioRepository.save(usuario));
    }
    
    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO){
        return null;
    }

    @Override
    public void eliminarUsuario(Long id){
        System.out.println("Saass sasa sale de volada!!!");
    }
}
