package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public RecipeAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipeList = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.tvName.setText(recipe.getName());

        //Format ingredients to remove pipes for preview list
        java.util.List<String> formattedPreview = new java.util.ArrayList<>();
        for (String req : recipe.getRequiredIngredients()) {
            String[] parts = req.split("\\|");
            if (parts.length == 3) {
                formattedPreview.add(parts[0] + " (" + parts[1] + " " + parts[2] + ")");
            } else {
                formattedPreview.add(req);
            }
        }

        String previewText = "Requires: " + android.text.TextUtils.join(", ", formattedPreview);

        holder.tvIngredients.setText(previewText);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredients = itemView.findViewById(R.id.tvRecipeIngredients);
        }
    }
}