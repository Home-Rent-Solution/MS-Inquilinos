package com.home_rental_solution.ms_inquilinos.controller;

import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
import com.home_rental_solution.ms_inquilinos.assemblers.InquilinosModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/inquilinos")
@RequiredArgsConstructor
@Tag(
        name = "Inquilinos V2 (HATEOAS)",
        description = "Controlador evolutivo para la gestión de inquilinos incorporando el motor hipermedia HATEOAS"
)
public class InquilinosControllerV2 {

    private final InquilinoService inquilinoService;
    private final InquilinosModelAssembler assembler;

    //***CRUD***

    @GetMapping
    @Operation(
            summary = "Obtener todos los inquilinos (HATEOAS)",
            description = "Devuelve una colección hipermedia ordenada alfabéticamente con todos los inquilinos y" +
                    " sus acciones dinámicas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Colección de inquilinos recuperada con éxito"
    )
    public ResponseEntity<CollectionModel<EntityModel<InquilinosResponseDTO>>> getInquilinos(){
        List<EntityModel<InquilinosResponseDTO>> inquilinos = inquilinoService
                .mostrarInquilinos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(inquilinos));
    }

    @GetMapping("{idInquilino}")
    @Operation(
            summary = "Obtener un inquilino por ID (HATEOAS)",
            description = "Busca un perfil de inquilino y adjunta enlaces contextuales según su estado actual" +
                    " de bloqueo."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inquilino localizado e instanciado hipermedia correctamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "El ID del inquilino solicitado no existe",
                    content = @Content
            )
    })
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> getPorId(
            @Parameter(
                    description = "ID numérico del inquilino a buscar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){

        InquilinosResponseDTO dto = inquilinoService.mostrarPorId(idInquilino);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @PostMapping
    @Operation(
            summary = "Registrar un nuevo inquilino (HATEOAS)",
            description = "Crea un inquilino en el sistema y retorna sus datos junto con los primeros enlaces de" +
                    " acción permitidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Inquilino registrado de forma exitosa"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación o conflicto de email",
                    content = @Content
            )
    })
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> postInquilino(
            @Valid @RequestBody InquilinosRequestDTO dto){

        InquilinosResponseDTO nuevoInquilino = inquilinoService.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(nuevoInquilino));
    }

    @PutMapping("{idInquilino}")
    @Operation(
            summary = "Actualizar perfil de un inquilino (HATEOAS)",
            description = "Modifica datos básicos y recalcula los enlaces del recurso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos modificados y enlaces actualizados con éxito"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID inexistente, datos inválidos o conflicto de email",
                    content = @Content
            )
    })
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> putInquilino (
            @Parameter(
                    description = "ID del inquilino que se va a editar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino,
            @Valid @RequestBody InquilinosRequestDTO dto){

        InquilinosResponseDTO editado = inquilinoService.editar(idInquilino, dto);
        return ResponseEntity.ok(assembler.toModel(editado));
    }

    @DeleteMapping("{idInquilino}")
    @Operation(
            summary = "Eliminar un inquilino",
            description = "Remueve permanentemente el registro del sistema (Acción destructiva, no retorna enlaces)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Inquilino eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se encontró ningún inquilino con el ID especificado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteInquilino(
            @Parameter(
                    description = "ID del inquilino a dar de baja",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){
        inquilinoService.borrar(idInquilino);
        return ResponseEntity
                .noContent()
                .build();
    }

    //***EXTRAS****

    @GetMapping("{idInquilino}/historial")
    @Operation(summary = "Obtener historial de reservas", description = "Recupera los códigos planos de reserva.")
    public ResponseEntity<List<String>> getHistorial(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.mostrarHistorial(idInquilino));
    }

    @GetMapping("{idInquilino}/validar")
    @Operation(
            summary = "Validar si el inquilino puede arrendar",
            description = "Retorna un booleano puro de validación."
    )
    public ResponseEntity<Boolean> validar(@PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.validar(idInquilino));
    }

    @PutMapping("{idInquilino}/bloquear")
    @Operation(
            summary = "Bloquear/Desbloquear un inquilino (HATEOAS)",
            description = "Ejecuta la acción toggle en el servicio e invierte de inmediato la semántica de los" +
                    " enlaces hipermedia devueltos."
    )
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> bloquear(
            @Parameter(
                    description = "ID del inquilino a switchear estado",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){

        InquilinosResponseDTO alterado = inquilinoService.bloquear(idInquilino);
        return ResponseEntity.ok(assembler.toModel(alterado));
    }
}
