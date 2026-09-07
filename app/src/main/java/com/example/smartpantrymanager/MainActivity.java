package com.example.smartpantrymanager;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);

        // Updated to use PantryItem instead of Object
        Call<List<PantryItem>> call = api.getPantryItems();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PantryItem>> call, @NonNull Response<List<PantryItem>> response) {
                if (response.isSuccessful()) {
                    Log.d("SUPABASE_TEST", "SUCCESS! Connected to Supabase. Data: " + response.body());
                } else {
                    Log.e("SUPABASE_TEST", "Connected, but got an error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PantryItem>> call, @NonNull Throwable t) {
                Log.e("SUPABASE_TEST", "FAILED to connect! Error: " + t.getMessage());
            }
        });
    }
}