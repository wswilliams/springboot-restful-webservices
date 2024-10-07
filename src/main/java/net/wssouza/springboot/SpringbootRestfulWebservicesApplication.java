package net.wssouza.springboot;


import java.util.ArrayList;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import net.wssouza.springboot.entity.Role;
import net.wssouza.springboot.entity.User;
import net.wssouza.springboot.service.UserService;

@SpringBootApplication
public class SpringbootRestfulWebservicesApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestfulWebservicesApplication.class, args); // Nome da classe atualizado

	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			if (!userService.existsByUsername("admin")) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword("admin");
				admin.setEmail("admin@email.com");
				admin.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_ADMIN)));
				userService.signup(admin);
			}

			if (!userService.existsByUsername("client")) {
				User client = new User();
				client.setUsername("client");
				client.setPassword("client");
				client.setEmail("client@email.com");
				client.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_CLIENT)));
				userService.signup(client);
			}
		} catch (Exception e) {
			System.err.println("Error during CommandLineRunner execution: " + e.getMessage());
			e.printStackTrace();
		}
	}
}