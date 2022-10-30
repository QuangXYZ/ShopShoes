package com.example.shopshoes.Admin;

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
import com.example.shopshoes.Model.Category;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateCategoryActivity extends AppCompatActivity {

    private Button updateBtn, deleteBtn;
    private EditText nameCategoryEdt;
    private TextView idCategoryEdt;
    private ProgressBar progressBar;
    private Category category;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        initAll();

        SettingClickListners();

        category = (Category) getIntent().getSerializableExtra("category");
        idCategoryEdt.setText(category.getCategoryId());
        nameCategoryEdt.setText(category.getCategoryName());
    }

    private void initAll() {
        updateBtn = findViewById(R.id.update_btn_category);
        deleteBtn = findViewById(R.id.delete_btn_category);
        idCategoryEdt = findViewById(R.id.category_id_update);
        nameCategoryEdt = findViewById(R.id.category_name_update);
        progressBar = findViewById(R.id.progress_bar_category_update);

        category = new Category();
        db = FirebaseFirestore.getInstance();

    }

    private void SettingClickListners() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idCategoryEdt.getText().toString().trim();
                String name = nameCategoryEdt.getText().toString().trim();

                if (TextUtils.isEmpty(id)) {
                    idCategoryEdt.setError("Nhập mã thể loại");
                    idCategoryEdt.requestFocus();
                } else if (TextUtils.isEmpty(name)) {
                    nameCategoryEdt.setError("Nhập tên thể loại");
                    nameCategoryEdt.requestFocus();
                } else {
                    category.setCategoryId(id);
                    category.setCategoryName(name);
                    UpdateDataCategory();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCategoryActivity.this);
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

    private void UpdateDataCategory() {
        progressBar.setVisibility(View.VISIBLE);
        final String docsID = (String) getIntent().getSerializableExtra("categoryId");
        db.collection(FirebaseFireStoreConstants.CATEGORY).document(docsID)
                .update("categoryId", category.getCategoryId(),
                        "categoryName", category.getCategoryName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateCategoryActivity.this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        startActivity(new Intent(UpdateCategoryActivity.this, ViewAllCategoryActivity.class));
    }

    private void deleteCategory() {
        final String docsID = (String) getIntent().getSerializableExtra("categoryId");
        db.collection(FirebaseFireStoreConstants.CATEGORY).document(docsID).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateCategoryActivity.this, "Xóa thành công", LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(UpdateCategoryActivity.this, ViewAllCategoryActivity.class));
                    }
                });
    }

    public void goBack(View view) {
        finish();
    }
}