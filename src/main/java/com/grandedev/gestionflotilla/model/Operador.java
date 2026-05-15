package com.grandedev.gestionflotilla.model;
import java.util.List;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String licencia;

    private LocalDate vencimientoLicencia;

    @OneToMany(mappedBy = "operador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos;

    private String nss; 
    private String rfc; 
    private String curp;
}
