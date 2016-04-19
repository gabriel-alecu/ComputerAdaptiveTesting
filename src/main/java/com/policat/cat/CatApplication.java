package com.policat.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={SocialWebAutoConfiguration.class})
public class CatApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatApplication.class, args);
	}
}
