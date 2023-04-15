package org.buildr.HOAmanager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AssetActivityController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/assetActivity")
    public String getAssetActivities(Model model, @RequestParam int id) {
        List<AssetActivityRow> rows = new ArrayList();
        var query = jdbcTemplate.queryForList("SELECT * FROM asset_activity aa " +
                "\nJOIN asset_transactions ast ON ast.asset_id = aa.asset_id AND ast.transaction_date = aa.activity_date" +
                "\nWHERE aa.asset_id = ? AND ast.isdeleted = 0", id);

        for(var row : query) {
            var activityRow = new AssetActivityRow(
                    (int)row.get("asset_id"),
                    (java.sql.Date)row.get("activity_date"),
                    (String)row.get("activity_description"),
                    (BigDecimal)row.get("cost"),
                    (String)row.get("status")
            );
            rows.add(activityRow);
        }

        model.addAttribute("asset_id", id);
        model.addAttribute("rows", rows);
        return "assetActivity";
    }
}
