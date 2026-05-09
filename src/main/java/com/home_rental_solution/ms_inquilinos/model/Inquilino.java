package com.home_rental_solution.ms_inquilinos.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquilino {

    @NotNull(message = "El ID del Inquilino no debe estar vacio")
    private Integer idInquilino;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email valido")
    private String email;

    @NotNull(message = "El historial de reservas no puede ser nulo")
    private List<String> historialReservas;
}
