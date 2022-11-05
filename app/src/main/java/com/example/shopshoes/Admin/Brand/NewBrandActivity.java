package com.example.shopshoes.Admin.Brand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NewBrandActivity extends AppCompatActivity {

    private StorageReference storageRef;
    private FirebaseFirestore db;
    private Button addBtnBrand;
    private EditText idBrand, nameBrand;
    private ProgressBar progressBar;
    private Brand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_brand);

        initAll();
        settingClickListners();
    }

    private void initAll() {
        idBrand = findViewById(R.id.brand_id);
        nameBrand = findViewById(R.id.brand_name);
        progressBar = findViewById(R.id.progress_bar_brand);
        addBtnBrand = findViewById(R.id.add_btn_brand);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        brand = new Brand();
    }

    private void settingClickListners() {
        addBtnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameBrand.getText().toString().trim();
                String id = idBrand.getText().toString().trim();

                if(TextUtils.isEmpty(id)) {
                    idBrand.setError("Nhập mã thể loại");
                    idBrand.requestFocus();
                } else if(TextUtils.isEmpty(name)) {
                    nameBrand.setError("Nhập tên thể loại");
                    nameBrand.requestFocus();
                } else {
                    brand.setBrandId(id);
                    brand.setBrandName(name);
                    saveInfoDatabaseCategory();
                }
            }
        });
    }

    private void saveInfoDatabaseCategory() {
        progressBar.setVisibility(View.VISIBLE);
        String ID = idBrand.getText().toString().trim();
        DocumentReference docIdRef = db.collection(FirebaseFireStoreConstants.BRAND).document(ID);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(NewBrandActivity.this, "Thương hiệu đã có", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        db.collection(FirebaseFireStoreConstants.BRAND).document(ID).set(brand);
                        Toast.makeText(NewBrandActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewBrandActivity.this, ViewAllBrandActivity.class));
                    }
                } else {
                    Toast.makeText(NewBrandActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBack(View view) {
        finish();
    }
}