package com.jango.assignmenttracker;

import com.jango.assignmenttracker.model.Roles;
import com.jango.assignmenttracker.model.User;
import com.jango.assignmenttracker.repository.RolesRepository;
import com.jango.assignmenttracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@Slf4j
public class AssignmenttrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmenttrackerApplication.class, args);
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public CommandLineRunner demoData(RolesRepository rolesRepo, BCryptPasswordEncoder passwordEncoder, UserRepository userRepo) {

		return args -> {


			if (rolesRepo.findAll().isEmpty()){
				String email = "admin@assignmenttracker.com";
				String password = "password";
				Roles roles = new Roles(1, "ADMIN");
				Roles mRoles = rolesRepo.save(roles);
				log.info("Role Id:::::{}Role Name::::::{}",mRoles.getId(),mRoles.getName());
				Roles roles1 = new Roles(2, "USER");
				Roles mRoles1 =rolesRepo.save(roles1);
				log.info("Role Id:::::{}Role Name::::::{}",mRoles1.getId(),mRoles1.getName());

				if (Boolean.TRUE.equals(userRepo.existsByEmail(email))) {
					return;
				}

				List<Roles> rolesList = new ArrayList<>();
				Optional<Roles> role = rolesRepo.findById(1);
				rolesList.add(role.get());

				User user = new User(0L, email, passwordEncoder.encode(password), rolesList);
				userRepo.save(user);

			}

		};

	}


}
