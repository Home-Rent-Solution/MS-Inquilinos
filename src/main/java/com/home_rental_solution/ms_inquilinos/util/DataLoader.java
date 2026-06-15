package com.home_rental_solution.ms_inquilinos.util;

import com.home_rental_solution.ms_inquilinos.model.Inquilino;
import com.home_rental_solution.ms_inquilinos.repository.InquilinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Profile({"dev", "test"})
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private final InquilinoRepository inquilinoRepository;

    @Override
    public void run(String... args) throws Exception{

        Faker faker = new Faker();
        Random random = new Random();

        //generamos 30 inquilinos
        for (int i = 0; i < 30; i++){
            Inquilino inquilino = new Inquilino();
            inquilino.setNombre(faker.name().fullName());
            inquilino.setEmail(faker.internet().emailAddress());
            inquilino.setBloqueado(faker.bool().bool());
            inquilino.setHistorialReservas(List.of(
                    "RES- " + faker.random().hex(5),
                    "RES- " + faker.random().hex(5)
            ));
            inquilinoRepository.save(inquilino);
        }
    log.info(">> ms-inquilinos: ¡Base de datos poblada con DataFaker!");
    }
}
