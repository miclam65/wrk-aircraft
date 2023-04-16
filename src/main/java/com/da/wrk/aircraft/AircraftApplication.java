package com.da.wrk.aircraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class AircraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(AircraftApplication.class, args);
	}
}
