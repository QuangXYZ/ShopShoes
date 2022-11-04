package com.example.shopshoes.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shopshoes.Activity.OrderDetailActivity;
import com.example.shopshoes.Admin.UpdateProductActivity;
import com.example.shopshoes.Model.Product;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopshoes.Model.Order;

import com.example.shopshoes.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    List<Order> orderList;
    Activity context;

    public OrderAdapter(List<Order> orderList, Activity context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_item, parent, false);

        return new OrderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orderList.get(position);
//        if(order.getProductArrayList().get(0).getPhotoUrl()!=null){
//            if(!order.getProductArrayList().get(0).getPhotoUrl().equals("")){
//                holder.orderImg.setVisibility(View.VISIBLE);
//                Picasso.get().load(order.getProductArrayList().get(0).getPhotoUrl()).placeholder(R.drawable.no_background_icon).into(holder.orderImg);
//            }
//        }
        holder.quality.setText("Số lượng sản phẩm "+order.getProductArrayList().size());
        holder.name.setText("Trạng thái "+order.getStatus());
        holder.date.setText(order.getDateOfOrder());
        holder.price.setText(order.getTotalPrice()+" VND");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderID", order.getId());
                intent.putExtra("order",order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView orderImg;
        TextView name,price,date,quality;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.order_layout);
            orderImg = itemView.findViewById(R.id.order_img);
            name = itemView.findViewById(R.id.order_name);
            date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.order_total_price);
            quality = itemView.findViewById(R.id.order_quantity);

        }
    }
}
