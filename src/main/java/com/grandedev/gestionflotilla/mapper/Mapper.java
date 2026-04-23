package com.grandedev.gestionflotilla.mapper;


import java.util.stream.Collectors;

import com.grandedev.gestionflotilla.dto.UsuarioDTO;
import com.grandedev.gestionflotilla.dto.CamionDTO;
import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.dto.OperadorDTO;

import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.model.Documento;

public class Mapper {
    //Mapeo de Usuario a UsuarioDTO
    public static UsuarioDTO toUsuarioDTO(Usuario usuario){
        if (usuario == null) {
            return null;
        }

        return UsuarioDTO.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .rol(usuario.getRol())
            .build();
    }

    //Mapeo de Documento a DocumentoDTO This is no te first time You try to get away This is not a party 
    public static DocumentoDTO toDocumentoDTO(Documento documento){
        if (documento == null) {
            return null;
        }

        return DocumentoDTO.builder()
            .id(documento.getId())
            .tipoDocumento(documento.getTipoDocumento())
            .nombreArchivo(documento.getNombreArchivo())
            .rutaArchivo(documento.getRutaArchivo())
            .fechaSubida(documento.getFechaSubida())
            .idOperador(documento.getOperador().getId())
            .idCamion(documento.getCamion().getId())

            .build();
    }

    //Mapeo de Camion a CamionDTO
    public static CamionDTO toCamionDTO(Camion camion){
        if (camion == null) {
            return null;
        }

        var documento = camion.getDocumentos() != null ? camion.getDocumentos().stream()
            .map(doc -> DocumentoDTO.builder()
                .id(doc.getId())
                .tipoDocumento(doc.getTipoDocumento())
                .nombreArchivo(doc.getNombreArchivo())
                .rutaArchivo(doc.getRutaArchivo())
                .fechaSubida(doc.getFechaSubida())
                .build()
            ).collect(Collectors.toList()) : null;

        return CamionDTO.builder()
            .id(camion.getId())
            .idOperador(camion.getOperador().getId())
            .marca(camion.getMarca())
            .motor(camion.getMotor())
            .anio(camion.getAnio())
            .serie(camion.getSerie())
            .inicioVigencia(camion.getInicioVigencia())
            .finVigencia(camion.getFinVigencia())
            .formaDePago(camion.getFormaDePago())
            .rutaArchivo(camion.getRutaArchivo())
            .fechaDePago(camion.getFechaDePago())
            .vencimientoPago(camion.getVencimientoPago())
            .documentos(documento)
            .build();
    }

    //Mapeo de Operador a OperadorDTO
    public static OperadorDTO toOperadorDTO(Operador operador){
        if (operador == null) {
            return null;
        }

        var documento = operador.getDocumentos() != null ? operador.getDocumentos().stream()
            .map(doc -> DocumentoDTO.builder()
                .id(doc.getId())
                .tipoDocumento(doc.getTipoDocumento())
                .nombreArchivo(doc.getNombreArchivo())
                .rutaArchivo(doc.getRutaArchivo())
                .fechaSubida(doc.getFechaSubida())
                .build()
            ).collect(Collectors.toList()) : null;

        return OperadorDTO.builder()
            .id(operador.getId())
            .nombre(operador.getNombre())
            .licencia(operador.getLicencia())
            .vencimientoLicencia(operador.getVencimientoLicencia())
            .nss(operador.getNss())
            .rfc(operador.getRfc())
            .curp(operador.getCurp())
            .documentos(documento)
            .build();
    }
}
