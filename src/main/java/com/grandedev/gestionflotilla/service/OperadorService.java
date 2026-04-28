package com.grandedev.gestionflotilla.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.dto.OperadorDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;

@Service
public class OperadorService implements IOperadorService{
    private final DocumentoRepository documentoRepository;
    private final OperadorRepository operadorRepository;

    public OperadorService(DocumentoRepository documentoRepository, OperadorRepository operadorRepository) {
        this.documentoRepository = documentoRepository;
        this.operadorRepository = operadorRepository;
    }
    
    private Operador buscarOperadorEntidadPorId(Long id) {
        return this.operadorRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Falla en buscar operador con id: " + id));
    }

    @Override
    public OperadorDTO buscarOperadorPorId(Long id) {
        return Mapper.toOperadorDTO(this.buscarOperadorEntidadPorId(id));
    }

    @Override
    public OperadorDTO crearOperador(OperadorDTO operadorDTO) {
        return Mapper.toOperadorDTO(this.operadorRepository.save(Mapper.toOperador(operadorDTO)));
    }

    @Override
    public List<OperadorDTO> listarOperadores() {
        return this.operadorRepository.findAll().stream().map(Mapper::toOperadorDTO).toList();
    }

    @Override
    public OperadorDTO actualizarOperador(Long id, OperadorDTO operadorActualizado) {
        Operador operador = this.buscarOperadorEntidadPorId(id);
        operador.setNombre(operadorActualizado.getNombre());
        operador.setLicencia(operadorActualizado.getLicencia());
        operador.setVencimientoLicencia(operadorActualizado.getVencimientoLicencia());
        operador.setNss(operadorActualizado.getNss());
        operador.setRfc(operadorActualizado.getRfc());
        operador.setCurp(operadorActualizado.getCurp());

        return Mapper.toOperadorDTO(this.operadorRepository.save(operador));
    }

    @Override
    public void eliminarOperadorPorId(Long id) {
        this.buscarOperadorEntidadPorId(id);
        this.operadorRepository.deleteById(id);
    }

    @Override
    public boolean operadorExistePorId(Long id) {
        return this.operadorRepository.existsById(id);
    }

    @Override
    public OperadorDTO buscarOperadorPorDocumento(Long documentoId) {
        this.documentoRepository
            .findById(documentoId)
            .orElseThrow(() -> new IllegalArgumentException("Documento con Id: " + documentoId + " no ha sido encontrado"));

        return Mapper.toOperadorDTO(this.operadorRepository.findByDocumentosId(documentoId));
    }
}
