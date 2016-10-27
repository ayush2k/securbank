package securbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author Ayush Gupta
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "securbank")
@EnableScheduling
public class SecurbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurbankApplication.class, args);
	}
}
