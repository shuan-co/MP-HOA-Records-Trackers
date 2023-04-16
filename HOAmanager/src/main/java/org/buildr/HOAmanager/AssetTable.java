package org.buildr.HOAmanager;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AssetTable {
    private int id;
    private String name;
    private String description;
    private String type;
    private String status;
    private java.sql.Date acquisitionDate;
    private Boolean forrent;
    private BigDecimal assetValue;
    private BigDecimal locLattitude;
    private BigDecimal locLongitude;
    private String hoaName;
    private String enclosingAsset;
    private ArrayList<String> possibleEnclosingAssets;

    // Add default constructor
    public AssetTable() {}

    public AssetTable(int id, String name, String description, String type, String status, java.sql.Date acquisitionDate, Boolean forrent, BigDecimal assetValue, BigDecimal locLattitude, BigDecimal locLongitude, String hoaName, String enclosingAsset) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.acquisitionDate = acquisitionDate;
        this.forrent = forrent;
        this.assetValue = assetValue;
        this.locLattitude = locLattitude;
        this.locLongitude = locLongitude;
        this.hoaName = hoaName;
        this.enclosingAsset = enclosingAsset;
    }

    // Getters and Setters
    public String getEnclosingAsset(){
        return enclosingAsset;
    }

    public void setEnclosingAsset(String enclosingAsset){
        this.enclosingAsset = enclosingAsset;
    }

    public void updatePossibleEnclosingAsset(ArrayList<String> possibleEnclosingAssets){
        this.possibleEnclosingAssets = possibleEnclosingAssets;
    }

    public ArrayList<String> getPossibleEnclosingAssets(){
        return possibleEnclosingAssets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public java.sql.Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(java.sql.Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Boolean isForrent() {
        return forrent;
    }

    public void setForrent(Boolean forrent) {
        this.forrent = forrent;
    }

    public BigDecimal getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(BigDecimal assetValue) {
        this.assetValue = assetValue;
    }

    public BigDecimal getLocLattitude() {
        return locLattitude;
    }

    public void setLocLattitude(BigDecimal locLattitude) {
        this.locLattitude = locLattitude;
    }

    public BigDecimal getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(BigDecimal locLongitude) {
        this.locLongitude = locLongitude;
    }

    public String getHoaName() {
        return hoaName;
    }

    public void setHoaName(String hoaName) {
        this.hoaName = hoaName;
    }
}