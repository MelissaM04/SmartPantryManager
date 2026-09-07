package com.example.smartpantrymanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPantryList;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Adjust for phone status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize RecyclerView
        rvPantryList = findViewById(R.id.rvPantryList);
        rvPantryList.setLayoutManager(new LinearLayoutManager(this));

        //Attach Adapter
        adapter = new PantryAdapter();
        rvPantryList.setAdapter(adapter);

        //Get data from Supabase
        loadPantryData();
    }

    private void loadPantryData() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        Call<List<PantryItem>> call = api.getPantryItems();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PantryItem>> call, @NonNull Response<List<PantryItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PantryItem> items = response.body();
                    adapter.setPantryItems(items);
                    Log.d("SUPABASE_TEST", "Successfully loaded " + items.size() + " items.");
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PantryItem>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}