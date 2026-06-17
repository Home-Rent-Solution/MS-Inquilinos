package com.home_rental_solution.ms_inquilinos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(InquilinosController.class) // Indica que se está probando el controlador de Inquilinos
public class InquilinosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquilinoService inquilinoService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private InquilinosResponseDTO inquilinoResponse;
    private InquilinosRequestDTO inquilinoRequest;

    @BeforeEach
    void setUp() {
        // Configura los objetos DTO de ejemplo antes de cada prueba
        inquilinoResponse = new InquilinosResponseDTO(
                1L,
                "Diego Silva",
                "diego.silva@mail.com",
                new ArrayList<>(Arrays.asList(
                        "Reserva en Santiago Centro",
                        "Reserva en Providencia")),
                false // bloqueado = false
        );

        inquilinoRequest = new InquilinosRequestDTO(
                "Diego Silva",
                "diego.silva@mail.com"
        );
    }

    // TESTS CRUD

    @Test
    public void testGetInquilinos() throws Exception {
        when(inquilinoService.mostrarInquilinos()).thenReturn(List.of(inquilinoResponse));
        mockMvc.perform(get("/api/v1/inquilinos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idInquilino").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Diego Silva"))
                .andExpect(jsonPath("$[0].email").value("diego.silva@mail.com"))
                .andExpect(jsonPath("$[0].bloqueado").value(false));
    }

    @Test
    public void testGetPorId() throws Exception {
        when(inquilinoService.mostrarPorId(1L)).thenReturn(inquilinoResponse);
        mockMvc.perform(get("/api/v1/inquilinos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInquilino").value(1))
                .andExpect(jsonPath("$.nombre").value("Diego Silva"))
                .andExpect(jsonPath("$.email").value("diego.silva@mail.com"));
    }

    @Test
    public void testPostInquilino() throws Exception {
        when(inquilinoService.save(any(InquilinosRequestDTO.class))).thenReturn(inquilinoResponse);
        mockMvc.perform(post("/api/v1/inquilinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquilinoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInquilino").value(1))
                .andExpect(jsonPath("$.nombre").value("Diego Silva"));
    }

    @Test
    public void testPutInquilino() throws Exception {
        when(inquilinoService.editar(eq(1L), any(InquilinosRequestDTO.class))).thenReturn(inquilinoResponse);
        mockMvc.perform(put("/api/v1/inquilinos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquilinoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInquilino").value(1))
                .andExpect(jsonPath("$.nombre").value("Diego Silva"));
    }

    @Test
    public void testDeleteInquilino() throws Exception {
        doNothing().when(inquilinoService).borrar(1L);
        mockMvc.perform(delete("/api/v1/inquilinos/1"))
                .andExpect(status().isNoContent());

        verify(inquilinoService, times(1)).borrar(1L);
    }

    // TESTS MÉTODOS EXTRAS

    @Test
    public void testGetHistorial() throws Exception {
        List<String> historialSimulado = List.of("RES-1001", "RES-1002");
        when(inquilinoService.mostrarHistorial(1L)).thenReturn(historialSimulado);
        mockMvc.perform(get("/api/v1/inquilinos/1/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("RES-1001"))
                .andExpect(jsonPath("$[1]").value("RES-1002"));
    }

    @Test
    public void testValidar() throws Exception {
        when(inquilinoService.validar(1L)).thenReturn(true);
        mockMvc.perform(get("/api/v1/inquilinos/1/validar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testBloquear() throws Exception {
        InquilinosResponseDTO inquilinoBloqueado = new InquilinosResponseDTO(
                1L,
                "Diego Silva", "diego.silva@mail.com",
                new ArrayList<>(Arrays.asList(
                "Reserva en Santiago Centro",
                "Reserva en Providencia")),
                true // bloqueado = true
        );
        when(inquilinoService.bloquear(1L)).thenReturn(inquilinoBloqueado);
        mockMvc.perform(put("/api/v1/inquilinos/1/bloquear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloqueado").value(true));
    }
}