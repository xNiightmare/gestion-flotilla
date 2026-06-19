package com.grandedev.gestionflotilla.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grandedev.gestionflotilla.model.Alerta;
import com.grandedev.gestionflotilla.model.TipoAlerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    List<Alerta> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long usuarioId);
    Optional<Alerta> findByIdAndUsuarioId(Long id, Long usuarioId);
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
    boolean existsByUsuarioIdAndDocumentoIdAndTipo(Long usuarioId, Long documentoId, TipoAlerta tipo);
    List<Alerta> findByUsuarioIdAndLeidaFalse(Long usuarioId);
}
