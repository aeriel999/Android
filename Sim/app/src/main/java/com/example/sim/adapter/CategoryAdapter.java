package com.example.sim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sim.R;
import com.example.sim.databinding.CategoryItemBinding;
import com.example.sim.dto.category.CategoryDto;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    public class CategoryHolder extends RecyclerView.ViewHolder {

        private final CategoryItemBinding binding;

        public CategoryHolder(View itemView) {
            super(itemView);
            binding = CategoryItemBinding.bind(itemView);
        }

        public void bind(@NonNull CategoryDto category){
            binding.categoryTitle.setText(category.getName());
            binding.categoryDescription.setText(category.getDescription());
            //  binding.categoryImageView.

        }



    }

    private final List<CategoryDto> categoryDtoList;

    public CategoryAdapter(  List<CategoryDto> states) {
        this.categoryDtoList = states;

    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryHolder holder, int position) {
        CategoryDto category = categoryDtoList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryDtoList.size();
    }

}
