package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.OrderProductDetailAdapter;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillDetailActivity extends AppCompatActivity {

    private OrderProductDetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> productArrayList;
    private Order order;
    DatabaseReference myRootRef;
    private RelativeLayout delete;
    private ImageView img;
    String currentUserId;



    private TextView orderID,orderPrice,orderstatus,orderDate,orderQuantity,address,comment;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        initAll();
        OnClickListeners();
    }
    private void initAll() {
        orderID = findViewById(R.id.orderID);
        orderstatus = findViewById(R.id.order_detail_status);
        orderDate = findViewById(R.id.order_detail_date);
        orderQuantity = findViewById(R.id.order_detail_quantity);
        orderPrice = findViewById(R.id.order_detail_total_price);
        recyclerView = findViewById(R.id.product_list_order);
        address = findViewById(R.id.order_address_view);
        comment = findViewById(R.id.order_comment_view);
        recyclerView = findViewById(R.id.product_list_order);

        img = findViewById(R.id.order_back);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        if (getIntent().getExtras().getBoolean("isAdmin"))
            currentUserId = getIntent().getExtras().getString("idCustomer");

        else currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ID = getIntent().getExtras().getString("orderID");

        getOrderFromFirebase();


    }
    private void getOrderFromFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Bill").child(currentUserId).child(ID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    order = snapshot.getValue(Order.class);
                    productArrayList = (ArrayList<Product>) order.getProductArrayList().clone();
                    orderID.setText(order.getId());
                    orderstatus.setText(order.getStatus());
                    orderDate.setText(order.getDateOfOrder());
                    orderQuantity.setText(String.valueOf(order.getProductArrayList().size()));
                    orderPrice.setText(String.valueOf((int)order.getTotalPrice()+10000)+" VND");
                    address.setText(order.getAddress());
                    comment.setText(order.getComments());
                }
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(BillDetailActivity.this,DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
                mAdapter = new OrderProductDetailAdapter(productArrayList,BillDetailActivity.this);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(BillDetailActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BillDetailActivity.this,"Error", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void OnClickListeners(){


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
}