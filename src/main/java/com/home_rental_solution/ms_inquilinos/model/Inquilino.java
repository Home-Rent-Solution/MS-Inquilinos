package com.home_rental_solution.ms_inquilinos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inquilinos")
public class Inquilino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInquilino;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @ElementCollection
    @CollectionTable(name = "inquilino_historial_reservas", joinColumns = @JoinColumn(name = "id_inquilino"))
    @Column(name = "reserva", nullable = false, length = 200)
    private List<String> historialReservas = new ArrayList<>();

    @Column(nullable = false)
    private boolean bloqueado = false;
}
