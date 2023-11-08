package io.maxafrica.gpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableCaching
public class GpServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GpServerApplication.class, args);
	}
}
