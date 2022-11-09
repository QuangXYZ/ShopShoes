package com.example.shopshoes.Admin;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Activity.OrderDetailActivity;
import com.example.shopshoes.Activity.ProductDetailActivity;
import com.example.shopshoes.Adapter.OrderAdapter;
import com.example.shopshoes.Adapter.OrderProductDetailAdapter;
import com.example.shopshoes.Adapter.ProductsAdapter;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class statisticActivity extends AppCompatActivity {
    private TextView totalProductSell,totalPrice,totalCustomer,totalOrder,totalOrderPrice,totalOrderFail,totalProductWarehouse,totalWarehousePrice;
    DatabaseReference myRootRef;
    private ArrayList<Order> orderArrayList;
    private ImageView backbtn;
    private ArrayList<Product> productArrayList;
    private ArrayList<Product> productBestSellerArrayList;
    private OrderProductDetailAdapter mAdapter;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        initAll();
    }
    private void initAll(){
        orderArrayList =new ArrayList<Order>();
        productArrayList =new ArrayList<Product>();
        productBestSellerArrayList =new ArrayList<Product>();


        totalProductSell = findViewById(R.id.statistic_total_product);
        totalPrice = findViewById(R.id.statistic_total_price);
        totalCustomer = findViewById(R.id.statistic_total_user);
        totalOrder = findViewById(R.id.statistic_total_order);
        totalOrderPrice = findViewById(R.id.statistic_total_order_price);
        totalOrderFail = findViewById(R.id.statistic_total_order_fail);
        totalProductWarehouse = findViewById(R.id.statistic_total_warehouse);
        totalWarehousePrice = findViewById(R.id.statistic_total_warehouse_price);
        backbtn = findViewById(R.id.statistic_back_btn);
        recyclerView = findViewById(R.id.statistic_list_product_sell);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new OrderProductDetailAdapter(productBestSellerArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAdminOrders();


    }
    public void getAdminOrders() {
        myRootRef = FirebaseDatabase.getInstance().getReference();
        myRootRef.child("Order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String id = child.getKey();
                        totalCustomer.setText(String.valueOf(Integer.parseInt(totalCustomer.getText().toString())+1));
                        getDataFromFirebase(id);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDataFromFirebase(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Order").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()){
                    Order order = post.getValue(Order.class);
                    totalOrder.setText(String.valueOf(Integer.parseInt(totalOrder.getText().toString())+1));
                    totalOrderPrice.setText(String.valueOf(Double.parseDouble(totalOrderPrice.getText().toString())+order.getTotalPrice()));
                    totalPrice.setText(totalOrderPrice.getText());
                    orderArrayList.add(order);
                    for (Product p : order.getProductArrayList())  totalProductSell.setText(String.valueOf(Integer.parseInt(totalProductSell.getText().toString())+p.getQuantityInCart()));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(statisticActivity.this,"Error", Toast.LENGTH_LONG).show();
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(FirebaseFireStoreConstants.PRODUCTS);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            for (QueryDocumentSnapshot document : snapshot) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Product product = new Product();
                                product = document.toObject(Product.class);
                                productArrayList.add(product);
                            }
                            int s = productArrayList.size();
                            for (int i=0;i<5&&i<s ;i++){
                                int max =0;
                                int index=0;
                                for (int j=0;j<productArrayList.size();j++){
                                    if (productArrayList.get(j).getSold()>max){
                                        max = productArrayList.get(j).getSold();
                                        index = j;
                                    }
                                }
                                productBestSellerArrayList.add(productArrayList.get(index));
                                productArrayList.remove(index);
                            }
                            Toast.makeText(statisticActivity.this,productBestSellerArrayList.get(0).toString(), Toast.LENGTH_LONG).show();

                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }
    void goBack(){
        finish();
    }

}