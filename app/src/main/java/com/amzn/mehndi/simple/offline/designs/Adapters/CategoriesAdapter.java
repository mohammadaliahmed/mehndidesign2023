package com.amzn.mehndi.simple.offline.designs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.amzn.mehndi.simple.offline.designs.Activites.ViewCategory;
import com.amzn.mehndi.simple.offline.designs.Models.CategoryModel;
import com.amzn.mehndi.simple.offline.designs.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.Viewholder> {
    Context context;
    List<CategoryModel> itemList;

    public CategoriesAdapter(Context context, List<CategoryModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<CategoryModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Viewholder viewHolder;
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false);
        viewHolder = new Viewholder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.Viewholder holder, int position) {
        CategoryModel category = itemList.get(position);
        holder.name.setText(category.getCategoryName());
        Glide.with(context).load(category.getImg()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ViewCategory.class).putExtra("category", category));

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);

        }
    }
}
