package com.home_rental_solution.ms_inquilinos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email valido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @ElementCollection
    @CollectionTable(name = "inquilino_historial_reservas", joinColumns = @JoinColumn(name = "id_inquilino"))
    @Size(max = 200, message = "La reserva no puede superar los 200 caracteres")
    @Column(name = "reserva", nullable = false, length = 200)
    private List<String> historialReservas = new ArrayList<>();

    @Column(nullable = false)
    private boolean bloqueado = false;
}
