package com.grandedev.gestionflotilla.repository;

import java.util.List;

import com.grandedev.gestionflotilla.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento,Long>{
    List<Documento> findByOperadorId(Long operadorId);
    List<Documento> findByCamionId(Long camionId);

    
}
