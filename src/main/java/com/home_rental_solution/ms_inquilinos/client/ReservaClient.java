package com.home_rental_solution.ms_inquilinos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "ms-reservas"
)
public interface ReservaClient {

    // GET /api/v1/reservas/inquilino/{idInquilino}
    @GetMapping("/api/v1/reservas/inquilino/{idInquilino}")
    List<Object> obtenerReservasPorInquilino(@PathVariable Long idInquilino);
}
