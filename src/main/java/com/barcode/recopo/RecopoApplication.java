package com.barcode.recopo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RecopoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecopoApplication.class, args);
	}

}
