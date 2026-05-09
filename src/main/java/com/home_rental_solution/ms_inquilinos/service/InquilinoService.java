package com.home_rental_solution.ms_inquilinos.service;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquilinoService {

    @Autowired
    private InquilinoRepository inquilinoRepository;

    //CRUD
    //GET /inquilinos
    public List<Inquilino> mostrarInquilinos(){
        return inquilinoRepository.obtenerInquilinos();
    }

    //GET /inquilinos/id
    public Inquilino mostrarPorId(int idInquilino){
        return inquilinoRepository.obtenerPorId(idInquilino);
    }

    //POST /inquilinos
    public Inquilino save(Inquilino nuevoInquilino) throws Exception{
        if (inquilinoRepository.obtenerPorEmail(nuevoInquilino.getEmail()) != null){
            throw new Exception("El email ya esta registrado");
        }
        return inquilinoRepository.guardar(nuevoInquilino);
    }

    //PUT /inquilino/id
    public Inquilino editar(int idInquilino, Inquilino inquilinoEditado) throws Exception{
        Inquilino inquilinoExistente = inquilinoRepository.obtenerPorId(idInquilino);
        if (inquilinoExistente == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        Inquilino inquilinoEmail = inquilinoRepository.obtenerPorEmail(inquilinoEditado.getEmail());
        if (inquilinoEmail != null && !inquilinoEmail.getIdInquilino().equals(idInquilino)){
            throw new Exception("El email ya esta registrado por otro usuario");
        }
        inquilinoEditado.setIdInquilino(idInquilino);
        inquilinoEditado.setBloqueado(inquilinoExistente.isBloqueado());
        return inquilinoRepository.actualizar(inquilinoEditado);
    }

    //DELETE /inquilinos/id
    public void borrar(int idInquilino) throws Exception{
        if (inquilinoRepository.obtenerPorId(idInquilino) == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        inquilinoRepository.eliminar(idInquilino);
    }

    //***EXTRAS***
    // GET /inquilinos/id/historial
    public List<String> mostrarHistorial(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.obtenerPorId(idInquilino);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        return inquilinoRepository.obtenerHistorial(idInquilino);
    }

    //GET /inquilinos/id/validar
    public boolean validar(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.obtenerPorId(idInquilino);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        return !inquilino.isBloqueado();
    }

    //PUT /inquilinos/id/bloquear
    public Inquilino bloquear(int idInquilino) throws Exception{
        Inquilino inquilino = inquilinoRepository.obtenerPorId(idInquilino);
        if (inquilino == null){
            throw new Exception("El inquilino con ID: " + idInquilino + " no existe");
        }
        return inquilinoRepository.bloquear(idInquilino);
    }
}
