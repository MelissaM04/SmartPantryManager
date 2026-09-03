package com.example.smartpantrymanager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {
    private static final String BASE_URL = "https://zxeorhoaszubweluhztd.supabase.co/rest/v1/";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp4ZW9yaG9hc3p1YndlbHVoenRkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODgxNzEyMzQsImV4cCI6MjEwMzc0NzIzNH0.86zpi9iRun2eRrB-OuN9V2btwd_ddzbYTfr-k9tVxwE";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Lambda replacement for the Interceptor
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("apikey", SUPABASE_KEY)
                        .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                        .addHeader("Prefer", "return=representation")
                        .build();
                return chain.proceed(newRequest);
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}