package com.home_rental_solution.ms_inquilinos.config;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{

    private final InquilinoRepository inquilinoRepository;

    @Override
    public void run(String...args){
        if (inquilinoRepository.count() > 0){
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial");
            return;
        }
        log.info(">>> Datainitializer: BD vacia detectado, insertando inquilinos de prueba");
        inquilinoRepository.save(new Inquilino(
                null,
                "Pedro Gonzalex",
                "pedro.gonzalez@email.com",
                new ArrayList<>(Arrays.asList(
                        "Reserva en Santiago Centro",
                        "Reserva en Providencia"
                )),
                false
        ));
        inquilinoRepository.save(new Inquilino(
                null,
                "Camila Rojas",
                "camila.rojas@email.com",
                new ArrayList<>(Arrays.asList(
                        "Reserva en Maipu"
                )),
                false
        ));

        inquilinoRepository.save(new Inquilino(
                null,
                "Matias Silva",
                "matias.silva@email.com",
                new ArrayList<>(),
                true
        ));
        log.info("DataInitializer: {} inquilinos insertados correctamente", inquilinoRepository.count());
    }
}
