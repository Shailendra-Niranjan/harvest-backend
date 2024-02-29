package com.harvestbackend;

import com.harvestbackend.model.ERole;
import com.harvestbackend.model.Role;
import com.harvestbackend.model.User;
import com.harvestbackend.repository.RoleRepository;
import com.harvestbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HarvestBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarvestBackendApplication.class, args);
	}



	@Bean
	CommandLineRunner run(RoleRepository roleRepository , UserRepository userRepository , PasswordEncoder passwordEncoder){

		return args -> {
			Role adminRole = null;
			if(roleRepository.findByName(ERole.ROLE_ADMIN).isPresent())return;
			adminRole = roleRepository.save(new Role(1 , ERole.ROLE_ADMIN));
			roleRepository.save(new Role(2 , ERole.ROLE_USER));
			roleRepository.save(new Role(3, ERole.ROLE_FARMER));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);
			User admin = new User("admin771@gmail.com" ,"admin", passwordEncoder.encode("admin"),roles);
			userRepository.save(admin);

		};



	}
}
