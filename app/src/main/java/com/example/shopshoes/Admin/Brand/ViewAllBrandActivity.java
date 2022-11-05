package com.example.shopshoes.Admin.Brand;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.BrandAdapter;
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

public class ViewAllBrandActivity extends AppCompatActivity {

    private BrandAdapter mBrandAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Brand> brandArrayList;

    private ProgressBar progressBar;
    private TextView noBranch;
    private EditText nameInput;
    private FirebaseFirestore db;
    private Brand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_brand);

        brandArrayList = new ArrayList<Brand>();
        recyclerView = findViewById(R.id.brand_recyclerview);
        progressBar = findViewById(R.id.progress_bar_brand);
        noBranch = findViewById(R.id.no_brand);
        nameInput = findViewById(R.id.name_input);
        db = FirebaseFirestore.getInstance();
        brand = new Brand();

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
                    if (brandArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noBranch.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noBranch.setVisibility(View.VISIBLE);
                    }

                    mBrandAdapter = new BrandAdapter(ViewAllBrandActivity.this, brandArrayList);
                    recyclerView.setAdapter(mBrandAdapter);
                    mBrandAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Brand> clone = new ArrayList<>();
                    for (Brand element : brandArrayList) {
                        if (element.getBrandName().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if (clone.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noBranch.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noBranch.setVisibility(View.VISIBLE);
                    }

                    mBrandAdapter = new BrandAdapter(ViewAllBrandActivity.this, clone);
                    recyclerView.setAdapter(mBrandAdapter);
                    mBrandAdapter.notifyDataSetChanged();
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
                                    progressBar.setVisibility(View.GONE);
                                }
                                Log.d("ShowEventInfo:", brand.toString());
                            }
                        } else {
                            noBranch.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ViewAllBrandActivity.this, "Error" + task.getException(), LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setData() {
        progressBar.setVisibility(View.GONE);
        if (brandArrayList.size() > 0) {
            mBrandAdapter = new BrandAdapter(ViewAllBrandActivity.this, brandArrayList);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(ViewAllBrandActivity.this));
            recyclerView.setAdapter(mBrandAdapter);
            mBrandAdapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
            noBranch.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noBranch.setVisibility(View.VISIBLE);
        }
    }

    public void goBack(View view) {
        finish();
    }
}