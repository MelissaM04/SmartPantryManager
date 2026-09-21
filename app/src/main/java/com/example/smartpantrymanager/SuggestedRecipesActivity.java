package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView rvRecipes, rvAlmostThereRecipes;
    private RecipeAdapter strictAdapter, almostThereAdapter;
    private TextView tvNoMatchMessage, tvNoAlmostThereMessage;
    private List<PantryItem> currentPantry = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNoMatchMessage = findViewById(R.id.tvNoMatchMessage);
        tvNoAlmostThereMessage = findViewById(R.id.tvNoAlmostThereMessage);

        //Strict Match List
        rvRecipes = findViewById(R.id.rvRecipes);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        strictAdapter = createAdapter();
        rvRecipes.setAdapter(strictAdapter);

        //Almost There List
        rvAlmostThereRecipes = findViewById(R.id.rvAlmostThereRecipes);
        rvAlmostThereRecipes.setLayoutManager(new LinearLayoutManager(this));
        almostThereAdapter = createAdapter();
        rvAlmostThereRecipes.setAdapter(almostThereAdapter);

        //Bottom navigation
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_recipes);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                startActivity(new android.content.Intent(SuggestedRecipesActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            } else if (itemId == R.id.nav_recipes) {
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new android.content.Intent(SuggestedRecipesActivity.this, SettingsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            return false;
        });

        fetchPantryInventory();
    }

    // Helper method to keep adapter creation clean
    private RecipeAdapter createAdapter() {
        return new RecipeAdapter(recipe -> {
            android.content.Intent intent = new android.content.Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_NAME", recipe.getName());

            List<String> formattedIngredients = new ArrayList<>();
            for (String req : recipe.getRequiredIngredients()) {
                String[] parts = req.split("\\|");
                if (parts.length == 3) {
                    formattedIngredients.add(parts[0] + " (" + parts[1] + " " + parts[2] + ")");
                } else {
                    formattedIngredients.add(req); // Fallback just in case you forgot a pipe in Supabase
                }
            }
            String ingredientsStr = android.text.TextUtils.join("\n• ", formattedIngredients);
            intent.putExtra("RECIPE_INGREDIENTS", "• " + ingredientsStr);
            intent.putExtra("RECIPE_METHOD", recipe.getPreparationSteps());

            String missingIngredient = "";
            for (String req : recipe.getRequiredIngredients()) {
                if (!isIngredientInPantry(req)) {
                    String[] parts = req.split("\\|");
                    if (parts.length == 3) {
                        missingIngredient = parts[0] + " (" + parts[1] + " " + parts[2] + ")";
                    } else {
                        missingIngredient = req;
                    }
                    break;
                }
            }
            intent.putExtra("MISSING_INGREDIENT", missingIngredient);

            startActivity(intent);
        });
    }

    private void fetchPantryInventory() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        api.getPantryItems().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PantryItem>> call, @NonNull Response<List<PantryItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentPantry = response.body();
                    fetchRecipesAndMatch();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PantryItem>> call, @NonNull Throwable t) {
                Toast.makeText(SuggestedRecipesActivity.this, "Failed to load pantry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipesAndMatch() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        api.getRecipes().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    runStrictMatchingEngine(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText(SuggestedRecipesActivity.this, "Failed to load recipes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void runStrictMatchingEngine(List<Recipe> allRecipes) {
        List<Recipe> strictMatches = new ArrayList<>();
        List<Recipe> almostThereMatches = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            int missingIngredientsCount = 0;

            //Check every ingredient
            for (String reqIngredient : recipe.getRequiredIngredients()) {
                if (!isIngredientInPantry(reqIngredient)) {
                    missingIngredientsCount++;
                }
            }

            //Route recipe based on missing count
            if (missingIngredientsCount == 0) {
                strictMatches.add(recipe);
            } else if (missingIngredientsCount == 1) {
                almostThereMatches.add(recipe);
            }
        }

        if (strictMatches.isEmpty()) {
            rvRecipes.setVisibility(View.GONE);
            tvNoMatchMessage.setVisibility(View.VISIBLE);
        } else {
            tvNoMatchMessage.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
            strictAdapter.setRecipes(strictMatches);
        }

        if (almostThereMatches.isEmpty()) {
            rvAlmostThereRecipes.setVisibility(View.GONE);
            tvNoAlmostThereMessage.setVisibility(View.VISIBLE);
        } else {
            tvNoAlmostThereMessage.setVisibility(View.GONE);
            rvAlmostThereRecipes.setVisibility(View.VISIBLE);
            almostThereAdapter.setRecipes(almostThereMatches);
        }
    }

    private boolean isIngredientInPantry(String required) {
        String[] parts = required.split("\\|");
        String reqName = parts[0].toLowerCase().trim();
        double reqQty = 0.0;

        if (parts.length >= 2) {
            try {
                reqQty = Double.parseDouble(parts[1].trim());
            } catch (Exception e) {
                reqQty = 0.0;
            }
        }

        if (reqName.endsWith("es")) reqName = reqName.substring(0, reqName.length() - 2);
        else if (reqName.endsWith("s")) reqName = reqName.substring(0, reqName.length() - 1);

        for (PantryItem item : currentPantry) {
            if (item.getName() == null) continue;

            String pantryStr = item.getName().toLowerCase().trim();
            if (pantryStr.endsWith("es")) pantryStr = pantryStr.substring(0, pantryStr.length() - 2);
            else if (pantryStr.endsWith("s")) pantryStr = pantryStr.substring(0, pantryStr.length() - 1);

            if (pantryStr.contains(reqName) || reqName.contains(pantryStr)) {

                if (item.getQuantity() >= reqQty) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}