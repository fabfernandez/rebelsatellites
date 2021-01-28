package com.faba.rebelsatellites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.faba.rebelsatellites.service")
public class FuegoDeQuasarApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuegoDeQuasarApplication.class, args);
	}

}
