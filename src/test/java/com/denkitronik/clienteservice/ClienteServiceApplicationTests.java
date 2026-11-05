package com.denkitronik.clienteservice;

import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.repositories.IClienteDao;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClienteServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void main_arrancaLaAplicacion() {
		// Se simula SpringApplication.run para no levantar un segundo servidor
		try (MockedStatic<SpringApplication> spring = mockStatic(SpringApplication.class)) {
			String[] args = {};
			ClienteServiceApplication.main(args);
			spring.verify(() -> SpringApplication.run(ClienteServiceApplication.class, args));
		}
	}

	@Test
	void regionesIniciales_sinRegiones_insertaLasRegionesBase() throws Exception {
		IClienteDao dao = mock(IClienteDao.class);
		JdbcTemplate jdbc = mock(JdbcTemplate.class);
		when(dao.findAllRegiones()).thenReturn(List.of());

		new ClienteServiceApplication().regionesIniciales(dao, jdbc).run(null);

		verify(jdbc, times(ClienteServiceApplication.REGIONES.size()))
				.update(eq("INSERT INTO regiones (nombre) VALUES (?)"), anyString());
	}

	@Test
	void regionesIniciales_conRegiones_noInsertaNada() throws Exception {
		IClienteDao dao = mock(IClienteDao.class);
		JdbcTemplate jdbc = mock(JdbcTemplate.class);
		when(dao.findAllRegiones()).thenReturn(List.of(new Region()));

		new ClienteServiceApplication().regionesIniciales(dao, jdbc).run(null);

		verifyNoInteractions(jdbc);
	}

}
