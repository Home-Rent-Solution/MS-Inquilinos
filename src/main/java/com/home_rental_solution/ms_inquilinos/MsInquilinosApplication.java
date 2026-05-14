package com.home_rental_solution.ms_inquilinos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsInquilinosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsInquilinosApplication.class, args);
	}

}
