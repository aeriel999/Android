package com.example.sim.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sim.R;
import com.example.sim.constants.Urls;
import com.example.sim.dto.category.CategoryDto;


import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryCardViewHolder> {
    private List<CategoryDto> items;
    private final  OnItemClickListener editCategory;

    private final  OnItemClickListener deleteCategory;


    public CategoriesAdapter(List<CategoryDto> items, OnItemClickListener editCategory,  OnItemClickListener deleteCategory) {
        this.items = items;
        this.editCategory = editCategory;
        this.deleteCategory = deleteCategory;
    }

    @NonNull
    @Override
    public CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardViewHolder holder, int position) {
        if(items!=null && position<items.size()) {
            CategoryDto item = items.get(position);
            holder.getCategoryName().setText(item.getName());
            holder.getCategoryDescription().setText(item.getDescription());

            String imageUrl = Urls.BASE + "/images/" + item.getImage();

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.getCategoryImage());

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editCategory.onItemClick(item);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCategory.onItemClick(item);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}
