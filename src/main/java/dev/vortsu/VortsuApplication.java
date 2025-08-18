package dev.vortsu;

import dev.vortsu.initializers.Initializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VortsuApplication {


	public static void main(String[] args) {
		SpringApplication.run(VortsuApplication.class, args);

	}

}
