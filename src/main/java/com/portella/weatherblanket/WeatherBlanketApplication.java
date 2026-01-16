package com.portella.weatherblanket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WeatherBlanketApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeatherBlanketApplication.class, args);
	}
}
