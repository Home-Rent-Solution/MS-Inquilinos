package com.home_rental_solution.ms_inquilinos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-propiedades", url = "${ms.propiedades.url")
public interface PropiedadClient {

    //validar si la propiedad existe y esta disponible
    @GetMapping("/api/v1/propiedades/id")
    Object obtenerPropiedad(@PathVariable Long id);
}
