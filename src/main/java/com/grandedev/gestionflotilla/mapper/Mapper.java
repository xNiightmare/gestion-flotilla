package com.grandedev.gestionflotilla.mapper;

import java.util.stream.Collectors;

import com.grandedev.gestionflotilla.dto.CamionDTO;
import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.dto.OperadorDTO;
import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.model.Usuario;

public class Mapper {

    public static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        return UsuarioResponseDTO.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .rol(usuario.getRol())
            .build();
    }

    public static Usuario toUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if (usuarioRequestDTO == null) {
            return null;
        }

        return Usuario.builder()
            .username(usuarioRequestDTO.getUsername())
            .password(usuarioRequestDTO.getPassword())
            .rol(usuarioRequestDTO.getRol())
            .build();
    }

    public static DocumentoDTO toDocumentoDTO(Documento documento) {
        if (documento == null) {
            return null;
        }

        return DocumentoDTO.builder()
            .id(documento.getId())
            .tipoDocumento(documento.getTipoDocumento())
            .nombreArchivo(documento.getNombreArchivo())
            .rutaArchivo(documento.getRutaArchivo())
            .fechaSubida(documento.getFechaSubida())
            .idOperador(documento.getOperador() != null ? documento.getOperador().getId() : null)
            .idCamion(documento.getCamion() != null ? documento.getCamion().getId() : null)
            .build();
    }

    public static Documento toDocumento(DocumentoDTO documentoDTO) {
        if (documentoDTO == null) {
            return null;
        }

        return Documento.builder()
            .id(documentoDTO.getId())
            .tipoDocumento(documentoDTO.getTipoDocumento())
            .nombreArchivo(documentoDTO.getNombreArchivo())
            .rutaArchivo(documentoDTO.getRutaArchivo())
            .fechaSubida(documentoDTO.getFechaSubida())
            .build();
    }

    public static CamionDTO toCamionDTO(Camion camion) {
        if (camion == null) {
            return null;
        }

        var documentos = camion.getDocumentos() != null
            ? camion.getDocumentos().stream().map(Mapper::toDocumentoDTO).collect(Collectors.toList())
            : null;

        return CamionDTO.builder()
            .id(camion.getId())
            .idOperador(camion.getOperador() != null ? camion.getOperador().getId() : null)
            .marca(camion.getMarca())
            .version(camion.getVersion())
            .motor(camion.getMotor())
            .anio(camion.getAnio())
            .serie(camion.getSerie())
            .inicioVigencia(camion.getInicioVigencia())
            .finVigencia(camion.getFinVigencia())
            .formaDePago(camion.getFormaDePago())
            .rutaArchivo(camion.getRutaArchivo())
            .fechaDePago(camion.getFechaDePago())
            .vencimientoPago(camion.getVencimientoPago())
            .documentos(documentos)
            .build();
    }

    public static Camion toCamion(CamionDTO camionDTO) {
        if (camionDTO == null) {
            return null;
        }

        return Camion.builder()
            .id(camionDTO.getId())
            .marca(camionDTO.getMarca())
            .version(camionDTO.getVersion())
            .motor(camionDTO.getMotor())
            .anio(camionDTO.getAnio())
            .serie(camionDTO.getSerie())
            .inicioVigencia(camionDTO.getInicioVigencia())
            .finVigencia(camionDTO.getFinVigencia())
            .formaDePago(camionDTO.getFormaDePago())
            .rutaArchivo(camionDTO.getRutaArchivo())
            .fechaDePago(camionDTO.getFechaDePago())
            .vencimientoPago(camionDTO.getVencimientoPago())
            .build();
    }

    public static OperadorDTO toOperadorDTO(Operador operador) {
        if (operador == null) {
            return null;
        }

        var documentos = operador.getDocumentos() != null
            ? operador.getDocumentos().stream().map(Mapper::toDocumentoDTO).collect(Collectors.toList())
            : null;

        return OperadorDTO.builder()
            .id(operador.getId())
            .nombre(operador.getNombre())
            .licencia(operador.getLicencia())
            .vencimientoLicencia(operador.getVencimientoLicencia())
            .nss(operador.getNss())
            .rfc(operador.getRfc())
            .curp(operador.getCurp())
            .documentos(documentos)
            .build();
    }

    public static Operador toOperador(OperadorDTO operadorDTO) {
        if (operadorDTO == null) {
            return null;
        }

        return Operador.builder()
            .id(operadorDTO.getId())
            .nombre(operadorDTO.getNombre())
            .licencia(operadorDTO.getLicencia())
            .vencimientoLicencia(operadorDTO.getVencimientoLicencia())
            .nss(operadorDTO.getNss())
            .rfc(operadorDTO.getRfc())
            .curp(operadorDTO.getCurp())
            .build();
    }
}
