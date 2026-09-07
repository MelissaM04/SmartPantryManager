package com.example.smartpantrymanager;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Recipe {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("required_ingredients")
    private List<String> requiredIngredients;

    @SerializedName("preparation_steps")
    private String preparationSteps;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public List<String> getRequiredIngredients() { return requiredIngredients; }
    public String getPreparationSteps() { return preparationSteps; }
}