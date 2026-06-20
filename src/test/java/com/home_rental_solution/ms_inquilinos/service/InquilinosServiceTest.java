package com.home_rental_solution.ms_inquilinos.service;

import com.home_rental_solution.ms_inquilinos.client.PropiedadClient;
import com.home_rental_solution.ms_inquilinos.client.ReservaClient;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class InquilinosServiceTest {

    @Autowired
    private InquilinoService inquilinoService;

    @MockitoBean
    private InquilinoRepository inquilinoRepository;

    @MockitoBean
    private PropiedadClient propiedadClient;

    @MockitoBean
    private ReservaClient reservaClient;

    // TESTS CRUD

    @Test
    public void testMostrarInquilinos() {
        Inquilino inquilino = new Inquilino(
                1L,
                "Lucas Silva",
                "lucas@mail.com",
                new ArrayList<>(List.of("Reserva 101")),
                false
        );
        when(inquilinoRepository.findAll()).thenReturn(List.of(inquilino));
        List<InquilinosResponseDTO> resultado = inquilinoService.mostrarInquilinos();
        assertNotNull(resultado);
        assertEquals(
                1,
                resultado.size()
        );
        assertEquals(
                "Lucas Silva",
                resultado.get(0).getNombre()
        );
    }

    @Test
    public void testMostrarPorId_Success() {
        Inquilino inquilino = new Inquilino(
                1L,
                "Lucas Silva",
                "lucas@mail.com",
                new ArrayList<>(),
                false
        );
        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(inquilino));
        InquilinosResponseDTO resultado = inquilinoService.mostrarPorId(1L);
        assertNotNull(resultado);
        assertEquals(
                "Lucas Silva",
                resultado.getNombre()
        );
        assertFalse(resultado.isBloqueado());
    }

    @Test
    public void testMostrarPorId_NotFound() {
        when(inquilinoRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inquilinoService.mostrarPorId(99L);
        });
        assertEquals(
                "El inquilino con ID: 99 no existe",
                exception.getMessage()
        );
    }

    @Test
    public void testSave_Success() {
        InquilinosRequestDTO request = new InquilinosRequestDTO(
                "Maria Paz",
                "maria@mail.com"
        );
        Inquilino inquilinoGuardado = new Inquilino(
                1L,
                request.getNombre(),
                request.getEmail(),
                new ArrayList<>(),
                false
        );
        when(inquilinoRepository.existsByEmailIgnoreCase("maria@mail.com")).thenReturn(false);
        when(inquilinoRepository.save(any(Inquilino.class))).thenReturn(inquilinoGuardado);
        InquilinosResponseDTO resultado = inquilinoService.save(request);
        assertNotNull(resultado);
        assertEquals(
                1L,
                resultado.getIdInquilino()
        );
        assertEquals(
                "Maria Paz",
                resultado.getNombre()
        );
    }

    @Test
    public void testSave_EmailYaExiste() {
        InquilinosRequestDTO request = new InquilinosRequestDTO(
                "Maria Paz",
                "maria@mail.com"
        );
        when(inquilinoRepository.existsByEmailIgnoreCase("maria@mail.com")).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inquilinoService.save(request);
        });
        assertEquals(
                "El email ya esta registrado",
                exception.getMessage()
        );
        verify(inquilinoRepository, never()).save(any());
    }

    @Test
    public void testEditar_Success() {
        Inquilino inquilinoExistente = new Inquilino(1L, "Viejo Nombre", "viejo@mail.com", new ArrayList<>(), false);
        InquilinosRequestDTO nuevosDatos = new InquilinosRequestDTO("Nuevo Nombre", "nuevo@mail.com");

        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(inquilinoExistente));
        when(inquilinoRepository.findByEmailIgnoreCase("nuevo@mail.com")).thenReturn(Optional.empty());
        when(inquilinoRepository.save(any(Inquilino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InquilinosResponseDTO resultado = inquilinoService.editar(1L, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals("nuevo@mail.com", resultado.getEmail());
    }

    @Test
    public void testEditar_EmailOcupadoPorOtroInquilino() {
        Inquilino inquilinoExistente = new Inquilino(
                1L,
                "Lucas",
                "lucas@mail.com",
                new ArrayList<>(), false
        );
        Inquilino otroInquilino = new Inquilino(
                2L,
                "Andres",
                "andres@mail.com",
                new ArrayList<>(), false
        );
        InquilinosRequestDTO nuevosDatos = new InquilinosRequestDTO(
                "Lucas",
                "andres@mail.com"
        );
        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(inquilinoExistente));
        when(inquilinoRepository.findByEmailIgnoreCase("andres@mail.com")).thenReturn(Optional.of(otroInquilino));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inquilinoService.editar(1L, nuevosDatos);
        });
        assertEquals(
                "El email ya esta registrado por otro usuario",
                exception.getMessage()
        );
    }

    @Test
    public void testEditar_MismoEmailDelInquilino() {
        Inquilino existente = new Inquilino(1L, "Lucas", "lucas@mail.com", new ArrayList<>(), false);
        InquilinosRequestDTO nuevosDatos = new InquilinosRequestDTO("Lucas Actualizado", "lucas@mail.com");
        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(inquilinoRepository.findByEmailIgnoreCase("lucas@mail.com")).thenReturn(Optional.of(existente));
        when(inquilinoRepository.save(any(Inquilino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InquilinosResponseDTO resultado = inquilinoService.editar(1L, nuevosDatos);

        assertEquals("Lucas Actualizado", resultado.getNombre());
    }

    @Test
    public void testEditar_InquilinoNoExiste() {
        InquilinosRequestDTO nuevosDatos = new InquilinosRequestDTO("Nombre", "correo@mail.com");
        when(inquilinoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inquilinoService.editar(99L, nuevosDatos));

        assertEquals("El inquilino con ID: 99 no existe", exception.getMessage());
    }

    @Test
    public void testBorrar_Success() {
        when(inquilinoRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> inquilinoService.borrar(1L));
        verify(inquilinoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBorrar_NotFound() {
        when(inquilinoRepository.existsById(99L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inquilinoService.borrar(99L);
        });
        assertEquals(
                "El inquilino con ID: 99 no existe",
                exception.getMessage()
        );
    }

    // TESTS EXTRAS

    @Test
    public void testMostrarHistorial() {
        List<String> historialFicticio = List.of(
                "Reserva #1",
                "Reserva #2"
        );
        Inquilino inquilino = new Inquilino(
                1L,
                "Lucas",
                "l@mail.com",
                historialFicticio,
                false
        );
        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(inquilino));
        List<String> resultado = inquilinoService.mostrarHistorial(1L);
        assertNotNull(resultado);
        assertEquals(
                2,
                resultado.size()
        );
        assertEquals(
                "Reserva #1",
                resultado.get(0)
        );
    }

    @Test
    public void testMostrarHistorial_InquilinoNoExiste() {
        when(inquilinoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inquilinoService.mostrarHistorial(99L));

        assertEquals("El inquilino con ID: 99 no existe", exception.getMessage());
    }

    @Test
    public void testValidar_InquilinoActivo() {
        when(inquilinoRepository.existsById(1L)).thenReturn(true);
        when(inquilinoRepository.existsByIdInquilinoAndBloqueadoFalse(1L)).thenReturn(true);
        boolean resultado = inquilinoService.validar(1L);
        assertTrue(resultado);
    }

    @Test
    public void testValidar_InquilinoNoExiste() {
        when(inquilinoRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inquilinoService.validar(99L));

        assertEquals("El inquilino con ID: 99 no existe", exception.getMessage());
    }

    @Test
    public void testBloquear() {
        Inquilino inquilinoInicial = new Inquilino(
                1L,
                "Lucas",
                "l@mail.com",
                new ArrayList<>(),
                false
        );
        when(inquilinoRepository.findById(1L)).thenReturn(Optional.of(inquilinoInicial));
        when(inquilinoRepository.save(any(Inquilino.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        // Al cambiar el estado de false, debería pasar a true (bloqueado)
        InquilinosResponseDTO resultado = inquilinoService.bloquear(1L);
        assertTrue(resultado.isBloqueado());
    }

    @Test
    public void testBloquear_InquilinoNoExiste() {
        when(inquilinoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inquilinoService.bloquear(99L));

        assertEquals("El inquilino con ID: 99 no existe", exception.getMessage());
    }
}
