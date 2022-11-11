package com.example.shopshoes.Activity;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shopshoes.Adapter.BrandAdapter;
import com.example.shopshoes.Adapter.FilterBrandAdapter;
import com.example.shopshoes.Admin.Brand.ViewAllBrandActivity;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class FilterSearchActivity extends AppCompatActivity {
    private FilterBrandAdapter mBrandAdapter;
    private RecyclerView brandRecycler;
    private ArrayList<Brand> brandArrayList;
    private Brand brand;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        initAll();
    }
    private void initAll(){
        brandArrayList = new ArrayList<Brand>();
        brandRecycler = findViewById(R.id.brand_list);
        db = FirebaseFirestore.getInstance();
        getDataFromFirebase();
    }
    public void getDataFromFirebase() {
        final int[] counter = {0};
        CollectionReference reference = db.collection(FirebaseFireStoreConstants.BRAND);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            for (QueryDocumentSnapshot document : snapshot) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                brand = document.toObject(Brand.class);
                                brandArrayList.add(brand);
                                counter[0]++;
                                if (counter[0] == task.getResult().size()) {
                                    setData();

                                }
                                Log.d("ShowEventInfo:", brand.toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(FilterSearchActivity.this, "Error" + task.getException(), LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void setData() {
        if (brandArrayList.size() > 0) {
            mBrandAdapter = new FilterBrandAdapter(brandArrayList,FilterSearchActivity.this);
            brandRecycler.setNestedScrollingEnabled(false);
            brandRecycler.setLayoutManager(new LinearLayoutManager(FilterSearchActivity.this,RecyclerView.HORIZONTAL,false));
            brandRecycler.setAdapter(mBrandAdapter);
            mBrandAdapter.notifyDataSetChanged();

            brandRecycler.setVisibility(View.VISIBLE);
        } else {

        }
    }
    public void goBack(View view) {
        finish();
    }
}