package org.buildr.HOAmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HoAmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoAmanagerApplication.class, args);
	}

	@GetMapping("/")
    public String hello() {
        return "Hi, Sir Oli!";
    }
}
