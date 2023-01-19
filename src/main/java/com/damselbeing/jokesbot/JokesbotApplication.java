package com.damselbeing.jokesbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JokesbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokesbotApplication.class, args);}

}
