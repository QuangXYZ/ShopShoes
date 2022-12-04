package com.example.shopshoes.Admin.Category;

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
import com.example.shopshoes.Model.Category;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NewCategoryActivity extends AppCompatActivity {

    private StorageReference storageRef;
    private FirebaseFirestore db;// khai báo
    private Button addBtnCategory;
    private EditText idCategory, nameCategory;
    private ProgressBar progressBar;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        initAll();
        settingClickListners();
    }

    public void goBack(View view) {
        finish();
    }

    private void initAll() {
        idCategory = findViewById(R.id.category_id);
        nameCategory = findViewById(R.id.category_name);
        progressBar = findViewById(R.id.progress_bar);
        addBtnCategory = findViewById(R.id.add_btn_category);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        category = new Category();
    }

    private void settingClickListners() {
        addBtnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameCategory.getText().toString().trim();
                String id = idCategory.getText().toString().trim();

                if(TextUtils.isEmpty(id)) {
                    idCategory.setError("Nhập mã thể loại");
                    idCategory.requestFocus();
                } else if(TextUtils.isEmpty(name)) {
                    nameCategory.setError("Nhập tên thể loại");
                    nameCategory.requestFocus();
                } else {
                    category.setCategoryId(id);
                    category.setCategoryName(name);
                    saveInfoDatabaseCategory();
                }
            }
        });
    }

    private void saveInfoDatabaseCategory() {
        progressBar.setVisibility(View.VISIBLE);
        String ID = idCategory.getText().toString().trim();
        DocumentReference docIdRef = db.collection(FirebaseFireStoreConstants.CATEGORY).document(ID);// DocumentReference dùng để trỏ đến hoặc tạo một collection
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {// dùng hàm get() lấy toàn bộ document
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) { //khi hoàn thành việc get() dữ liệu nằm trong task
                if (task.isSuccessful()){ // nếu task lấy về thành công
                    DocumentSnapshot document = task.getResult(); // task.getResult() trả về document
                    if (document.exists()) {
                        Toast.makeText(NewCategoryActivity.this, "Thể loại đã có", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        db.collection(FirebaseFireStoreConstants.CATEGORY).document(ID).set(category);
                        Toast.makeText(NewCategoryActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewCategoryActivity.this, ViewAllCategoryActivity.class));
                    }
                } else {
                    Toast.makeText(NewCategoryActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}