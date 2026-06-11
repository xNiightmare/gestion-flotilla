package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.dto.DocumentoDTO;


public interface IDocumentoService {
    public DocumentoDTO buscarDocumentoPorId(Long id);
    public List<DocumentoDTO> listarDocumentos();
    public boolean documentoExistePorId(Long id);
    public List<DocumentoDTO> listarDocumentosPorOperador(Long operadorId);
    public List<DocumentoDTO> listarDocumentoPorCamion(Long camionId);
    public DocumentoDTO asignarDocumentoAOperador(Long documentoId, Long operadorId);
    public DocumentoDTO asignarDocumentoACamion(Long documentoId, Long camionId);
    public void eliminarDocumentoFisicoYLogico(Long documentoId);
    
}
