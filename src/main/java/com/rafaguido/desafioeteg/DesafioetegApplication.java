package com.rafaguido.desafioeteg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DesafioetegApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DesafioetegApplication.class, args);
	}

}