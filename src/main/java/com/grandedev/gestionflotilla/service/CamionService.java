package com.grandedev.gestionflotilla.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.dto.CamionDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.repository.CamionRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;

@Service
public class CamionService implements ICamionService{

    private final CamionRepository camionRepository;
    private final OperadorRepository operadorRepository;

    public CamionService(CamionRepository camionRepository, OperadorRepository operadorRepository) {
        this.camionRepository = camionRepository;
        this.operadorRepository = operadorRepository;
    }

    private Camion buscarCamionEntidadPorId(Long id) {
        return this.camionRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + id + " no encontrado"));
    }

    @Override
    public CamionDTO buscarCamionPorId(Long id) {
        return Mapper.toCamionDTO(this.buscarCamionEntidadPorId(id));
    }

    @Override
    public CamionDTO actualizarCamion(CamionDTO camionActualizado, Long id) {
        Camion camion = this.buscarCamionEntidadPorId(id);
        actualizarCamposCamion(camion, camionActualizado);
        asignarOperadorSiExiste(camion, camionActualizado.getIdOperador());

        return Mapper.toCamionDTO(this.camionRepository.save(camion));
    }

    @Override
    public CamionDTO crearCamion(CamionDTO camionDTO) {
        Camion camion = Mapper.toCamion(camionDTO);
        asignarOperadorSiExiste(camion, camionDTO.getIdOperador());

        return Mapper.toCamionDTO(this.camionRepository.save(camion));
    }

    @Override
    public List<CamionDTO> listarCamiones() {
        return this.camionRepository.findAll().stream().map(Mapper::toCamionDTO).toList();
    }

    @Override
    public void eliminarCamionPorId(Long id) {
        this.buscarCamionEntidadPorId(id);
        this.camionRepository.deleteById(id);
    }

    @Override
    public boolean camionExistePorId(Long id) {
        return this.camionRepository.existsById(id);
    }

    @Override
    public CamionDTO asignarOperador(Long camionId, Long operadorId) {
        Camion camion = this.buscarCamionEntidadPorId(camionId);
        Operador operador = this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no encontrado"));

        camion.setOperador(operador);
        return Mapper.toCamionDTO(this.camionRepository.save(camion));
    }

    private void actualizarCamposCamion(Camion camion, CamionDTO camionActualizado) {
        camion.setMarca(camionActualizado.getMarca());
        camion.setVersion(camionActualizado.getVersion());
        camion.setAnio(camionActualizado.getAnio());
        camion.setMotor(camionActualizado.getMotor());
        camion.setSerie(camionActualizado.getSerie());
        camion.setInicioVigencia(camionActualizado.getInicioVigencia());
        camion.setFinVigencia(camionActualizado.getFinVigencia());
        camion.setFormaDePago(camionActualizado.getFormaDePago());
        camion.setRutaArchivo(camionActualizado.getRutaArchivo());
        camion.setFechaDePago(camionActualizado.getFechaDePago());
        camion.setVencimientoPago(camionActualizado.getVencimientoPago());
    }

    private void asignarOperadorSiExiste(Camion camion, Long operadorId) {
        if (operadorId == null) {
            return;
        }

        Operador operador = this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no encontrado"));
        camion.setOperador(operador);
    }
}
