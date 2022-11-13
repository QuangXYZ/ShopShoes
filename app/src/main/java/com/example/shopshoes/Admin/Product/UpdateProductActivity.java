package com.example.shopshoes.Admin.Product;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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

import com.example.shopshoes.Constants.FirebaseFireStoreConstants;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.Model.Utils;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

public class UpdateProductActivity extends AppCompatActivity {
//    ActivityUpdateProductBinding binding;
    String[] categoriesList = {"", "Dép", "Giày"};
    String[] brandsList = {"", "Nike", "Adidas"};
    String[] sizeTypeList = {"", "Nam", "Nữ"};
    String[] sizeList = {"", "34-35", "35", "35-36", "36", "36-37", "37", "37-38", "38", "38-39", "39", "39-40", "40", "40-41", "41", "41-42", "42", "42-43", "43", "43-44", "44", "44-45", "45", "46", "47", "48", "49"};
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
        setContentView(R.layout.activity_update_product);
        db = FirebaseFirestore.getInstance();
        colRefProducts = db.collection(FirebaseFireStoreConstants.PRODUCTS);
//        getProduct();

//        binding = ActivityUpdateProductBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

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

        product = (Product) getIntent().getSerializableExtra("product");

        if (product.getPhotoUrl() != null) {
            if (!product.getPhotoUrl().equals("")) {
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.icon).into(productImg);
            }
        }

        nameEt.setText(product.getName());
        colorEt.setText(product.getColor());
        priceEt.setText(String.valueOf(product.getPrice()));
        stockEt.setText(String.valueOf(product.getStock()));
        descriptionEt.setText(product.getDescription());

//        binding.productNameUpdate.setText(product.getName());
//        categorySpinner.setSelection(getSpinnerIndex(categorySpinner, product.getCategory()));
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEt.getText().toString().trim();
                String price = priceEt.getText().toString().trim();
                String color = colorEt.getText().toString().trim();
                String stock = stockEt.getText().toString().trim();
                String desc = descriptionEt.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameEt.setError("Nhập tên sản phẩm");
                    nameEt.requestFocus();
                } else if (category.equals("")) {
                    Toast.makeText(UpdateProductActivity.this, "Chưa chọn thể loại", Toast.LENGTH_SHORT).show();
                } else if (brand.equals("")) {
                    Toast.makeText(UpdateProductActivity.this, "Chưa chọn thương hiệu ", Toast.LENGTH_SHORT).show();
                } else if (sizeType.equals("")) {
                    Toast.makeText(UpdateProductActivity.this, "Chưa chọn loại size", Toast.LENGTH_SHORT).show();
                } else if (size.equals("")) {
                    Toast.makeText(UpdateProductActivity.this, "Chưa chọn size", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(price)) {
                    priceEt.setError("Nhập giá sản phẩm");
                    priceEt.requestFocus();
                } else if (TextUtils.isEmpty(color)) {
                    colorEt.setError("Nhập màu sản phẩm");
                    colorEt.requestFocus();
                } else if (TextUtils.isEmpty(stock)) {
                    stockEt.setError("Nhập số lượng sản phẩm");
                    stockEt.requestFocus();
                } else if (TextUtils.isEmpty(desc)) {
                    descriptionEt.setError("Nhập nội dung");
                    descriptionEt.requestFocus();
                } else {

                    product.setName(name);
                    product.setCategory(category);
                    product.setBrand(brand);
                    product.setSizeType(sizeType);
                    product.setSize(size);
                    product.setPrice(Integer.parseInt(price));
                    product.setColor(color);
                    product.setStock(Integer.parseInt(stock));
                    product.setDescription(desc);
//                    updateDataProduct();
                    UploadImage();
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
                builder.setTitle("Bạn có chắc chắn về điều này?");
                builder.setMessage("Xóa vĩnh viễn");
                builder.setPositiveButton("xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProduct();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(filePath));
                productImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }

        return 0;
    }

    private void updateDataProduct() {
        final String docsID = (String) getIntent().getSerializableExtra("productId");
        db.collection(FirebaseFireStoreConstants.PRODUCTS).document(docsID)
                .update(
                        "name", product.getName(),
                        "category", product.getCategory(),
                        "brand", product.getBrand(),
                        "sizeType", product.getSizeType(),
                        "size", product.getSize(),
                        "price", product.getPrice(),
                        "color", product.getColor(),
                        "stock", product.getStock(),
                        "description", product.getDescription(),
                        "photoUrl", product.getPhotoUrl()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProductActivity.this, "Sửa thông tin thành công", LENGTH_SHORT);
                    }
                });

        startActivity(new Intent(UpdateProductActivity.this, ViewAllProductsActivity.class));
    }

    private void UploadImage() {
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference childRef = storageRef.child("product_images").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(UpdateProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UpdateProductActivity.this, "Đã tải ảnh...", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageUrl = childRef.getDownloadUrl().toString();
                            return childRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();
                                Log.d("imagUrl", downloadImageUrl);
                                product.setPhotoUrl(downloadImageUrl);
                                updateDataProduct();
                            }
                        }
                    });
                }
            });

        } else {
            Toast.makeText(UpdateProductActivity.this, "Chọn ảnh mới", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct() {
        final String docsID = (String) getIntent().getSerializableExtra("productId");
        db.collection(FirebaseFireStoreConstants.PRODUCTS).document(docsID).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        Toast.makeText(UpdateProductActivity.this, "Xóa thành công", LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(UpdateProductActivity.this, ViewAllProductsActivity.class));
                    }
                });
    }
}
//https://github.dev/jirawatee/CloudFirestore-Android

