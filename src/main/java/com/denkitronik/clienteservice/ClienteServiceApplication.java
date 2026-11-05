package com.denkitronik.clienteservice;

import com.denkitronik.clienteservice.domain.repositories.IClienteDao;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
public class ClienteServiceApplication {

	// Las mismas regiones de import.sql, en el mismo orden (ids 1..8)
	static final List<String> REGIONES = List.of(
			"Suramerica", "Centroamerica", "Norteamerica", "Europa",
			"Asia", "Africa", "Oceania", "Antartida");

	public static void main(String[] args) {
		SpringApplication.run(ClienteServiceApplication.class, args);
	}

	/**
	 * Si al arrancar no hay regiones (p. ej. el entorno de pruebas no cargó
	 * import.sql), inserta las regiones base para que un POST con
	 * region {"id": 1} no falle por la clave foránea. Con import.sql cargado
	 * no hace nada.
	 */
	@Bean
	ApplicationRunner regionesIniciales(IClienteDao clienteDao, JdbcTemplate jdbc) {
		return args -> {
			if (clienteDao.findAllRegiones().isEmpty()) {
				REGIONES.forEach(nombre ->
						jdbc.update("INSERT INTO regiones (nombre) VALUES (?)", nombre));
			}
		};
	}

}
