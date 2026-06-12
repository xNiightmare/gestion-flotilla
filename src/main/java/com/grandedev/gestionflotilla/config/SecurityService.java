package com.grandedev.gestionflotilla.config;

import com.grandedev.gestionflotilla.exception.ResourceNotFoundException;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("securityService")
public class SecurityService {
        private final DocumentoRepository documentoRepository;

        public SecurityService(DocumentoRepository documentoRepository){
            this.documentoRepository = documentoRepository;
        }

        public boolean esMiDocumento(Long docId, Authentication authentication)
         {

            Usuario usuario = (Usuario) authentication.getPrincipal();
            Documento documento =
                    documentoRepository.findById(docId)
                            .orElseThrow(() -> new ResourceNotFoundException("Documento NO encontrado"));

            assert usuario != null;
            return documento.getOperador()
                    .getId()
                    .equals(
                            usuario.getOperador().getId()
                    );
        }
    }



