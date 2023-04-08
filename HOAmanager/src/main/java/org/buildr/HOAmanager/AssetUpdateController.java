package org.buildr.HOAmanager;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AssetUpdateController {

    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    
    @GetMapping("/myEndpoint")
    public AssetTable myEndpoint(@RequestParam int id) {
        Map<String, Object> assets = jdbcTemplate.queryForList("SELECT * FROM assets WHERE asset_id = ?", id).get(0);
        AssetTable asset =  new AssetTable((int) assets.get("asset_id"), (String) assets.get("asset_name"), (String) assets.get("asset_description"),
                              (String) assets.get("type_asset"), (String) assets.get("status"), (java.sql.Date) assets.get("acquisition_date"), 
                              (Boolean) assets.get("forrent"), (BigDecimal) assets.get("asset_value"), (BigDecimal) assets.get("loc_lattitude"), 
                              (BigDecimal) assets.get("loc_longiture"), (String) assets.get("hoa_name"));

        return asset;
    }

    @PostMapping("/addAsset")
    public void addAsset(@RequestBody AssetTable asset) {
        
    }
}