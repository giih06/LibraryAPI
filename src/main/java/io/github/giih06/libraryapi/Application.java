package io.github.giih06.libraryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Habbilita anotações de auditoria na aplicação
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
