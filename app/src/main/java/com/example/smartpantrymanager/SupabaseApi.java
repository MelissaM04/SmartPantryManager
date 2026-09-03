package com.example.smartpantrymanager;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface SupabaseApi {
    @GET("pantry_items?select=*")
    Call<List<Object>> getPantryItems();
}