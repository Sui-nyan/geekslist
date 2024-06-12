package hdm.stuttgart.geekslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GeeksListApplication {

	GeeksListApplication() {
		// Empty constructor required for partial context when testing
	}

	public static void main(String[] args) {
		SpringApplication.run(GeeksListApplication.class, args);
	}
}
