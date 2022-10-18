package com.example.shopshoes.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>{

    List<Product> myJokesList;
    Activity context;
    boolean isAdmin;

    public ProductsAdapter(List<Product> usersList, Activity context, boolean isAdmin) {
        this.myJokesList = usersList;
        this.context = context;
        this.isAdmin=isAdmin;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Product product=myJokesList.get(position);

        if(product.getPhotoUrl()!=null){
            if(!product.getPhotoUrl().equals("")){
                holder.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.no_background_icon).into(holder.productImg);
            }
        }
        holder.name.setText(product.getName());
        holder.price.setText("$"+product.getPrice());

//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isAdmin){
//                    Intent intent=new Intent(context, ProductDetailsActivity.class);
//                    intent.putExtra("product",product);
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return myJokesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        ImageView productImg;
        TextView name,price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_product);
            productImg = itemView.findViewById(R.id.category_image_product);
            name = itemView.findViewById(R.id.product_brand_name_admin);
            price = itemView.findViewById(R.id.price_tv_product);
        }
    }
}