package com.home_rental_solution.ms_inquilinos.repository;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquilinoRepository extends JpaRepository<Inquilino, Integer> {

    //***EXTRAS***
    //buscar por email
    Inquilino findByEmailIgnoreCase(String email);

    //validar si esta puede arrendar
    boolean existsByIdInquilinoAndBloqueadoFalse(Integer idInquilino);

}