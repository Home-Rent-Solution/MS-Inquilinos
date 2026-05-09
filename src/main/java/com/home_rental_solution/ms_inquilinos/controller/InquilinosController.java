package com.home_rental_solution.ms_inquilinos.controller;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/inquilinos")
public class InquilinosController {

    @Autowired
    private InquilinoService inquilinoService;

    //***CRUD***
    //GET /inquilinos
    @GetMapping
    public ResponseEntity<List<Inquilino>> getInquilinos(){
        return ResponseEntity.ok(inquilinoService.mostrarInquilinos());
    }

    //GET /inquilinos/id
    @GetMapping("{idInquilino}")
    public ResponseEntity<?> getPorId(@PathVariable int idInquilino){
        Inquilino inquilino = inquilinoService.mostrarPorId(idInquilino);
        if (inquilino == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El inquilino con ID: " + idInquilino + " no existe");
        }
        return ResponseEntity.ok(inquilino);
    }

    //POST /inquilinos
    @PostMapping
    public ResponseEntity<?> postInquilino(@Valid @RequestBody Inquilino nuevoInquilino){
        try{
            Inquilino inquilinoGuardado = inquilinoService.save(nuevoInquilino);
            return ResponseEntity.status(HttpStatus.CREATED).body(inquilinoGuardado);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //PUT /inquilinos/id
    @PutMapping("{idInquilino}")
    public ResponseEntity<?> putInquilino (@PathVariable int idInquilino, @Valid @RequestBody Inquilino inquilinoEditado){
        try{
            Inquilino inquilinoActualizado = inquilinoService.editar(idInquilino, inquilinoEditado);
            return ResponseEntity.ok((inquilinoActualizado));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //DELETE /inquilinos/id
    @DeleteMapping("{idInquilino}")
    public ResponseEntity<?> deleteInquilino(@PathVariable int idInquilino){
        try{
            inquilinoService.borrar(idInquilino);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //***EXTRAS***
    //GET /inquilinos/id/historial
    @GetMapping("{idInquilino}/historial")
    public ResponseEntity<?> getHistorial(@PathVariable int idInquilino){
        try{
            return ResponseEntity.ok(inquilinoService.mostrarHistorial(idInquilino));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //GET /inquilinos/id/validar
    @GetMapping("{idInquilino}/validar")
    public ResponseEntity<?> validar(@PathVariable int idInquilino){
        try{
            boolean habilitado = inquilinoService.validar(idInquilino);
            return ResponseEntity.ok(habilitado);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //PUT /inquilinos/id/bloquear
    @PutMapping("{idInquilino}/bloquear")
    public ResponseEntity<?> bloquear(@PathVariable int idInquilino){
        try{
            Inquilino inquilino = inquilinoService.bloquear(idInquilino);
            return ResponseEntity.ok(inquilino);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Manejo de errores de validacion
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> manejarErroresValidacion(MethodArgumentNotValidException ex){
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return errores;
    }
}
