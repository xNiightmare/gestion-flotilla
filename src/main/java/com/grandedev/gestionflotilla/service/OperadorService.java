package com.grandedev.gestionflotilla.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;

@Service
public class OperadorService {
    private final DocumentoRepository documentoRepository;
    private final OperadorRepository operadorRepository;

    public OperadorService(DocumentoRepository documentoRepository, OperadorRepository operadorRepository){
        this.documentoRepository = documentoRepository;
        this.operadorRepository = operadorRepository;
    }

    public Operador buscarOperadorPorId(Long id){
        return this.operadorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Falla en buscar operador con id: "+ id));
    }

    public Operador crearOperador(Operador operador){
        return this.operadorRepository.save(operador);
    }

    public List<Operador> listarOperadores(){
        return this.operadorRepository.findAll();
    }

    public void eliminarOperadorPorId(Long id){
        this.operadorRepository.deleteById(id);
    }

    public boolean operadorExistePorId(Long id){
        return this.operadorRepository.existsById(id);
    }

    public Operador buscarOperadorPorDocumento(Long DocumentoId){
        this.documentoRepository
            .findById(DocumentoId)
            .orElseThrow(() -> new IllegalArgumentException("Documento con Id: "+ DocumentoId + " no ha sido encontrado"));

        return this.operadorRepository.findByDocumentosId(DocumentoId);
    }
}

