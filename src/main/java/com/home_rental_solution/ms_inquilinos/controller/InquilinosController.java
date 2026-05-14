package com.home_rental_solution.ms_inquilinos.controller;

import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquilinos")
@RequiredArgsConstructor
public class InquilinosController {

    private final InquilinoService inquilinoService;

    //***CRUD***
    //GET /inquilinos
    @GetMapping
    public ResponseEntity<List<InquilinosResponseDTO>> getInquilinos(){
        return ResponseEntity.ok(inquilinoService.mostrarInquilinos());
    }

    //GET /inquilinos/id
    @GetMapping("{idInquilino}")
    public ResponseEntity<InquilinosResponseDTO> getPorId(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.mostrarPorId(idInquilino));
    }

    //POST /inquilinos
    @PostMapping
    public ResponseEntity<InquilinosResponseDTO> postInquilino(@Valid @RequestBody InquilinosRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(inquilinoService.save(dto));
        }

    //PUT /inquilinos/id
    @PutMapping("{idInquilino}")
    public ResponseEntity<InquilinosResponseDTO> putInquilino (@PathVariable Long idInquilino, @Valid @RequestBody
    InquilinosRequestDTO dto){
        return ResponseEntity.ok(inquilinoService.editar(idInquilino, dto));
        }

    //DELETE /inquilinos/id
    @DeleteMapping("{idInquilino}")
    public ResponseEntity<Void> deleteInquilino(@PathVariable Long idInquilino){
        inquilinoService.borrar(idInquilino);
        return ResponseEntity.noContent().build();
    }

    //***EXTRAS***
    //GET /inquilinos/id/historial
    @GetMapping("{idInquilino}/historial")
    public ResponseEntity<List<String>> getHistorial(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.mostrarHistorial(idInquilino));
        }

    //GET /inquilinos/id/validar
    @GetMapping("{idInquilino}/validar")
    public ResponseEntity<Boolean> validar(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.validar(idInquilino));
        }

    //PUT /inquilinos/id/bloquear
    @PutMapping("{idInquilino}/bloquear")
    public ResponseEntity<InquilinosResponseDTO> bloquear(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.bloquear(idInquilino));
        }
    }