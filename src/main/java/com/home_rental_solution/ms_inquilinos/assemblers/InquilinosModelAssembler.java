package com.home_rental_solution.ms_inquilinos.assemblers;

import com.home_rental_solution.ms_inquilinos.controller.InquilinosController;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InquilinosModelAssembler implements RepresentationModelAssembler<InquilinosResponseDTO, EntityModel<InquilinosResponseDTO>> {

    @Override
    public EntityModel<InquilinosResponseDTO> toModel(InquilinosResponseDTO inquilino) {

        EntityModel<InquilinosResponseDTO> model = EntityModel.of(inquilino);
        model.add(linkTo(methodOn(InquilinosController.class)
                .getPorId(inquilino.getIdInquilino()))
                .withSelfRel()
        );
        model.add(linkTo(methodOn(InquilinosController.class)
                .getHistorial(inquilino.getIdInquilino()))
                .withRel("historial")
        );

        if (inquilino.isBloqueado()) {
            model.add(linkTo(methodOn(InquilinosController.class)
                    .bloquear(inquilino.getIdInquilino()))
                    .withRel("desbloquear_inquilino")
            );
        } else {
            model.add(linkTo(methodOn(InquilinosController.class)
                    .validar(inquilino.getIdInquilino()))
                    .withRel("validar_aptitud")
            );
            model.add(linkTo(methodOn(InquilinosController.class)
                    .bloquear(inquilino.getIdInquilino()))
                    .withRel("bloquear_inquilino")
            );
        }

        return model;
    }
}
