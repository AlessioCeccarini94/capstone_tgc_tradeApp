package alessioceccarini.tgcapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TgcappApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgcappApplication.class, args);
	}

}
