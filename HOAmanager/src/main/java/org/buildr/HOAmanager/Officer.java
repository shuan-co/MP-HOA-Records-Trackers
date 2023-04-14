package org.buildr.HOAmanager;

import java.sql.Date;

public class Officer {
    private int hoId;
    private String position;
    private java.sql.Date electionDate;
    private String firstName;
    private String lastName;
    public Officer(int hoId, String position, Date electionDate, String firstName, String lastName) {
        this.hoId = hoId;
        this.position = position;
        this.electionDate = electionDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getHoId() {
        return hoId;
    }

    public String getPosition() {
        return position;
    }

    public Date getElectionDate() {
        return electionDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
