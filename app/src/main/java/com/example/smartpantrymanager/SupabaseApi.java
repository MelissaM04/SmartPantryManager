package com.example.smartpantrymanager;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SupabaseApi {
    @GET("pantry_items?select=*")
    Call<List<PantryItem>> getPantryItems();

    @POST("pantry_items")
    Call<Void> addPantryItem(@Body PantryItem item);
}