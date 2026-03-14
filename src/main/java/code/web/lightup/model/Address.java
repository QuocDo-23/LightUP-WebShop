package code.web.lightup.model;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private int userId;
    private String recipientName;
    private String phone;
    private String email;
    private String house_number;
    private String commune;
    private String district;
    private String addressDetail;
    private boolean isDefault;

    public Address() {
    }

    public Address(String phone,  int userId, String recipientName,
                   String house_number, String commune, String district, String addressDetail) {
        this.phone = phone;
        this.userId = userId;
        this.recipientName = recipientName;
        this.house_number = house_number;
        this.commune = commune;
        this.district = district;
        this.addressDetail = addressDetail;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getHouse_number() {
        return house_number;
    }

    public String getCommune() {
        return commune;
    }

    public String getDistrict() {
        return district;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}


