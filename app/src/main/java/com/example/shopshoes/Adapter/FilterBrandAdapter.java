package com.example.shopshoes.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopshoes.Admin.Brand.UpdateBrandActivity;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.R;

import java.util.ArrayList;
import java.util.List;

public class FilterBrandAdapter extends RecyclerView.Adapter<FilterBrandAdapter.MyViewHolder> {
    ArrayList<Brand>  brandList;
    Activity context;

    public FilterBrandAdapter(ArrayList<Brand> brandList, Activity context) {
        this.brandList = brandList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_category, parent, false);

        return new FilterBrandAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.nameBrand.setText(brand.getBrandName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setCardBackgroundColor(Color.argb(255,255,193,7));
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView nameBrand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.Filter_layout);
            nameBrand = itemView.findViewById(R.id.Filter_name);
        }
    }
}
