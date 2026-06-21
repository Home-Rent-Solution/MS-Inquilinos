package com.home_rental_solution.ms_inquilinos.assemblers;

import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InquilinosModelAssemblerTest {

    private final InquilinosModelAssembler assembler = new InquilinosModelAssembler();

    @Test
    void agregaEnlacesParaInquilinoActivo() {
        InquilinosResponseDTO dto = crearInquilino(false);

        EntityModel<InquilinosResponseDTO> model = assembler.toModel(dto);

        assertSame(dto, model.getContent());
        assertTrue(model.getLink("self").isPresent());
        assertTrue(model.getLink("historial").isPresent());
        assertTrue(model.getLink("validar_aptitud").isPresent());
        assertTrue(model.getLink("bloquear_inquilino").isPresent());
        assertTrue(model.getLink("desbloquear_inquilino").isEmpty());
    }

    @Test
    void agregaEnlaceParaDesbloquearInquilinoBloqueado() {
        InquilinosResponseDTO dto = crearInquilino(true);

        EntityModel<InquilinosResponseDTO> model = assembler.toModel(dto);

        assertTrue(model.getLink("self").isPresent());
        assertTrue(model.getLink("historial").isPresent());
        assertTrue(model.getLink("desbloquear_inquilino").isPresent());
        assertTrue(model.getLink("validar_aptitud").isEmpty());
        assertTrue(model.getLink("bloquear_inquilino").isEmpty());
    }

    private InquilinosResponseDTO crearInquilino(boolean bloqueado) {
        return new InquilinosResponseDTO(
                1L,
                "Diego Silva",
                "diego@mail.com",
                new ArrayList<>(),
                bloqueado
        );
    }
}
