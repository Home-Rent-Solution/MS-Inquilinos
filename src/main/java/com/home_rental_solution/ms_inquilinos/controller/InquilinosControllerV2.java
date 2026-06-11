package com.home_rental_solution.ms_inquilinos.controller;

import com.home_rental_solution.ms_inquilinos.assemblers.InquilinosModelAssembler;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.service.InquilinoService;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/inquilinos")
@RequiredArgsConstructor
@Tag(
        name = "Inquilinos",
        description = "Controlador principal para la gestión de usuarios inquilinos, historiales de reserva y" +
                " validaciones de estado"
)
public class InquilinosControllerV2 {

    private final InquilinoService inquilinoService;
    private final InquilinosModelAssembler assembler;

    //***CRUD***
    //GET /inquilinos
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Obtener todos los inquilinos",
            description = "Devuelve una lista ordenada alfabéticamente con todos los inquilinos registrados en la" +
                    " plataforma."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de inquilinos recuperada con éxito",
            content = @Content(
                    mediaType = "application/hal + json",
                    array = @ArraySchema(schema = @Schema(implementation = InquilinosResponseDTO.class))
            )
    )
    public CollectionModel<EntityModel<InquilinosResponseDTO>> getInquilinos(){
        List<EntityModel<InquilinosResponseDTO>> inquilinos = inquilinoService
                .mostrarInquilinos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(
                inquilinos,
                linkTo(methodOn(InquilinosControllerV2
                        .class)
                        .getInquilinos())
                        .withSelfRel()
        );
    }

    //GET /inquilinos/id
    @GetMapping(
            value = "{idInquilino}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Obtener un inquilino por ID",
            description = "Busca y devuelve el perfil completo de un inquilino a través de su identificador único."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Inquilino localizado correctamente",
                            content = @Content(
                                    mediaType = "application/hal + json",
                                    schema = @Schema(implementation = InquilinosResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El ID del inquilino solicitado no existe",
                            content = @Content
                    )
            }
    )
    public EntityModel<InquilinosResponseDTO> getPorId(
            @Parameter(
                    description = "ID numérico del inquilino a buscar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){
        InquilinosResponseDTO dto = inquilinoService.mostrarPorId(idInquilino);
        return assembler.toModel(dto);
    }

    //POST /inquilinos
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(
            summary = "Registrar un nuevo inquilino",
            description = "Crea un inquilino en el sistema de forma limpia. Valida la estructura del JSON y que el" +
                    " correo sea único."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Inquilino registrado de forma exitosa",
                            content = @Content(
                                    mediaType = "application/hal + json",
                                    schema = @Schema(implementation = InquilinosResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validación en los datos de entrada o el correo ya se encuentra" +
                                    " registrado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> postInquilino(@io
            .swagger
            .v3
            .oas
            .annotations
            .parameters
            .RequestBody(
                    description = "Datos estructurados requeridos para dar de alta al inquilino",
                    required = true
            ) @Valid @RequestBody InquilinosRequestDTO dto){
        InquilinosResponseDTO nuevoDto = inquilinoService.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(nuevoDto));
    }

    //PUT /inquilinos/id
    @PutMapping(
            value = "{idInquilino}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Actualizar perfil de un inquilino",
            description = "Permite modificar el nombre o correo electrónico de un inquilino. Los campos sensibles" +
                    " de historial y bloqueo no se alteran por esta vía."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Datos del inquilino modificados con éxito",
                            content = @Content(
                                    mediaType = "application/hal + json",
                                    schema = @Schema(implementation = InquilinosResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID inexistente, datos inválidos o conflicto de email duplicado",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> putInquilino (
            @Parameter(
                    description = "ID del inquilino que se va a editar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino,
            @io
                    .swagger
                    .v3
                    .oas
                    .annotations
                    .parameters
                    .RequestBody(
                    description = "Esquema con los nuevos datos actualizados del inquilino",
                    required = true
            )
            @Valid @RequestBody InquilinosRequestDTO dto){
        InquilinosResponseDTO editadoDto = inquilinoService.editar(
                idInquilino,
                dto
        );
        return ResponseEntity.ok(assembler.toModel(editadoDto));
    }

    //DELETE /inquilinos/id
    @DeleteMapping(
            value = "{idInquilino}",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Eliminar un inquilino",
            description = "Remueve permanentemente el registro de un inquilino del sistema utilizando su" +
                    " identificador único."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Inquilino eliminado exitosamente (Sin contenido de retorno)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No se encontró ningún inquilino con el ID especificado",
                            content = @Content
                    )
            }
    )
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
    //GET /inquilinos/id/historial
    // Nota: Retorna una lista cruda de Strings sin DTO estructurado, se mantiene JSON nativo.
    @GetMapping(
            value = "{idInquilino}/historial",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Obtener historial de reservas",
            description = "Recupera una lista con los códigos o identificadores de texto de las reservas que el" +
                    " inquilino ha realizado."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Historial de reservas recuperado correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El ID proporcionado no existe en el sistema",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<List<String>> getHistorial(
            @Parameter(
                    description = "ID del inquilino para consultar sus reservas",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.mostrarHistorial(idInquilino));
    }

    //GET /inquilinos/id/validar
    // Nota: Consumido internamente / Feign Client. Retorna un booleano directo en JSON nativo.
    @GetMapping(
            value = "{idInquilino}/validar",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Validar si el inquilino puede arrendar",
            description = "Comprueba el estado de seguridad del usuario. Devuelve true únicamente si existe y no se" +
                    " encuentra bloqueado."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación completada. Devuelve un booleano con la aptitud de renta",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El ID de inquilino enviado no es válido",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Boolean> validar(
            @Parameter(
                    description = "ID del inquilino a evaluar",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){
        return ResponseEntity.ok(inquilinoService.validar(idInquilino));
    }

    //PUT /inquilinos/id/bloquear
    @PutMapping(
            value = "{idInquilino}/bloquear",
            produces = MediaTypes.HAL_JSON_VALUE
    )
    @Operation(
            summary = "Bloquear un inquilino",
            description = "Cambia el estado de seguridad del inquilino (bloqueado = true), impidiéndole generar nueva" +
                    " reservas en la plataforma."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inquilino bloqueado de manera exitosa",
                            content = @Content(
                                    mediaType = "application/hal + json",
                                    schema = @Schema(implementation = InquilinosResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No se pudo realizar la acción porque el ID no existe",
                            content = @Content
                    )
            })
    public ResponseEntity<EntityModel<InquilinosResponseDTO>> bloquear(
            @Parameter(
                    description = "ID del inquilino al que se le aplicará la restricción",
                    example = "1",
                    required = true
            )
            @PathVariable Long idInquilino){
        InquilinosResponseDTO bloqueadoDto = inquilinoService.bloquear(idInquilino);
        return ResponseEntity.ok(assembler.toModel(bloqueadoDto));
    }
}