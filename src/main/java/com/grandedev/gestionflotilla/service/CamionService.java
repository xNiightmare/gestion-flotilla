package com.grandedev.gestionflotilla.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.repository.CamionRepository;

@Service
public class CamionService {

    private final CamionRepository camionRepository;

    public CamionService(CamionRepository camionRepository){
        this.camionRepository = camionRepository;
    }

    public Camion buscarCamionPorId(Long id){
        return this.camionRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + id + " no encontrado"));
    }

    public Camion actualizarCamion(Camion camionActualizado, Long id){
        return this.camionRepository
            .findById(id)
            .map(camion ->{
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

                return this.camionRepository.save(camion);
            })
            .orElseGet(() -> {
                    return this.camionRepository.save(camionActualizado);
                });
            
    }

   public Camion crearCamion(Camion camion){
        return this.camionRepository.save(camion);
   }

   public List<Camion> listarCamiones(){
    return this.camionRepository.findAll();
   }

   public void eliminarCamionPorId(Long id){
        this.camionRepository.deleteById(id);
   }

   public void eliminarCamion(Camion camion){
       this.camionRepository.delete(camion);
   }

   public boolean camionExistePorId(Long id){
    return this.camionRepository.existsById(id);
   }

   
}
