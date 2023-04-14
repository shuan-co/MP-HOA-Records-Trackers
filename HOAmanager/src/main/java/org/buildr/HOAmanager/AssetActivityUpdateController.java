package org.buildr.HOAmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AssetActivityUpdateController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/getAssetActivity")
    public AssetActivityTable getAssetAcitivity(@RequestParam int id) {
        var assetActivities = jdbcTemplate.queryForList("SELECT * FROM asset_activity WHERE asset_id = ?;", id).get(0);
        var officer = jdbcTemplate.queryForList("SELECT ast.trans_hoid, ast.trans_position, ast.trans_electiondate, p.firstname, p.lastname\n" +
                "FROM asset_activity aa\n" +
                "JOIN asset_transactions ast ON ast.asset_id = aa.asset_id AND\n" +
                "                ast.transaction_date = aa.activity_date\n" +
                "JOIN people p ON p.peopleid = ast.trans_hoid;", id).get(0);
        var transOfficer = new Officer((int) officer.get("trans_hoid"),
                (String)officer.get("trans_position"),
                (java.sql.Date)officer.get("trans_electiondate"),
                (String)officer.get("firstname"),
                (String)officer.get("lastname"));
        var assetActivity = new AssetActivityTable(
                (int)assetActivities.get("asset_id"),
                (java.sql.Date)assetActivities.get("activity_date"),
                (String)assetActivities.get("activity_description"),
                (java.sql.Date)assetActivities.get("tent_start"),
                (java.sql.Date)assetActivities.get("tent_end"),
                (java.sql.Date)assetActivities.get("act_start"),
                (java.sql.Date)assetActivities.get("act_end"),
                (BigDecimal)assetActivities.get("cost"),
                (String)assetActivities.get("status"),
                transOfficer
        );

        return assetActivity;
    }

    @GetMapping("/getOfficers")
    public List<Officer> getOfficers() {
        var officers = new ArrayList<Officer>();
        var result = jdbcTemplate.queryForList("SELECT * FROM officer o JOIN homeowner h ON o.ho_id = h.ho_id " +
                "JOIN people p ON p.peopleid = h.ho_id " +
                "WHERE o.end_date >= NOW();");

        for (var row : result) {
            var officer = new Officer((int)row.get("ho_id"),
                    (String)row.get("position"),
                    (java.sql.Date)row.get("election_date"),
                    (String)row.get("firstname"),
                    (String)row.get("lastname"));
            officers.add(officer);
        }

        return officers;
    }

    @PostMapping("/insertAssetActivity")
    public void insertAssetActivity(@RequestBody AssetActivityTable assetActivity) {
        var insertTransaction = "INSERT INTO asset_transactions (asset_id, transaction_date, isdeleted," +
                "trans_hoid, trans_position, trans_electiondate, transaction_type)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(insertTransaction, assetActivity.getId(), assetActivity.getActivityDate(), 0,
                assetActivity.getTransOfficer().getHoId(), assetActivity.getTransOfficer().getPosition(),
                assetActivity.getTransOfficer().getElectionDate(), "A");

        var insertActivity = "INSERT INTO asset_activity (asset_id, activity_date, " +
                "activity_description, tent_start, tent_end, cost, status)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(insertActivity, assetActivity.getId(), assetActivity.getActivityDate(),
                assetActivity.getDescription(), assetActivity.gettStart(), assetActivity.gettEnd(), assetActivity.getCost(),
                "S");
    }
}
