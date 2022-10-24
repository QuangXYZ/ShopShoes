package com.example.shopshoes.Admin;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopshoes.Adapter.ProductsAdapter;
import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.Model.Utils;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.shopshoes.databinding.ActivityUpdateProductBinding;
import com.squareup.picasso.Picasso;

public class UpdateProductActivity extends AppCompatActivity {
    ActivityUpdateProductBinding binding;
    String[] categoriesList = {"Select Category", "sandal", "Shoes"};
    String[] brandsList = {"Select Brand Name", "Nike", "Adidas"};
    String[] sizeTypeList = {"Select Size Type", "male", "female"};
    String[] sizeList = {"Select Size", "34-35", "35", "35-36", "36", "36-37", "37", "37-38", "38", "38-39", "39", "39-40", "40", "40-41", "41", "41-42", "42", "42-43", "43", "43-44", "44", "44-45", "45", "46", "47", "48", "49"};
    Spinner categorySpinner, brandSpinner, sizeTypeSpinner, sizeSpinner;
    String category = "";
    String brand = "";
    String sizeType = "";
    String size = "";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath = null;
    StorageReference storageRef;
    private FirebaseFirestore db;
    ImageView uploadPhotoBtn, productImg;
    Button updateBtn, deleteBtn;
    private String downloadImageUrl = "";
    private EditText nameEt, priceEt, colorEt, stockEt, descriptionEt;
    private ProgressBar progressBar;
    Product product, productEdit;
    private ProgressDialog loader;
    private CollectionReference colRefProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        colRefProducts = db.collection(FirebaseFireStoreConstants.PRODUCTS);
        getProduct();

        binding = ActivityUpdateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAll();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(UpdateProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        Log.d("UpdateProduct", "category" + product.getCategory());
        if (product.getCategory() != null) {
//            categorySpinner.setSelection(getSpinnerIndex(categorySpinner, product.getCategory()));
            categorySpinner.setSelection(1);
        }

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(UpdateProductActivity.this, android.R.layout.simple_list_item_1, brandsList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<String> sizeTypeAdapter = new ArrayAdapter<String>(UpdateProductActivity.this, android.R.layout.simple_list_item_1, sizeTypeList);
        sizeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeTypeSpinner.setAdapter(sizeTypeAdapter);

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(UpdateProductActivity.this, android.R.layout.simple_list_item_1, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        SettingClickListners();


        if (product.getPhotoUrl() != null) {
            if (!product.getPhotoUrl().equals("")) {
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.icon).into(productImg);
            }
        }

        binding.productNameUpdate.setText(product.getName());
//        Log.d("logCategory:", product.getCategory());


//        binding.productBrandSpinnerUpdate.setSelection(product.getBrand());

    }

    private void getProduct() {
        final String productId = (String) getIntent().getSerializableExtra("productId");
        colRefProducts.document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("GetProduct", "Product category " + productId);
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    product = documentSnapshot.toObject(Product.class);
                    Log.d(TAG, "onSuccess: product name" + product.getName());
                    Log.d(TAG, "onSuccess: CategoryTest: " + product.getCategory());
                } else {
                    Log.d(TAG, "onSuccess: product null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("addOnFailureListener", e.getMessage());
            }
        });


    }

    private void SettingClickListners() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brand = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sizeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sizeType = String.valueOf(parent.getItemAtPosition(position));
                if (sizeType.equals("")) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        uploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

            }
        });
    }

    private void initAll() {
        categorySpinner = findViewById(R.id.product_category_Spinner_update);
        brandSpinner = findViewById(R.id.product_brand_Spinner_update);
        sizeTypeSpinner = findViewById(R.id.product_size_type_Spinner_update);
        sizeSpinner = findViewById(R.id.product_size_Spinner_update);
        progressBar = findViewById(R.id.progress_bar_update);

        uploadPhotoBtn = findViewById(R.id.upload_image_btn_update);
        productImg = findViewById(R.id.product_image_update);
        updateBtn = findViewById(R.id.btn_update);
        deleteBtn = findViewById(R.id.btn_delete);

        nameEt = findViewById(R.id.product_name_update);
        priceEt = findViewById(R.id.price_edt_update);
        colorEt = findViewById(R.id.color_edt_update);
        stockEt = findViewById(R.id.stock_edt_update);
        descriptionEt = findViewById(R.id.description_edt_update);

        storageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        product = new Product();
        Utils.statusBarColor(UpdateProductActivity.this);
    }

    public void goBack(View view) {
        finish();
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }

        return 0;
    }
}
//https://github.dev/jirawatee/CloudFirestore-Android