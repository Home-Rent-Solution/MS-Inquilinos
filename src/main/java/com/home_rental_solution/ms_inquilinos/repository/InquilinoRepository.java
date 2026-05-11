package com.home_rental_solution.ms_inquilinos.repository;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquilinoRepository extends JpaRepository<Inquilino, Integer> {

    //***EXTRAS***
    //buscar por email
    Optional<Inquilino> findByEmailIgnoreCase(String email);

    //verificar si el email ya existe
    boolean existsByEmailIgnoreCase(String email);

    //validar si esta puede arrendar
    boolean existsByIdInquilinoAndBloqueadoFalse(Integer idInquilino);

    //Listar por estado de bloqueo
    List<Inquilino> findByBloqueadoFalse();
    List<Inquilino> findByBloqueadoTrue();

}