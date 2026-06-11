package com.home_rental_solution.ms_inquilinos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquilinosRequestDTO {

    //idInquilino no se incluye porque MySQL lo genera
    //bloqueado no se incluye porque se maneja con put
    //historialReserva no se incluye porque el cliente no lo maneja

    @NotBlank(message = "El nombre es obligatorio")
    @Size(
            max = 120,
            message = "El nombre no puede superar los 120 caracteres"
    )
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email valido")
    @Size(
            max = 150,
            message = "El email no puede superar los 150 caracteres"
    )
    private String email;

}
