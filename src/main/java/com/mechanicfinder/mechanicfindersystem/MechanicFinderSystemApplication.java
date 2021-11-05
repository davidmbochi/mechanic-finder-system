package com.mechanicfinder.mechanicfindersystem;

import com.mechanicfinder.mechanicfindersystem.model.Availability;
import com.mechanicfinder.mechanicfindersystem.model.Mechanic;
import com.mechanicfinder.mechanicfindersystem.model.Task;
import com.mechanicfinder.mechanicfindersystem.repository.MechanicRepository;
import com.mechanicfinder.mechanicfindersystem.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class MechanicFinderSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MechanicFinderSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(TaskRepository taskRepository,
											   MechanicRepository mechanicRepository){
		return args -> {
		List<Mechanic> mechanics = List.of(
				new Mechanic("john",
						"doe",
						"john@gmail.com",
						Availability.AVAILABLE,
						"Nairobi"),
				new Mechanic("peter",
						"parker",
						"peter@gmail.com",
						Availability.NOT_AVAILABLE,"" +
						"kiambu"),
				new Mechanic("mary",
						"public",
						"mary@gmail.com",
						Availability.AVAILABLE,
						"Eldoret"),
				new Mechanic("alexander",
						"johns",
						"alexander@gmail.com",
						Availability.AVAILABLE,
						"Eldoret"),
				new Mechanic("harry",
						"potter",
						"harry@gmail.com",
						Availability.NOT_AVAILABLE,
						"Kisumu")
		);

		mechanicRepository.saveAll(mechanics);

		List<Task> mechanicTasks = List.of(
				new Task("wheel repair",
						"Repair of punctured & worn out wheels",
						new BigDecimal("346.89"),
						Duration.ofHours(3)),
				new Task("Wind screen repair",
						"Repair of broken wind screens",
						new BigDecimal("600.89"),
						Duration.ofHours(4)),
				new Task("Oil change",
						"Changing used engine oil",
						new BigDecimal("450.33"),
						Duration.ofHours(2)),
				new Task("Battery charge",
						"charging of battery for head lights",
						new BigDecimal("300"),
						Duration.ofHours(5))
		);

		taskRepository.saveAll(mechanicTasks);

		Task oil_change = taskRepository.findTaskByTaskName("Oil change");

		Mechanic john = mechanicRepository.findMechanicByFirstName("john");

		if (oil_change.equals(null) || john.equals(null)){
			throw new RuntimeException("either task or service is null");
		}else {
			john.getTasks().add(oil_change);
		}

		mechanicRepository.save(john);

		};
	}

}
