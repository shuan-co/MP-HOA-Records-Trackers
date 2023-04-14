package org.buildr.HOAmanager;

import java.math.BigDecimal;
import java.sql.Date;

public class AssetActivityRow {
    private int id;
    private java.sql.Date activityDate;

    private String description;

    private BigDecimal cost;

    private String status;

    public Date getActivityDate() {
        return activityDate;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }
    public int getId() {
        return id;
    }

    public AssetActivityRow(int id, Date activityDate, String description, BigDecimal cost, String status) {
        this.id = id;
        this.activityDate = activityDate;
        this.description = description;
        this.cost = cost;
        this.status = status;
    }
}
