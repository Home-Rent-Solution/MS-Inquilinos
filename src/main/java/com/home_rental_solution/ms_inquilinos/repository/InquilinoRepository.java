package com.home_rental_solution.ms_inquilinos.repository;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquilinoRepository extends JpaRepository<Inquilino, Integer> {

    //***EXTRAS***
    //buscar por email
    @Query("SELECT i FROM Inquilino i WHERE LOWER(i.email) = LOWER(:email)")
    Optional<Inquilino> findByEmailIgnoreCase(@Param("email") String email);

    //verificar si el email ya existe
    boolean existsByEmailIgnoreCase(String email);

    //validar si esta puede arrendar
    boolean existsByIdInquilinoAndBloqueadoFalse(Integer idInquilino);

    //Listar por estado de bloqueo
    @Query("SELECT i FROM Inquilino i WHERE i.bloqueado = false ORDER BY i.nombre")
    List<Inquilino> findByBloqueadoFalse();

    @Query("SELECT i FROM Inquilino i WHERE i.bloqueado = true ORDER BY i.nombre")
    List<Inquilino> findByBloqueadoTrue();
}