package com.home_rental_solution.ms_inquilinos.service;

import com.home_rental_solution.ms_inquilinos.client.PropiedadClient;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosRequestDTO;
import com.home_rental_solution.ms_inquilinos.dto.InquilinosResponseDTO;
import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquilinoService {

    private final InquilinoRepository inquilinoRepository;

    @Autowired
    private final PropiedadClient propiedadClient;

    //Validacion con feign
    private void validarPropiedad(int idPropiedad){
        try{
            propiedadClient.obtenerPropiedad(idPropiedad);
            log.info(">>> Propiedad {} validada correctamente (Feign Client)", idPropiedad);
        } catch (FeignException.NotFound e){
            throw new RuntimeException("La propiedad con ID: " + idPropiedad + " no existe");
        } catch (FeignException e){
            throw new RuntimeException("No se puede conectar con ms-propiedades");
        }
    }


    //Mapeo Entidad -> ResponseDTO
    private InquilinosResponseDTO mapToDTO(Inquilino inquilino){
        return new InquilinosResponseDTO(
                inquilino.getIdInquilino(),
                inquilino.getNombre(),
                inquilino.getEmail(),
                inquilino.getHistorialReservas(),
                inquilino.isBloqueado()
        );
    }

    //Mapeo RequestDTO -> Entidad
    private Inquilino mapToEntity(InquilinosRequestDTO dto){
        return new Inquilino(
                null,
                dto.getNombre(),
                dto.getEmail(),
                new ArrayList<>(),
                false
        );
    }

    //CRUD
    //GET /inquilinos
    public List<InquilinosResponseDTO> mostrarInquilinos(){
        return inquilinoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    //GET /inquilinos/id
    public InquilinosResponseDTO mostrarPorId(Long idInquilino){
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElseThrow(() -> new RuntimeException("El " +
                "inquilino con ID: " + idInquilino + " no existe"));
        return mapToDTO(inquilino);
    }

    //POST /inquilinos
    public InquilinosResponseDTO save(InquilinosRequestDTO dto){
        if (inquilinoRepository.existsByEmailIgnoreCase(dto.getEmail())){
            throw new RuntimeException("El email ya esta registrado");
        }
        Inquilino nuevoInquilino = mapToEntity(dto);
        return mapToDTO(inquilinoRepository.save(nuevoInquilino));
    }

    //PUT /inquilino/id
    public InquilinosResponseDTO editar(Long idInquilino, InquilinosRequestDTO dto){
        Inquilino inquilinoExistente = inquilinoRepository.findById(idInquilino).orElseThrow(() -> new RuntimeException(
                "El inquilino con ID: " + idInquilino + " no existe"));
        Optional<Inquilino> inquilinoEmail = inquilinoRepository.findByEmailIgnoreCase(dto.getEmail());
        if (inquilinoEmail.isPresent() && !inquilinoEmail.get().getIdInquilino().equals(idInquilino)){
            throw new RuntimeException("El email ya esta registrado por otro usuario");
        }
        inquilinoExistente.setNombre(dto.getNombre());
        inquilinoExistente.setEmail(dto.getEmail());
        return mapToDTO(inquilinoRepository.save(inquilinoExistente));
    }

    //DELETE /inquilinos/id
    public void borrar(Long idInquilino) {
        if (!inquilinoRepository.existsById(idInquilino)){
            throw new RuntimeException("El inquilino con ID: " + idInquilino + " no existe");
        }
        inquilinoRepository.deleteById(idInquilino);
    }

    //***EXTRAS***
    // GET /inquilinos/id/historial
    public List<String> mostrarHistorial(Long idInquilino){
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElseThrow(() -> new RuntimeException("El " +
                "inquilino con ID: " + idInquilino + " no existe"));
        return inquilino.getHistorialReservas();
    }

    //GET /inquilinos/id/validar
    public boolean validar(Long idInquilino){
        if (!inquilinoRepository.existsById(idInquilino)){
            throw new RuntimeException("El inquilino con ID: " + idInquilino + " no existe");
        }
        return inquilinoRepository.existsByIdInquilinoAndBloqueadoFalse(idInquilino);
    }

    //PUT /inquilinos/id/bloquear
    public InquilinosResponseDTO bloquear(Long idInquilino){
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElseThrow(() -> new RuntimeException("El" +
                " inquilino con ID: " + idInquilino + " no existe"));
        inquilino.setBloqueado(true);
        return mapToDTO(inquilinoRepository.save(inquilino));
    }
}
