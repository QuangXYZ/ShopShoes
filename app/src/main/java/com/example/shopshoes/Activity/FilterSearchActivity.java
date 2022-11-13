package com.example.shopshoes.Activity;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.BrandAdapter;
import com.example.shopshoes.Adapter.FilterBrandAdapter;
import com.example.shopshoes.Adapter.FilterCategoryAdapter;
import com.example.shopshoes.Admin.Brand.ViewAllBrandActivity;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Fragment.FragmentHome;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.Model.Category;
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
    private FilterCategoryAdapter mCategoryAdapter;
    private RecyclerView categoryRecycler;
    private ArrayList<Category> categoryArrayList;
    private Category category;
    private FirebaseFirestore db;
    public static ArrayList<Brand> Clickbrand = new ArrayList<>();
    public static ArrayList<Category> Clickcategory = new ArrayList<>();
    private RelativeLayout filterApply;
    public static TextView clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        initAll();
        filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Brand",Clickbrand);
                intent.putExtra("Category",Clickcategory);
                setResult(MainActivity.RESULT_OK,intent);
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clickbrand.clear();
                Clickcategory.clear();
                Toast.makeText(FilterSearchActivity.this, "CLEAR", LENGTH_SHORT).show();
                getDataFromFirebase();
            }
        });
    }
    private void initAll(){
        brandArrayList = new ArrayList<Brand>();
        categoryArrayList = new ArrayList<Category>();
        brandRecycler = findViewById(R.id.brand_list);
        categoryRecycler= findViewById(R.id.category_list);
        db = FirebaseFirestore.getInstance();
        filterApply = findViewById(R.id.filter_apply);
        clear = findViewById(R.id.id_clear_btn);
        getDataFromFirebase();
    }
    public void getDataFromFirebase() {
        final int[] counter = {0};
        brandArrayList.clear();
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


        //get category
        final int[] counterCategory = {0};
        categoryArrayList.clear();
        CollectionReference referenceCategoty = db.collection(FirebaseFireStoreConstants.CATEGORY);
        referenceCategoty.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = task.getResult();
                            for (QueryDocumentSnapshot document : snapshot) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                category = document.toObject(Category.class);
                                categoryArrayList.add(category);
                                counterCategory[0]++;
                                if (counterCategory[0] == task.getResult().size()) {
                                    setData();

                                }

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
            brandRecycler.setLayoutManager(new GridLayoutManager(this, 3));
            brandRecycler.setAdapter(mBrandAdapter);
            mBrandAdapter.notifyDataSetChanged();

            brandRecycler.setVisibility(View.VISIBLE);
        }
        if (categoryArrayList.size() > 0) {
            mCategoryAdapter = new FilterCategoryAdapter(categoryArrayList,FilterSearchActivity.this);
            categoryRecycler.setNestedScrollingEnabled(false);
            categoryRecycler.setLayoutManager(new GridLayoutManager(this, 3));
            categoryRecycler.setAdapter(mCategoryAdapter);
            mCategoryAdapter.notifyDataSetChanged();

            categoryRecycler.setVisibility(View.VISIBLE);
        }
    }
    public void goBack(View view) {
        finish();
    }
}