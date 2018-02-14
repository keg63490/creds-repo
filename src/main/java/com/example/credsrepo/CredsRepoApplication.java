package com.example.credsrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example"})
@EntityScan("com.example")
public class CredsRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CredsRepoApplication.class, args);
	}
}
