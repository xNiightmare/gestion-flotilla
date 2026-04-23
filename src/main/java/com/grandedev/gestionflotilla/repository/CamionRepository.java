package com.grandedev.gestionflotilla.repository;

import com.grandedev.gestionflotilla.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamionRepository extends JpaRepository<Camion,Long>{
    
}
