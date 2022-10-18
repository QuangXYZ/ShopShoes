package com.android.shoppingzoo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.android.shoppingzoo.Model.Product;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewProductActivity extends AppCompatActivity {
    String[] categoriesList = {"Select Category", "Baby", "Kids", "Men", "Women"};
    String[] brandsList = {"Select Brand Name", "Nike", "Adidas", "Levi’s", "Jordan"};
    String[] sizeTypeList = {"Select Size Type", "Regular", "Plus", "Juniors", "Tall"};
    String[] sizeList = {"Select Size", "M", "XL", "S", "2XL"};
    Spinner categorySpinner, brandSpinner, sizeTypeSpinner, sizeSpinner;
    String category = "";
    String brand = "";
    String sizeType = "";
    String size = "";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath=null;
    StorageReference storageRef;
    DatabaseReference myRootRef;
    ImageView uploadPhotoBtn, productImg;
    Button addBtn;
    private String downloadImageUrl = "";
    private EditText nameEt,priceEt,colorEt,stockEt,descriptionEt;
    private ProgressBar progressBar;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        initAll();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, brandsList);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);

        ArrayAdapter<String> sizeTypeAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, sizeTypeList);
        sizeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeTypeSpinner.setAdapter(sizeTypeAdapter);

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        SettingClickListners();


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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEt.getText().toString().trim();
                String price=priceEt.getText().toString().trim();
                String color=colorEt.getText().toString().trim();
                String stock=stockEt.getText().toString().trim();
                String desc=descriptionEt.getText().toString().trim();
                if(filePath==null){
                    Toast.makeText(NewProductActivity.this, "Please select product image", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(name)){
                    nameEt.setError("Enter product name");
                    nameEt.requestFocus();
                }
                else if(category.equals("Select Category")){
                    Toast.makeText(NewProductActivity.this, "Select category", Toast.LENGTH_SHORT).show();
                }
                else if(brand.equals("Select Brand Name")){
                    Toast.makeText(NewProductActivity.this, "Select brand", Toast.LENGTH_SHORT).show();
                }
                else if(sizeType.equals("Select Size Type")){
                    Toast.makeText(NewProductActivity.this, "Select size", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(price)){
                    priceEt.setError("Enter product price");
                    priceEt.requestFocus();
                }
                else if(TextUtils.isEmpty(color)){
                    colorEt.setError("Enter product color");
                    colorEt.requestFocus();
                }
                else if(TextUtils.isEmpty(stock)){
                    stockEt.setError("Enter product stock");
                    stockEt.requestFocus();
                }
                else if(TextUtils.isEmpty(desc)){
                    descriptionEt.setError("Enter product description");
                    descriptionEt.requestFocus();
                }
                else{
                    product.setName(name);
                    product.setCategory(category);
                    product.setBrand(brand);
                    product.setSizeType(sizeType);
                    product.setPrice(Double.parseDouble(price));
                    product.setColor(color);
                    product.setStock(stock);
                    product.setDescription(desc);
                    UploadImage();

                }

            }
        });
    }

    private void initAll() {
        categorySpinner = findViewById(R.id.product_category_Spinner);
        brandSpinner = findViewById(R.id.product_brand_Spinner);
        sizeTypeSpinner = findViewById(R.id.product_size_type_Spinner);
        sizeSpinner = findViewById(R.id.product_size_Spinner);
        progressBar = findViewById(R.id.progress_bar);

        uploadPhotoBtn = findViewById(R.id.upload_image_btn);
        productImg = findViewById(R.id.product_image);
        addBtn = findViewById(R.id.add_btn);

        nameEt=findViewById(R.id.product_name_et);
        priceEt=findViewById(R.id.price_et);
        colorEt=findViewById(R.id.color_et);
        stockEt=findViewById(R.id.stock_et);
        descriptionEt=findViewById(R.id.description_tv);

        storageRef = FirebaseStorage.getInstance().getReference();
        myRootRef = FirebaseDatabase.getInstance().getReference();
        product=new Product();
        Utils.statusBarColor(NewProductActivity.this);
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

    private void UploadImage() {
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference childRef = storageRef.child("product_images").child(System.currentTimeMillis() + ".jpg");
            final UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(NewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(NewProductActivity.this, "Photo uploaded...", Toast.LENGTH_SHORT).show();
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
                                SaveInfoToDatabase();
                            }
                        }
                    });
                }
            });

        } else {
            Toast.makeText(NewProductActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveInfoToDatabase() {
        String key=myRootRef.push().getKey();
//        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Products");
//        String userId = data.push().getKey();
//        data.child(userId).setValue(product);
        product.setProductId(key);
        myRootRef.child("Products").child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NewProductActivity.this, "Product uploaded Successfully", Toast.LENGTH_SHORT).show();
                finish();
                Log.d("TAG", "Saved to firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d("test", e.toString());
            }
        });
    }
}