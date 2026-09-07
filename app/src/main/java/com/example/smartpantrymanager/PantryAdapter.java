package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList = new ArrayList<>();

    //Takes database list and updates screen
    public void setPantryItems(List<PantryItem> items) {
        this.pantryList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Gets item_pantry.xml layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        //Adds database data into text fields for each row
        PantryItem currentItem = pantryList.get(position);
        holder.tvName.setText(currentItem.getName());
        holder.tvQuantity.setText(String.valueOf(currentItem.getQuantity()));
        holder.tvUnit.setText(currentItem.getUnit());
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    //Connects Java code to XML IDs
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvUnit;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvUnit = itemView.findViewById(R.id.tvUnit);
        }
    }
}