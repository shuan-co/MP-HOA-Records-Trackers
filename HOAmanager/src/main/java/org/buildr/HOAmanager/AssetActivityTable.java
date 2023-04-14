package org.buildr.HOAmanager;

import java.math.BigDecimal;
import java.sql.Date;

public class AssetActivityTable {
    private int id;
    private java.sql.Date activityDate;
    private String description;
    private java.sql.Date tStart;
    private java.sql.Date tEnd;
    private java.sql.Date aStart;
    private java.sql.Date aEnd;
    private BigDecimal cost;
    private String status;

    public Officer getTransOfficer() {
        return transOfficer;
    }

    private Officer transOfficer;
    public int getId() {
        return id;
    }

    public java.sql.Date getActivityDate() {
        return activityDate;
    }

    public String getDescription() {
        return description;
    }

    public java.sql.Date gettStart() {
        return tStart;
    }

    public java.sql.Date gettEnd() {
        return tEnd;
    }

    public java.sql.Date getaStart() {
        return aStart;
    }

    public java.sql.Date getaEnd() {
        return aEnd;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public AssetActivityTable(int id, java.sql.Date activityDate, String description, Date tStart, Date tEnd, Date aStart,
                              Date aEnd, BigDecimal cost, String status, Officer transOfficer) {
        this.id = id;
        this.activityDate = activityDate;
        this.description = description;
        this.tStart = tStart;
        this.tEnd = tEnd;
        this.aStart = aStart;
        this.aEnd = aEnd;
        this.cost = cost;
        this.status = status;
        this.transOfficer = transOfficer;
    }
}
