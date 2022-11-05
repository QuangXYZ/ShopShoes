package com.example.shopshoes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopshoes.Admin.Brand.UpdateBrandActivity;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.R;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Brand> brandArrayList;
    private Brand brand;

    public BrandAdapter(Activity context, ArrayList<Brand> brandArrayList) {
        this.context = context;
        this.brandArrayList = brandArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_brand, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand brand = brandArrayList.get(position);
        holder.brandId.setText(brand.getBrandId());
        holder.brandName.setText(brand.getBrandName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateBrandActivity.class);
                intent.putExtra("brandId", brand.getBrandId());
                intent.putExtra("brand",brand);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brandId, brandName;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brandId = itemView.findViewById(R.id.brand_id);
            brandName = itemView.findViewById(R.id.brand_name);
            layout = itemView.findViewById(R.id.layout_brand);
        }
    }
}

