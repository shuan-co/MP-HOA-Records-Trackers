package org.buildr.HOAmanager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HoAmanagerApplication{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(HoAmanagerApplication.class, args);
	}

	@GetMapping("/index")
    public Map<String, Object> hello() {
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList("SELECT * FROM assets;");
    	Map<String, Object> firstRow = resultList.get(0);
        return firstRow;
    }
}
