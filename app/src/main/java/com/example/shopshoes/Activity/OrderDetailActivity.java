package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
public class OrderDetailActivity extends AppCompatActivity {
    private OrderProductDetailAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> productArrayList;
    private Order order;
    DatabaseReference myRootRef;
    private RelativeLayout delete;
    private ImageView img;
    private AlertDialog.Builder builder;


    private TextView orderID,orderPrice,orderstatus,orderDate,orderQuantity,address,comment;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
        delete = findViewById(R.id.order_delete);
        img = findViewById(R.id.order_back);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        ID = getIntent().getExtras().getString("orderID");
        getOrderFromFirebase();

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có muốn hủy đơn hàng");

    }
    private void getOrderFromFirebase(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Order").child(currentUserId).child(ID);
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
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(OrderDetailActivity.this,DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
                mAdapter = new OrderProductDetailAdapter(productArrayList,OrderDetailActivity.this);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailActivity.this,"Error", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void OnClickListeners(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Order");
                        databaseReference.child(currentUserId).child(ID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OrderDetailActivity.this,"Hủy đơn hàng thành công", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrderDetailActivity.this,"Hủy đơn không thành công", Toast.LENGTH_LONG).show();

                            }
                        });
                        dialog.dismiss();

                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
}