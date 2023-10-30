package com.example.oosd2023project_android;

public class Package {
    /* member variables to store package information*/
    private int packageId;
    private String pkgName;
    private String pkgStartDate;
    private String pkgEndDate;
    private String pkgDesc;
    private String pkgBasePrice;
    private String pkgAgencyCommission;

    /*constructor to initialize package*/
    public Package(int packageId, String pkgName, String pkgStartDate, String pkgEndDate, String pkgDesc, String pkgBasePrice, String pkgAgencyCommission) {
        this.packageId = packageId;
        this.pkgName = pkgName;
        this.pkgStartDate = pkgStartDate;
        this.pkgEndDate = pkgEndDate;
        this.pkgDesc = pkgDesc;
        this.pkgBasePrice = pkgBasePrice;
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

    /*getter and setter for the member variables*/
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgStartDate() {
        return pkgStartDate;
    }

    public void setPkgStartDate(String pkgStartDate) {
        this.pkgStartDate = pkgStartDate;
    }

    public String getPkgEndDate() {
        return pkgEndDate;
    }

    public void setPkgEndDate(String pkgEndDate) {
        this.pkgEndDate = pkgEndDate;
    }

    public String getPkgDesc() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc = pkgDesc;
    }

    public String getPkgBasePrice() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(String pkgBasePrice) {
        this.pkgBasePrice = pkgBasePrice;
    }

    public String getPkgAgencyCommission() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(String pkgAgencyCommission) {
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

    /*to display the package-specific  information*/
    @Override
    public String toString() {
        return packageId + ". "+pkgName +" ,$" + pkgBasePrice ;
    }
}
