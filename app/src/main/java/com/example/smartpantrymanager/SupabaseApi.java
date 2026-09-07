package com.example.smartpantrymanager;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SupabaseApi {

    @GET("pantry_items?select=*")
    Call<List<PantryItem>> getPantryItems();

    @GET("recipes?select=*")
    Call<List<Recipe>> getRecipes();

}