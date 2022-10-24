package com.example.shopshoes.Admin;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopshoes.Adapter.ProductsAdapter;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.Model.Utils;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewAllProductsActivity extends AppCompatActivity {

    private ProductsAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Product> productArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private EditText nameInput;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);


        productArrayList =new ArrayList<Product>();
        recyclerView =findViewById(R.id.product_list);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_product);
        nameInput = findViewById(R.id.name_input);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        Utils.statusBarColor(ViewAllProductsActivity.this);

        mAdapter = new ProductsAdapter(productArrayList, ViewAllProductsActivity.this,true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();




        getDataFromFirebase();


        searchFunc();
    }
    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if(productArrayList.size()!=0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new ProductsAdapter(productArrayList,ViewAllProductsActivity.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Product> clone = new ArrayList<>();
                    for (Product element : productArrayList) {
                        if (element.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if(clone.size()!=0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new ProductsAdapter(clone,ViewAllProductsActivity.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
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
                                counter[0]++;
                                if (counter[0] == task.getResult().size()) {
                                    setData();
                                    progressBar.setVisibility(View.GONE);
                                }
                                Log.d("ShowEventInfo:", product.toString());
                            }
                        } else {
                            noJokeText.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ViewAllProductsActivity.this, "Error" + task.getException() , LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setData() {
        if(productArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void goBack(View view) {
        finish();
    }
}