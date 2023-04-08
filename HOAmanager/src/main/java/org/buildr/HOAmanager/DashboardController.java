package org.buildr.HOAmanager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
	private JdbcTemplate jdbcTemplate;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        List<DashboardRow> rows = new ArrayList<>();
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList("SELECT * FROM assets;");
        for (int i = 0; i < resultList.size(); i++){
            Map<String, Object> currentRow = resultList.get(i);
            rows.add(new DashboardRow((int) currentRow.get("asset_id"), 
                    (String) currentRow.get("asset_name"), 
                    (String) currentRow.get("asset_description"), 
                    (String) currentRow.get("type_asset"), 
                    (String) currentRow.get("status")));
        }
        model.addAttribute("rows", rows);
        return "dashboard";
    }
}
