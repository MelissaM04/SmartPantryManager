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

public class IngredientDetailActivity extends AppCompatActivity {

    private int currentItemId;
    private EditText etName, etQuantity, etUnit, etExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredient_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etDetailName);
        etQuantity = findViewById(R.id.etDetailQuantity);
        etUnit = findViewById(R.id.etDetailUnit);
        etExpiry = findViewById(R.id.etDetailExpiry);
        Button btnUpdate = findViewById(R.id.btnUpdateIngredient);
        Button btnDelete = findViewById(R.id.btnDeleteIngredient);
        Button btnCancel = findViewById(R.id.btnCancelDetail);

        android.content.Intent intent = getIntent();
        currentItemId = intent.getIntExtra("ITEM_ID", -1);
        etName.setText(intent.getStringExtra("ITEM_NAME"));
        etQuantity.setText(String.valueOf(intent.getDoubleExtra("ITEM_QUANTITY", 0.0)));
        etUnit.setText(intent.getStringExtra("ITEM_UNIT"));
        etExpiry.setText(intent.getStringExtra("ITEM_EXPIRY"));

        btnUpdate.setOnClickListener(v -> updateItem());
        btnDelete.setOnClickListener(v -> deleteItem());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty() || expiry.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Package the updated data
        PantryItem updatedItem = new PantryItem();
        updatedItem.setId(currentItemId);
        updatedItem.setName(name);
        updatedItem.setQuantity(Double.parseDouble(qtyStr));
        updatedItem.setUnit(unit);
        updatedItem.setExpiryDate(expiry);

        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        Call<Void> call = api.updatePantryItem("eq." + currentItemId, updatedItem);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(IngredientDetailActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(IngredientDetailActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(IngredientDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem() {
        SupabaseApi api = SupabaseClient.getClient().create(SupabaseApi.class);
        Call<Void> call = api.deletePantryItem("eq." + currentItemId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(IngredientDetailActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(IngredientDetailActivity.this, "Delete failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(IngredientDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}