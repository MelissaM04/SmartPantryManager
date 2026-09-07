package com.example.smartpantrymanager;
import com.google.gson.annotations.SerializedName;

public class PantryItem {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("unit")
    private String unit;

    @SerializedName("expiry_date")
    private String expiryDate;

    //Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }

    //Setters
    public void setName(String name) { this.name = name; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}