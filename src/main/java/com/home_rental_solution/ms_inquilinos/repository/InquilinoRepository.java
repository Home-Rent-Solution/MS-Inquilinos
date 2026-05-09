package com.home_rental_solution.ms_inquilinos.repository;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Repository
public class InquilinoRepository {

    private List<Inquilino> listaInquilinos = new ArrayList<>();
    private int contadorId = 4;

    public InquilinoRepository(){
        listaInquilinos.add(new Inquilino(
                1,
                "Pedro gonzalez",
                "pedro.gonzalez@email.com",
                new ArrayList<>(Arrays.asList(
                        "Reserva en Santiago Centro",
                        "Reserva en Providencia"
                ))
        ));listaInquilinos.add(new Inquilino(
                2,
                "Camila Rojas",
                "camila.rojas@email.com",
                new ArrayList<>(Arrays.asList(
                        "Reserva en Maipu"
                ))
        ));listaInquilinos.add(new Inquilino(
                3,
                "Matias Silva",
                "matias.silva@email.com",
                new ArrayList<>()
                ));
    }

    //CRUD
    //obtener todos los inquilinos
    public List<Inquilino> obtenerInquilinos(){
        return listaInquilinos;
    }

    //buscar por I
    public Inquilino obtenerPorId(int idInquilino){
        for (Inquilino inquilino : listaInquilinos){
            if (inquilino.getIdInquilino() == idInquilino){
                return inquilino;
            }
        }
        return null;
    }

    //guardar nuevo inquilino
    public Inquilino guardar(Inquilino inquilino){
        inquilino.setIdInquilino(contadorId++);
        listaInquilinos.add(inquilino);
        return inquilino;
    }

    //actualizar inquilino
    public Inquilino actualizar(Inquilino inquilino){
        for (int i = 0; i < listaInquilinos.size(); i++){
            if (listaInquilinos.get(i).getIdInquilino().equals(inquilino.getIdInquilino())){
                listaInquilinos.set(i, inquilino);
                return inquilino;
            }
        }
        return null;
    }

    //eliminar por ID
    public void eliminar(int idInquilino){
        Inquilino inquilino = obtenerPorId(idInquilino);
        if (inquilino != null){
            listaInquilinos.remove(inquilino);
        }
    }

    //***EXTRAS***
    //buscar por email
    public Inquilino obtenerPorEmail(String email){
        for (Inquilino inquilino : listaInquilinos){
            if (inquilino.getEmail().equalsIgnoreCase(email)){
                return inquilino;
            }
        }
        return null;
    }

    // obtener historial
    public List<String> obtenerHistorial(int idInquilino){
        Inquilino inquilino = obtenerPorId(idInquilino);
        if (inquilino != null){
            return inquilino.getHistorialReservas();
        }
        return null;
    }

    //validar si esta registrado
    public boolean estaRegistrado(int idInquilino){
        return obtenerPorId(idInquilino) != null;
    }
}
