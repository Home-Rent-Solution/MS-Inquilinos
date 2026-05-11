package com.home_rental_solution.ms_inquilinos.service;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquilinoService {

    private final InquilinoRepository inquilinoRepository;

    //CRUD
    //GET /inquilinos
    public List<Inquilino> mostrarInquilinos(){
        return inquilinoRepository.findAll();
    }

    //GET /inquilinos/id
    public Inquilino mostrarPorId(int idInquilino){
        return inquilinoRepository.findById(idInquilino).orElse(null);
    }

    //POST /inquilinos
    public Inquilino save(Inquilino nuevoInquilino) throws Exception{
        if (inquilinoRepository.findByEmailIgnoreCase(nuevoInquilino.getEmail()) != null){
            throw new Exception("El email ya esta registrado");
        }
        nuevoInquilino.setBloqueado(false);
        return inquilinoRepository.save(nuevoInquilino);
    }

    //PUT /inquilino/id
    public Inquilino editar(int idInquilino, Inquilino inquilinoEditado) throws Exception{
        Inquilino inquilinoExistente = inquilinoRepository.findById(idInquilino).orElse(null);
        if (inquilinoExistente == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        Inquilino inquilinoEmail = inquilinoRepository.findByEmailIgnoreCase(inquilinoEditado.getEmail());
        if (inquilinoEmail != null && !inquilinoEmail.getIdInquilino().equals(idInquilino)){
            throw new Exception("El email ya esta registrado por otro usuario");
        }
        inquilinoEditado.setIdInquilino(idInquilino);
        inquilinoEditado.setBloqueado(inquilinoExistente.isBloqueado());
        return inquilinoRepository.save(inquilinoEditado);
    }

    //DELETE /inquilinos/id
    public void borrar(int idInquilino) throws Exception{
        if (!inquilinoRepository.existsById(idInquilino)){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        inquilinoRepository.deleteById(idInquilino);
    }

    //***EXTRAS***
    // GET /inquilinos/id/historial
    public List<String> mostrarHistorial(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElse(null);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        return inquilino.getHistorialReservas();
    }

    //GET /inquilinos/id/validar
    public boolean validar(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElse(null);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        return inquilinoRepository.existsByIdInquilinoAndBloqueadoFalse(idInquilino);
    }

    //PUT /inquilinos/id/bloquear
    public Inquilino bloquear(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.findById(idInquilino).orElse(null);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        inquilino.setBloqueado(true);
        return inquilinoRepository.save(inquilino);
    }
}
