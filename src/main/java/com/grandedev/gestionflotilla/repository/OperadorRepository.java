package com.grandedev.gestionflotilla.repository;

import com.grandedev.gestionflotilla.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperadorRepository extends JpaRepository<Operador, Long> {
    public Operador findByDocumentosId(Long documentoId);
}
