package com.reservation.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class StoreReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreReservationServiceApplication.class, args);
	}

}
