package com.example.smartpantrymanager;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {

    @GET("pantry_items?select=*")
    Call<List<PantryItem>> getPantryItems();

    @POST("pantry_items")
    Call<Void> addPantryItem(@Body PantryItem item);

    @PATCH("pantry_items")
    Call<Void> updatePantryItem(@Query("id") String idQuery, @Body PantryItem item);

    @DELETE("pantry_items")
    Call<Void> deletePantryItem(@Query("id") String idQuery);
}