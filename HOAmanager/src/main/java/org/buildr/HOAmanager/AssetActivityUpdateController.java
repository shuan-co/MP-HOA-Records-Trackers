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
    public AssetActivityTable getAssetActivity(@RequestParam int id, @RequestParam java.sql.Date activityDate) {
        var assetActivities = jdbcTemplate.queryForList("SELECT * FROM asset_activity WHERE asset_id = ? AND activity_date = ?;", id, activityDate).get(0);
        var officer = jdbcTemplate.queryForList("SELECT ast.trans_hoid, ast.trans_position, ast.trans_electiondate, p.firstname, p.lastname\n" +
                "FROM asset_activity aa\n" +
                "JOIN asset_transactions ast ON ast.asset_id = aa.asset_id AND\n" +
                "                ast.transaction_date = aa.activity_date\n" +
                "JOIN people p ON p.peopleid = ast.trans_hoid\n" +
                "WHERE aa.asset_id = ? AND aa.activity_date = ?;", id, activityDate).get(0);
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
    public List<Officer> getOfficers(@RequestParam String hoaName) {
        var officers = new ArrayList<Officer>();
        var result = jdbcTemplate.queryForList("SELECT * FROM officer o JOIN homeowner h ON o.ho_id = h.ho_id\n" +
                "JOIN people p ON p.peopleid = h.ho_id\n" +
                "JOIN properties pr ON pr.ho_id = h.ho_id\n" +
                "JOIN hoa ON hoa.hoa_name = pr.hoa_name\n" +
                "    WHERE o.end_date >= NOW() AND hoa.hoa_name = ?\n" +
                "GROUP BY h.ho_id;", hoaName);

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

    @GetMapping("/getPresident")
    public Officer getPresident(@RequestParam String hoaName) {
        var result = jdbcTemplate.queryForList("SELECT * FROM officer o JOIN homeowner h ON o.ho_id = h.ho_id\n" +
                "JOIN officer_presidents op ON op.ho_id = o.ho_id\n" +
                "JOIN people p ON p.peopleid = h.ho_id\n" +
                "JOIN properties pr ON pr.ho_id = h.ho_id\n" +
                "JOIN hoa ON hoa.hoa_name = pr.hoa_name\n" +
                "    WHERE o.end_date >= NOW() AND hoa.hoa_name = ?\n" +
                "GROUP BY h.ho_id;", hoaName).get(0);

        var officer = new Officer((int)result.get("ho_id"),
                    (String)result.get("position"),
                    (java.sql.Date)result.get("election_date"),
                    (String)result.get("firstname"),
                    (String)result.get("lastname"));

        return officer;
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

    @GetMapping("/disposeAssetActivity")
    public void disposeAssetActivity(@RequestParam int id, @RequestParam java.sql.Date activityDate,
                                     @RequestParam int presidentId, @RequestParam String presidentPosition,
                                     @RequestParam java.sql.Date presidentElectionDate) {
        var setDeleted = "UPDATE asset_transactions SET isdeleted = 1 WHERE asset_id = ? AND transaction_date = ?";
        var setOfficer = "UPDATE asset_transactions SET approval_hoid = ?, approval_position = ?, approval_electiondate = ?\n" +
                "WHERE asset_id = ? AND transaction_date = ?";
        jdbcTemplate.update(setOfficer, presidentId, presidentPosition, presidentElectionDate, id, activityDate);
        jdbcTemplate.update(setDeleted, id, activityDate);
    }

    @PostMapping("/updateAssetActivity")
    public void updateAssetActivity(@RequestBody AssetActivityTable assetActivity) {
        if(!assetActivity.getStatus().equals("C")) {
            var updateActivity = "UPDATE asset_activity\n" +
                    "SET activity_description = ?," +
                    "    tent_start = ?," +
                    "    tent_end = ?," +
                    "    act_start = ?," +
                    "    act_end = ?," +
                    "    cost = ?," +
                    "    status = ? \n" +
                    "WHERE asset_id = ? AND activity_date = ?";
            var updateTransaction = "UPDATE asset_transactions\n" +
                    "SET trans_hoid = ?," +
                    "    trans_position = ?," +
                    "    trans_electiondate = ?\n" +
                    "WHERE asset_id = ? AND transaction_date = ?";

            jdbcTemplate.update(updateActivity, assetActivity.getDescription(), assetActivity.gettStart(),
                    assetActivity.gettEnd(), assetActivity.getaStart(), assetActivity.getaEnd(),
                    assetActivity.getCost(), assetActivity.getStatus(), assetActivity.getId(), assetActivity.getActivityDate());
            jdbcTemplate.update(updateTransaction, assetActivity.getTransOfficer().getHoId(),
                    assetActivity.getTransOfficer().getPosition(),
                    assetActivity.getTransOfficer().getElectionDate(), assetActivity.getId(), assetActivity.getActivityDate());

        }
        else {
            int id = generateOR();
            var updateActivity = "UPDATE asset_activity\n" +
                    "SET activity_description = ?," +
                    "    tent_start = ?," +
                    "    tent_end = ?," +
                    "    act_start = ?," +
                    "    act_end = ?," +
                    "    cost = ?," +
                    "    status = ?\n" +
                    "WHERE asset_id = ? AND activity_date = ?";
            var updateTransaction = "UPDATE asset_transactions\n" +
                    "SET trans_hoid = ?," +
                    "    trans_position = ?," +
                    "    trans_electiondate = ?," +
                    "    ornum = ?\n" +
                    "WHERE asset_id = ? AND transaction_date = ?";
            jdbcTemplate.update("INSERT INTO ref_ornumbers VALUES(?)", id);
            jdbcTemplate.update(updateActivity, assetActivity.getDescription(), assetActivity.gettStart(),
                    assetActivity.gettEnd(), assetActivity.getaStart(), assetActivity.getaEnd(),
                    assetActivity.getCost(), assetActivity.getStatus(), assetActivity.getId(), assetActivity.getActivityDate());
            jdbcTemplate.update(updateTransaction, assetActivity.getTransOfficer().getHoId(),
                    assetActivity.getTransOfficer().getPosition(),
                    assetActivity.getTransOfficer().getElectionDate(), id, assetActivity.getId(), assetActivity.getActivityDate());
        }
    }

    private int generateOR() {
        int id;
        int count;
        do {
            id = IDGenerator.generate(7);
            count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ref_ornumbers WHERE ornum = ?", new Object[]{id}, Integer.class);
        }while(count == 1);

        return id;
    }
}
