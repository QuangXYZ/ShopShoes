package com.example.shopshoes.Admin.Brand;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Brand;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateBrandActivity extends AppCompatActivity {

    private Button updateBtn, deleteBtn;
    private EditText nameBrandEdt;
    private TextView idBrandTv;
    private ProgressBar progressBar;
    private Brand brand;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_brand);

        initAll();

        SettingClickListners();

        brand = (Brand) getIntent().getSerializableExtra("brand");
        idBrandTv.setText(brand.getBrandId());
        nameBrandEdt.setText(brand.getBrandName());
    }

    private void initAll() {
        updateBtn = findViewById(R.id.update_btn_brand);
        deleteBtn = findViewById(R.id.delete_btn_brand);
        idBrandTv = findViewById(R.id.brand_id_update);
        nameBrandEdt = findViewById(R.id.brand_name_update);
        progressBar = findViewById(R.id.progress_bar_brand_update);

        brand = new Brand();
        db = FirebaseFirestore.getInstance();
    }

    private void SettingClickListners() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameBrandEdt.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameBrandEdt.setError("Nhập tên thể loại");
                    nameBrandEdt.requestFocus();
                } else {
                    brand.setBrandName(name);
                    UpdateDataCategory();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBrandActivity.this);
                builder.setTitle("Bạn có chắc chắn về điều này?");
                builder.setMessage("Xóa vĩnh viễn");
                builder.setPositiveButton("xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCategory();
                    }
                });

                builder.setNegativeButton("không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.show();
            }
        });
    }

    public void goBack(View view) {
        finish();
    }

    private void UpdateDataCategory() {
        progressBar.setVisibility(View.VISIBLE);
        final String docsID = (String) getIntent().getSerializableExtra("brandId");
        db.collection(FirebaseFireStoreConstants.BRAND).document(docsID)
                .update("brandId", brand.getBrandId(),
                        "brandName", brand.getBrandName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateBrandActivity.this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        startActivity(new Intent(UpdateBrandActivity.this, ViewAllBrandActivity.class));
    }

    private void deleteCategory() {
        final String docsID = (String) getIntent().getSerializableExtra("brandId");
        db.collection(FirebaseFireStoreConstants.BRAND).document(docsID).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateBrandActivity.this, "Xóa thành công", LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(UpdateBrandActivity.this, ViewAllBrandActivity.class));
                    }
                });
    }

}
