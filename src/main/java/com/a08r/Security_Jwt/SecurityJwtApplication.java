package com.a08r.Security_Jwt;

import com.a08r.Security_Jwt.security.UserServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.a08r.Security_Jwt.security.Role.ADMIN;
import static com.a08r.Security_Jwt.security.Role.MANAGER;

import  com.a08r.Security_Jwt.security.User;


@SpringBootApplication
public class SecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			UserServices service
	) {
		return args -> {
			var admin = User.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password123")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin));

			var manager = User.builder()
					.firstname("Manager")
					.lastname("Manager")
					.email("manager@mail.com")
					.password("password123")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager));

		};
	}

}
