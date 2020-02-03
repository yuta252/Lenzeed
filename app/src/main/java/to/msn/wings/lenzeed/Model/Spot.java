package to.msn.wings.lenzeed.Model;

import java.io.Serializable;

public class Spot implements Serializable {
    private static final long id = 1L;

    private String name;
    private String thumbnail;
    private String information;
    private String majorCategory;
    private String spotpk;
    private String address;
    private String telephone;
    private String entranceFee;
    private String businessHours;
    private String holiday;

    public Spot() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getMajorCategory() {
        return majorCategory;
    }

    public void setMajorCategory(String majorCategory) {
        this.majorCategory = majorCategory;
    }

    public String getSpotpk() {
        return spotpk;
    }

    public void setSpotpk(String spotpk) {
        this.spotpk = spotpk;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEntranceFee() {
        return entranceFee;
    }

    public void setEntranceFee(String entranceFee) {
        this.entranceFee = entranceFee;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
}

