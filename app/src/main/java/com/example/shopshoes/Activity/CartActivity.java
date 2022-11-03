package com.example.shopshoes.Activity;


import android.content.Intent;
import android.os.Bundle;

import com.example.shopshoes.Adapter.CartCustomAdapter;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    public static TextView totalPriceView;
    private ImageView deleteCart,cartBackArrow;
    CardView checkOut;
    private Order order;
    private RecyclerView cartRecyclerView;
    private CartCustomAdapter cartCustomAdapter;
    private ArrayList<Product> productArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initAll();
        clickListener();
    }



    private void clickListener() {
        deleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.getCartProductList().clear();
                order.setTotalPrice(0);
                productArrayList.clear();
                updateOrderToFirebase();
                totalPriceView.setText("0 VND");
                cartCustomAdapter.notifyDataSetChanged();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(order.getTotalPrice()>0){
                    Intent intent=new Intent(CartActivity.this,CheckOutActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CartActivity.this, "Không có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cartBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAll() {
        order=new Order();
        productArrayList =new ArrayList<>();
        totalPriceView=findViewById(R.id.cart_total_price_view);
        deleteCart=findViewById(R.id.delete_cart_imageView);
        cartBackArrow=findViewById(R.id.cart_back_arrow);

        getOrderFormFirebase();


        // Double price=Double.toString());
        //totalPriceView.setText(new DecimalFormat("##.##").format(order.getTotalPrice()));
        
//        cartRecyclerView=findViewById(R.id.cart_order_recyclerview);
//        cartCustomAdapter=new CartCustomAdapter(CartActivity.this, productArrayList,order);
//        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        cartRecyclerView.setNestedScrollingEnabled(false);
//        cartRecyclerView.setAdapter(cartCustomAdapter);
        checkOut=findViewById(R.id.check_out_btn);


    }
    private void getOrderFormFirebase(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Cart").child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    order = snapshot.getValue(Order.class);
                    productArrayList = (ArrayList<Product>) order.getProductArrayList().clone();
                    cartRecyclerView=findViewById(R.id.cart_order_recyclerview);
                    cartCustomAdapter=new CartCustomAdapter(CartActivity.this, productArrayList,order);
                    cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    cartRecyclerView.setNestedScrollingEnabled(false);
                    cartRecyclerView.setAdapter(cartCustomAdapter);
                    totalPriceView.setText(String.valueOf(order.getTotalPrice())+ " VND");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this,"Error", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void updateOrderToFirebase(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();;
        myRootRef.child("Cart").child(currentUserId).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}