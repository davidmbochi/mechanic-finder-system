package com.mechanicfinder.mechanicfindersystem;

import com.mechanicfinder.mechanicfindersystem.model.*;
import com.mechanicfinder.mechanicfindersystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class MechanicFinderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicFinderSystemApplication.class, args);
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner commandLineRunner(TaskRepository taskRepository,
											   MechanicRepository mechanicRepository,
											   CustomerRepository customerRepository,
											   AppUserRepository appUserRepository,
											   RoleRepository roleRepository,
											   BCryptPasswordEncoder bCryptPasswordEncoder){
		return args -> {
		List<Mechanic> mechanics = List.of(
				new Mechanic("john",
						"doe",
						"john@gmail.com",
						Availability.AVAILABLE,
						"Nairobi",
						"0724567345",
						ApplicationStatus.PENDING,
						LocalDate.now()),
				new Mechanic("peter",
						"parker",
						"peter@gmail.com",
						Availability.AVAILABLE,
						"kiambu",
						"0774763565",
						ApplicationStatus.PENDING,
						LocalDate.now()),
				new Mechanic("alexander",
						"johns",
						"alexander@gmail.com",
						Availability.AVAILABLE,
						"Eldoret",
						"0734127865",
						ApplicationStatus.PENDING,
						LocalDate.now().minusDays(1)),
				new Mechanic("harry",
						"potter",
						"harry@gmail.com",
						Availability.AVAILABLE,
						"Kisumu",
						"0734129084",
						ApplicationStatus.PENDING,
						LocalDate.now().minusDays(1))
		);

		mechanicRepository.saveAll(mechanics);

		List<Task> mechanicTasks = List.of(
				new Task("wheel repair",
						"Repair of punctured & worn out wheels",
						new BigDecimal("346.89")),
				new Task("Wind screen repair",
						"Repair of broken wind screens",
						new BigDecimal("600.89")),
				new Task("Oil change",
						"Changing used engine oil",
						new BigDecimal("450.33")),
				new Task("Battery charge",
						"charging of battery for head lights",
						new BigDecimal("300"))
		);

		taskRepository.saveAll(mechanicTasks);

		Task oil_change = taskRepository.findTaskByTaskName("Oil change");

		Mechanic john = mechanicRepository.findMechanicByFirstName("john");

		john.getTasks().add(oil_change);

		mechanicRepository.save(john);

		List<Role> roles = List.of(
				new Role("ROLE_MECHANIC"),
				new Role("ROLE_CUSTOMER"),
				new Role("ROLE_ADMIN")
		);

		roleRepository.saveAll(roles);

		appUserRepository.save(
				new AppUser("harryson",bCryptPasswordEncoder.encode("harryson")));

		AppUser harryson = appUserRepository.findAppUserByUsername("harryson");

		Role role_admin = roleRepository.findRoleByRoleName("ROLE_ADMIN");

		harryson.setRoles(List.of(role_admin));

		appUserRepository.save(harryson);


		};
	}

}
