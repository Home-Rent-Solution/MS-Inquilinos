package com.home_rental_solution.ms_inquilinos.controller;

import com.home_rental_solution.ms_inquilinos.assemblers.InquilinosModelAssembler;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InquilinosControllerV2Test {

    @Mock
    private InquilinoService inquilinoService;

    @Mock
    private InquilinosModelAssembler assembler;

    @InjectMocks
    private InquilinosControllerV2 controller;

    private InquilinosResponseDTO responseDto;
    private InquilinosRequestDTO requestDto;
    private EntityModel<InquilinosResponseDTO> model;

    @BeforeEach
    void setUp() {
        responseDto = new InquilinosResponseDTO(
                1L,
                "Diego Silva",
                "diego@mail.com",
                new ArrayList<>(List.of("RES-1")),
                false
        );
        requestDto = new InquilinosRequestDTO("Diego Silva", "diego@mail.com");
        model = EntityModel.of(responseDto);
    }

    @Test
    void getInquilinosRetornaColeccionHateoas() {
        when(inquilinoService.mostrarInquilinos()).thenReturn(List.of(responseDto));
        when(assembler.toModel(responseDto)).thenReturn(model);

        ResponseEntity<CollectionModel<EntityModel<InquilinosResponseDTO>>> response =
                controller.getInquilinos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void getPorIdRetornaModeloHateoas() {
        when(inquilinoService.mostrarPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(model);

        ResponseEntity<EntityModel<InquilinosResponseDTO>> response = controller.getPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(model, response.getBody());
    }

    @Test
    void postInquilinoRetornaCreado() {
        when(inquilinoService.save(requestDto)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(model);

        ResponseEntity<EntityModel<InquilinosResponseDTO>> response =
                controller.postInquilino(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(model, response.getBody());
    }

    @Test
    void putInquilinoRetornaModeloActualizado() {
        when(inquilinoService.editar(1L, requestDto)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(model);

        ResponseEntity<EntityModel<InquilinosResponseDTO>> response =
                controller.putInquilino(1L, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(model, response.getBody());
    }

    @Test
    void deleteInquilinoRetornaSinContenido() {
        ResponseEntity<Void> response = controller.deleteInquilino(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(inquilinoService).borrar(1L);
    }

    @Test
    void getHistorialRetornaReservas() {
        when(inquilinoService.mostrarHistorial(1L)).thenReturn(List.of("RES-1", "RES-2"));

        ResponseEntity<List<String>> response = controller.getHistorial(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of("RES-1", "RES-2"), response.getBody());
    }

    @Test
    void validarRetornaEstadoDelInquilino() {
        when(inquilinoService.validar(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.validar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void bloquearRetornaModeloConNuevoEstado() {
        when(inquilinoService.bloquear(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(model);

        ResponseEntity<EntityModel<InquilinosResponseDTO>> response = controller.bloquear(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(model, response.getBody());
    }
}
