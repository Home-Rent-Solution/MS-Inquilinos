package com.home_rental_solution.ms_inquilinos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquilinosResponseDTO {

    private Long idInquilino;
    private String nombre;
    private String email;
    private List<String> historialReservas;
    private boolean bloqueado;
}
