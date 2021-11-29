package com.thikthak.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ThikthakAdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThikthakAdmApplication.class, args);


		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "tomal";
		String encodedPassword = encoder.encode(rawPassword);
		System.out.println(encodedPassword);

		// create system admin user

	}

}
