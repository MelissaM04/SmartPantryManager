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
        adapter = new PantryAdapter(item -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, IngredientDetailActivity.class);
            // Package the tapped ingredient's data to send to the detail screen
            intent.putExtra("ITEM_ID", item.getId());
            intent.putExtra("ITEM_NAME", item.getName());
            intent.putExtra("ITEM_QUANTITY", item.getQuantity());
            intent.putExtra("ITEM_UNIT", item.getUnit());
            intent.putExtra("ITEM_EXPIRY", item.getExpiryDate());
            startActivity(intent);
        });
        rvPantryList.setAdapter(adapter);

        //Set click listener
        com.google.android.material.floatingactionbutton.FloatingActionButton fab = findViewById(R.id.fabAddIngredient);
        fab.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });

        // Initialize Bottom Navigation
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Highlight Pantry
        bottomNavigationView.setSelectedItemId(R.id.nav_pantry);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                return true;
            } else if (itemId == R.id.nav_recipes) {
                startActivity(new android.content.Intent(MainActivity.this, SuggestedRecipesActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new android.content.Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        //Get data from Supabase
        loadPantryData();
    }

    @Override
    protected void onResume() {
        super.onResume();
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