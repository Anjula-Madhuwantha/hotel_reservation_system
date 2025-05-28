package lk.anjula.hotelreservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true,prePostEnabled = true,securedEnabled = true)
public class HotelreservationsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelreservationsystemApplication.class, args);
	}

}
