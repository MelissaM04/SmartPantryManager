package com.example.smartpantrymanager;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Recipe {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("required_ingredients")
    private List<String> requiredIngredients;

    @SerializedName("preparation_steps")
    private String preparationSteps;

    //Getters
    public Integer getId() { return id != null ? id : 0; }
    public String getName() { return name; }
    public List<String> getRequiredIngredients() { return requiredIngredients; }
    public String getPreparationSteps() { return preparationSteps; }

    //Setters
    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRequiredIngredients(List<String> requiredIngredients) { this.requiredIngredients = requiredIngredients; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}