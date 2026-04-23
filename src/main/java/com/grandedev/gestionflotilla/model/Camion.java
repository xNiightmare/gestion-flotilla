package com.grandedev.gestionflotilla.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "camiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "operador_id")
    private Operador operador;
    
    private String marca;
    private String version;
    private int anio;
    private String motor;
    private String serie;

    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "camion_id")
    private List<Documento> documentos; //Tipo de documento que se le asigna al camion, puede ser un complemento o factura, dependiendo del caso
    
    private LocalDate inicioVigencia;
    private LocalDate finVigencia;
    private String formaDePago;
    private String rutaArchivo; //Complemento o factura
    private LocalDate fechaDePago;
    private LocalDate vencimientoPago;
}
