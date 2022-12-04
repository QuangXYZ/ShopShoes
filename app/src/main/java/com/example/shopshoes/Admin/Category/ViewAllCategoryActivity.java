package com.example.shopshoes.Admin.Category;

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

import com.example.shopshoes.Adapter.CategoryAdapter;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
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

public class ViewAllCategoryActivity extends AppCompatActivity {

    private CategoryAdapter mCategoryAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Category> categoryArrayList;

    private ProgressBar progressBar;
    private TextView noCategory;
    private EditText nameInput;
    private FirebaseFirestore db;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_category);

        categoryArrayList = new ArrayList<Category>();
        recyclerView = findViewById(R.id.category_recyclerview);
        progressBar = findViewById(R.id.progress_bar_category);
        noCategory = findViewById(R.id.no_category);
        nameInput = findViewById(R.id.name_input);
        db = FirebaseFirestore.getInstance();
        category = new Category();

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
                    if (categoryArrayList.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noCategory.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noCategory.setVisibility(View.VISIBLE);
                    }

                    mCategoryAdapter = new CategoryAdapter(ViewAllCategoryActivity.this, categoryArrayList);
                    recyclerView.setAdapter(mCategoryAdapter);
                    mCategoryAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<Category> clone = new ArrayList<>();
                    for (Category element : categoryArrayList) {
                        if (element.getCategoryName().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if (clone.size() != 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noCategory.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noCategory.setVisibility(View.VISIBLE);
                    }

                    mCategoryAdapter = new CategoryAdapter(ViewAllCategoryActivity.this, clone);
                    recyclerView.setAdapter(mCategoryAdapter);
                    mCategoryAdapter.notifyDataSetChanged();
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
        CollectionReference reference = db.collection(FirebaseFireStoreConstants.CATEGORY);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) { // khi hoàn thành việt get dữ liệu trả về nằm trong task
                        if (task.isSuccessful()) { //task lấy về thành công
                            QuerySnapshot snapshot = task.getResult(); // QuerySnapshot là danh sách toàn bộ các document
                            for (QueryDocumentSnapshot document : snapshot) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                category = document.toObject(Category.class);// chuyển đổi document thành 1 đối tượng category
                                categoryArrayList.add(category); //đưa vào arraylist
                                counter[0]++;
                                if (counter[0] == task.getResult().size()) {
                                    setData();
                                    progressBar.setVisibility(View.GONE);
                                }
                                Log.d("ShowEventInfo:", category.toString());
                            }
                        } else {
                            noCategory.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(ViewAllCategoryActivity.this, "Error" + task.getException(), LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setData() {
        progressBar.setVisibility(View.GONE);
        if (categoryArrayList.size() > 0) {
            mCategoryAdapter = new CategoryAdapter(ViewAllCategoryActivity.this, categoryArrayList);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(ViewAllCategoryActivity.this));
            recyclerView.setAdapter(mCategoryAdapter);
            mCategoryAdapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
            noCategory.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noCategory.setVisibility(View.VISIBLE);
        }
    }

    public void goBack(View view) {
        finish();
    }
}