package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.dto.CamionDTO;

public interface ICamionService {
    public CamionDTO buscarCamionPorId(Long id);
    public CamionDTO actualizarCamion(CamionDTO camionActualizado, Long id);
    public CamionDTO crearCamion(CamionDTO camionDTO);
    public List<CamionDTO> listarCamiones();
    public void eliminarCamionPorId(Long id);
    public boolean camionExistePorId(Long id);
    public CamionDTO asignarOperador(Long camionId, Long operadorId);
}
