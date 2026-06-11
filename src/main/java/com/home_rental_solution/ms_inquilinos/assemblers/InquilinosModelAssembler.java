package com.home_rental_solution.ms_inquilinos.assemblers;

import com.home_rental_solution.ms_inquilinos.controller.InquilinosController;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InquilinosModelAssembler implements RepresentationModelAssembler<InquilinosResponseDTO,
        EntityModel<InquilinosResponseDTO>> {

    @Override
    public EntityModel<InquilinosResponseDTO> toModel (InquilinosResponseDTO dto){

        Long id = dto.getIdInquilino();
        return EntityModel.of(
                dto,
                linkTo(methodOn(InquilinosController
                        .class)
                        .getPorId(id))
                        .withSelfRel(),
                linkTo(methodOn(InquilinosController
                        .class)
                        .getInquilinos())
                        .withRel("inquilinos"),
                linkTo(methodOn(InquilinosController
                        .class)
                        .getHistorial(id))
                        .withRel("historial-reservas"),
                linkTo(methodOn(InquilinosController
                        .class)
                        .bloquear(id))
                        .withRel("bloquear-inquilino")
        );
    }
}
