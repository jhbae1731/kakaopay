package com.kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KakaopayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KakaopayApplication.class, args);
	}
}
