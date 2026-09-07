package com.example.smartpantrymanager;
import com.google.gson.annotations.SerializedName;

public class PantryItem {
    @SerializedName("id")
    private int id;

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
}