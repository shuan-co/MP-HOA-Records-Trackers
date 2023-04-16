package org.buildr.HOAmanager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.script.*;


@RestController
public class AssetUpdateController {

    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    
    @GetMapping("/myEndpoint")
    public AssetTable myEndpoint(@RequestParam int id) {
        Map<String, Object> assets = jdbcTemplate.queryForList("SELECT * FROM assets WHERE asset_id = ?", id).get(0);
        int enclosingAsset = assets.get("enclosing_asset") != null ? (int) assets.get("enclosing_asset") : 0;
        String enclosingAssetName = "None";

        if (enclosingAsset != 0){
            enclosingAssetName = (String) jdbcTemplate.queryForList("SELECT asset_name FROM assets WHERE asset_id = ?", enclosingAsset).get(0).get("asset_name");
        }

        AssetTable asset =  new AssetTable((int) assets.get("asset_id"), (String) assets.get("asset_name"), (String) assets.get("asset_description"),
                              (String) assets.get("type_asset"), (String) assets.get("status"), (java.sql.Date) assets.get("acquisition_date"),
                              (Boolean) assets.get("forrent"), (BigDecimal) assets.get("asset_value"), (BigDecimal) assets.get("loc_lattitude"),
                              (BigDecimal) assets.get("loc_longiture"), (String) assets.get("hoa_name"), enclosingAssetName);
        ArrayList<String> possibleEnclosingAssets = new ArrayList<String>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT asset_name FROM assets WHERE hoa_name = ? AND type_asset = 'P' AND asset_id != ? AND status != 'X'", (String) assets.get("hoa_name"), (int) assets.get("asset_id"));
        for (int i = 0; i < result.size(); i++){
            possibleEnclosingAssets.add((String) result.get(i).get("asset_name"));
        }
        asset.updatePossibleEnclosingAsset(possibleEnclosingAssets);
        return asset;
    }

    @GetMapping("/myEndpoint2")
    public void myEndpoint2(@RequestParam int id) {
        System.out.println(id);
        jdbcTemplate.update("DELETE FROM assets WHERE asset_id = ? AND NOT EXISTS (SELECT 1 FROM asset_transactions WHERE asset_id = ?)", id, id);
    }

    @GetMapping("/myEndpoint3")
    public int myEndpoint3() {
        Object result = jdbcTemplate.queryForObject("SELECT MAX(asset_id)+1 FROM assets", Object.class);
        if (result == null){
            return 5000;
        }
        else {
            return Integer.parseInt(result.toString());
        }
    }

    @GetMapping("/myEndpoint4")
    public void myEndpoint4(@RequestParam int id) {
        jdbcTemplate.update("UPDATE assets SET status = 'X' WHERE asset_id = ? AND status = 'S'",id);
    }


    @PostMapping("/updateAsset")
    public void updateAsset(@RequestBody AssetTable asset) throws ScriptException {

        String update = "UPDATE assets " +
                "SET asset_name = ?, " +
                "    asset_description = ?, " +
                "    acquisition_date = ?, " +
                "    forrent = ?, " +
                "    asset_value = ?, " +
                "    type_asset = ?, " +
                "    status = ?, " +
                "    loc_lattitude = ?, " +
                "    loc_longiture = ?, " +
                "    hoa_name = ?, " +
                "    enclosing_asset = ? " +
                "    WHERE asset_id = ?";
        String enclosingAsset = asset.getEnclosingAsset();
        if (!enclosingAsset.equals("None")){
            int enclosingAssetINT = (int) jdbcTemplate.queryForList("SELECT DISTINCT asset_id FROM assets WHERE asset_name = ?", enclosingAsset).get(0).get("asset_id");
            jdbcTemplate.update(update, asset.getName(), asset.getDescription(), asset.getAcquisitionDate(), asset.isForrent(), asset.getAssetValue(), asset.getType(), asset.getStatus(), asset.getLocLattitude(), asset.getLocLongitude(), asset.getHoaName(), enclosingAssetINT, asset.getId());
        }
        else {
            jdbcTemplate.update(update, asset.getName(), asset.getDescription(), asset.getAcquisitionDate(), asset.isForrent(), asset.getAssetValue(), asset.getType(), asset.getStatus(), asset.getLocLattitude(), asset.getLocLongitude(), asset.getHoaName(), null, asset.getId());
        }

    }

    @PostMapping("/insertAsset")
    public void insertAsset(@RequestBody AssetTable asset) {
        String query = "INSERT INTO assets (asset_id, asset_name, asset_description, acquisition_date, forrent, asset_value, type_asset, status, loc_lattitude, loc_longiture, hoa_name, enclosing_asset) "
            + "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, asset.getId(), asset.getName(), asset.getDescription(), asset.isForrent(), asset.getAssetValue(), asset.getType(), asset.getStatus(),asset.getLocLattitude(), asset.getLocLongitude(), asset.getHoaName(), null);
    }
}