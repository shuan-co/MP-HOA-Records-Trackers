package org.buildr.HOAmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HoAmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoAmanagerApplication.class, args);
	}

	@GetMapping("/index")
    public String hello() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.queryForList("SELECT * FROM hoa");
        return "Hi, Sir Oli!";
    }
}
