package com.example.shopshoes.Admin.Oder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Activity.OrderDetailActivity;
import com.example.shopshoes.Adapter.OrderProductDetailAdapter;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerOderDetailActivity extends AppCompatActivity {

    private OrderProductDetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> productArrayList;
    private Order order;
    DatabaseReference myRootRef;
    private RelativeLayout confirmStatus;
    private ImageView img;


    private TextView orderID,orderPrice,orderstatus,orderDate,orderQuantity,address,comment;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_oder_detail);
        initAll();
        confirmStatus();
    }
    private void initAll(){
        orderID = findViewById(R.id.orderID);
        orderstatus = findViewById(R.id.order_detail_status);
        orderDate = findViewById(R.id.order_detail_date);
        orderQuantity = findViewById(R.id.order_detail_quantity);
        orderPrice = findViewById(R.id.order_detail_total_price);
        recyclerView = findViewById(R.id.product_list_order);
        address = findViewById(R.id.order_address_view);
        comment = findViewById(R.id.order_comment_view);
        recyclerView = findViewById(R.id.product_list_order);
        confirmStatus = findViewById(R.id.order_confirm_status);
        img = findViewById(R.id.order_back);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        ID = getIntent().getExtras().getString("id");
        Log.d("ShowId", ID);
        getAdminOrders();
    }

    public void getAdminOrders() {
//        progressBar.setVisibility(View.VISIBLE);
        myRootRef.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String id=child.getKey();
                        getOrderFromFirebase(id);
                    }
                } else {
//                    noOder.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CustomerOderDetailActivity.this, "k có sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(CustomerOderDetailActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getOrderFromFirebase(String id){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Order").child(id).child(ID);
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
                    orderPrice.setText(String.valueOf(order.getTotalPrice()+10000));
                    address.setText(order.getAddress());
                    comment.setText(order.getComments());
                }
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(CustomerOderDetailActivity.this,DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
                mAdapter = new OrderProductDetailAdapter(productArrayList,CustomerOderDetailActivity.this);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOderDetailActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerOderDetailActivity.this,"Error", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void confirmStatus() {
        confirmStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HashMap status = new HashMap();
//                String setStatus = "Đơn đã xác nhận";
//                status.put("status",setStatus);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("Order").child("Ru1ipgzBcUcsO0YCvkmenii0mZs1").child("-NG1Kb5ftg8KGux1nher");

                Order order = new Order();
                order.setStatus("Đơn đã xác nhận");

                databaseReference.setValue(order, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(CustomerOderDetailActivity.this, "Xác nhận đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}