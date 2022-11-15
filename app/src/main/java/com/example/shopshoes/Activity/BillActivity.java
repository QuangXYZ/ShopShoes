package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.OrderAdapter;
import com.example.shopshoes.Admin.Oder.CustomerOderActivity;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {
    private OrderAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Order> orderArrayList;
    private ArrayList<String> idUser;
    private TextView noOder;
    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        initAll();

    }

    private void initAll() {
        orderArrayList =new ArrayList<Order>();
        recyclerView = findViewById(R.id.customer_order_list);
        progressBar = findViewById(R.id.spin_progress_bar_customer_order);
        noOder = findViewById(R.id.no_customer_order);
        myRootRef = FirebaseDatabase.getInstance().getReference();

        mAdapter = new OrderAdapter(orderArrayList, BillActivity.this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        getDataFromFirebase();
        mAdapter.notifyDataSetChanged();
    }

    public void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Bill").child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderArrayList.clear();
                for (DataSnapshot post : snapshot.getChildren()){
                    Order order = post.getValue(Order.class);
                    orderArrayList.add(order);
                }
                mAdapter.notifyDataSetChanged();
                setData();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noOder.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BillActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setData() {
        if(orderArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noOder.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            noOder.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void goBack(View view) {
        finish();
    }
}