package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredient);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etName = findViewById(R.id.etIngredientName);
        EditText etQuantity = findViewById(R.id.etQuantity);
        EditText etUnit = findViewById(R.id.etUnit);
        EditText etExpiryDate = findViewById(R.id.etExpiryDate);
        Button btnSave = findViewById(R.id.btnSaveIngredient);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();
            String expiry = etExpiryDate.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || unit.isEmpty() || expiry.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            PantryItem newItem = new PantryItem();
            newItem.setName(name);
            newItem.setQuantity(Double.parseDouble(quantityStr));
            newItem.setUnit(unit);
            newItem.setExpiryDate(expiry);

            saveToDatabase(newItem);
        });
    }

    private void saveToDatabase(PantryItem item) {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        Call<Void> call = api.addPantryItem(item);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddIngredientActivity.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddIngredientActivity.this, "Error saving: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(AddIngredientActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}