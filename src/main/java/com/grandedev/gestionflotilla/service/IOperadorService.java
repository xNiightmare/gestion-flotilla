package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.dto.OperadorDTO;

public interface IOperadorService {
    public OperadorDTO buscarOperadorPorId(Long id);
    public OperadorDTO crearOperador(OperadorDTO operadorDTO);
    public List<OperadorDTO> listarOperadores();
    public OperadorDTO actualizarOperador(Long id, OperadorDTO operadorActualizado);
    public void eliminarOperadorPorId(Long id);
    public boolean operadorExistePorId(Long id);
    public OperadorDTO buscarOperadorPorDocumento(Long documentoId);
}
