//package com.example.shopshoes;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_home);
//
//        // Quang : Hello branch dev
//    }
//}

//package com.example.shopshoes;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.shopshoes.Model.Item;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import org.checkerframework.checker.units.qual.A;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//    TextView tvData;
//    EditText editText, edtQuantity;
//    private ListView listView;
//    private FirebaseFirestore firestore;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editText = findViewById(R.id.edt_data);
//        edtQuantity = findViewById(R.id.edt_quantity);
//        listView = findViewById(R.id.listproduct);
//        Button btnPushData = findViewById(R.id.btn_push_data);
//        tvData = findViewById(R.id.tv_get_data);
//        Button btnGetData = findViewById(R.id.btn_get_data);
//        firestore = FirebaseFirestore.getInstance();
//        final CollectionReference reference = firestore.collection("Products");
//
//        btnPushData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map<String, Object> item = new HashMap<>();
//                item.put("name", editText.getText().toString());
//                item.put("quantity", edtQuantity.getText().toString());
//                reference.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(MainActivity.this, "Add success", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, "Add fail", Toast.LENGTH_SHORT);
//                            }
//                        });
//            }
//        });
//
//        btnGetData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            QuerySnapshot snapshots = task.getResult();
//                            List<Item> list = new ArrayList<>();
//                            for (QueryDocumentSnapshot doc: snapshots) {
//                                Item item = new Item();
//                                item.setName(doc.get("name").toString());
//                                item.setQuantity(Integer.parseInt((String) doc.get("quantity".toString())));
//                                list.add(item);
//                            }
//                            ArrayAdapter<Item> adapter = new ArrayAdapter<>(MainActivity.this,
//                                    android.R.layout.simple_list_item_1, list);
//                            listView.setAdapter(adapter);
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//}

package com.example.shopshoes.Admin.Product;

import android.app.ProgressDialog;
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

import androidx.appcompat.app.AppCompatActivity;


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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends AppCompatActivity {
    //    String[] categoriesList = {"", "Dép", "Giày"};
//    String[] brandsList = {"", "Nike", "Adidas"};
//    String[] sizeTypeList = {"", "Nam", "Nữ"};
    String[] sizeList = {"", "34-35", "35", "35-36", "36", "36-37", "37", "37-38", "38", "38-39", "39", "39-40", "40", "40-41", "41", "41-42", "42", "42-43", "43", "43-44", "44", "44-45", "45", "46", "47", "48", "49"};
    Spinner categorySpinner, brandSpinner, sizeTypeSpinner, sizeSpinner;
    String category = "";
    String brand = "";
    String sizeType = "";
    String size = "";

    int PICK_IMAGE_REQUEST = 111;
    Uri filePath = null;
    StorageReference storageRef;
    FirebaseFirestore db;
    ImageView uploadPhotoBtn, productImg;
    Button addBtn;
    private String downloadImageUrl = "";
    private EditText idProductEt, nameEt, priceEt, colorEt, stockEt, descriptionEt;
    private ProgressBar progressBar;
    Product product;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        initAll();

//        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, categoriesList);
//        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        categorySpinner.setAdapter(categoryAdapter);

//        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, brandsList);
//        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        brandSpinner.setAdapter(brandAdapter);

//        ArrayAdapter<String> sizeTypeAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, sizeTypeList);
//        sizeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sizeTypeSpinner.setAdapter(sizeTypeAdapter);

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(NewProductActivity.this, android.R.layout.simple_list_item_1, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        SettingClickListners();


    }

    private void SettingClickListners() {
        getDataCategory();
        getDataBrand();
        getDataSizeType();
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
                String name = nameEt.getText().toString().trim();
                String id = idProductEt.getText().toString().trim();
                String price = priceEt.getText().toString().trim();
                String color = colorEt.getText().toString().trim();
                String stock = stockEt.getText().toString().trim();
                String desc = descriptionEt.getText().toString().trim();
                if (filePath == null) {
                    Toast.makeText(NewProductActivity.this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(id)) {
                    idProductEt.setError("Nhập mã sản phẩm");
                    idProductEt.requestFocus();
                } else if (TextUtils.isEmpty(name)) {
                    nameEt.setError("Nhập tên sản phẩm");
                    nameEt.requestFocus();
                } else if (category.equals("")) {
                    Toast.makeText(NewProductActivity.this, "Chưa chọn thể loại", Toast.LENGTH_SHORT).show();
                } else if (brand.equals("")) {
                    Toast.makeText(NewProductActivity.this, "Chưa chọn thương hiệu", Toast.LENGTH_SHORT).show();
                } else if (sizeType.equals("")) {
                    Toast.makeText(NewProductActivity.this, "Chưa chọn loại size", Toast.LENGTH_SHORT).show();
                } else if (size.equals("")) {
                    Toast.makeText(NewProductActivity.this, "Chưa chọn size", Toast.LENGTH_SHORT).show();
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
                    product.setProductId(id);
                    product.setName(name);
                    product.setCategory(category);
                    product.setBrand(brand);
                    product.setSizeType(sizeType);
                    product.setSize(size);
                    product.setPrice(Integer.parseInt(price));
                    product.setColor(color);
                    product.setStock(Integer.parseInt(stock));
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

        idProductEt = findViewById(R.id.product_id_et);
        nameEt = findViewById(R.id.product_name_et);
        priceEt = findViewById(R.id.price_et);
        colorEt = findViewById(R.id.color_et);
        stockEt = findViewById(R.id.stock_et);
        descriptionEt = findViewById(R.id.description_tv);

        storageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        product = new Product();
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
            final StorageReference childRef = storageRef.child(FirebaseFireStoreConstants.IMAGES).child(System.currentTimeMillis() + ".jpg");
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
        //https://stackoverflow.com/questions/68977581/cant-get-document-id-properly-firestore
        //https://www.google.com/search?q=Use+document+ID+for+attribute+id+firestore+android&sxsrf=ALiCzsaQL9EHpEjv06yhPpvY1vAg9R2sMg%3A1666539435848&ei=q19VY9GoM8ThseMP_qmxiAI&ved=0ahUKEwiRqs-Y1_b6AhXEcGwGHf5UDCEQ4dUDCA8&uact=5&oq=Use+document+ID+for+attribute+id+firestore+android&gs_lp=Egdnd3Mtd2l6uAED-AEBMgUQIRigATIFECEYoAHCAgoQABhHGNYEGLADwgIHECEYoAEYCsICBBAhGBWQBghIhEpQ-AVY4kZwAXgByAEAkAEAmAG2AaAB8hOqAQQwLjE44gMEIE0YAeIDBCBBGADiAwQgRhgAiAYB&sclient=gws-wiz

        String ID = idProductEt.getText().toString().trim();
//        db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("Products").document(ID);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(NewProductActivity.this, "Product exists", Toast.LENGTH_SHORT);
                    } else {
                        db.collection("Products").document(ID).set(product);
                        Toast.makeText(NewProductActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewProductActivity.this, "Failed with: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        startActivity(new Intent(NewProductActivity.this, ViewAllProductsActivity.class));


//        final CollectionReference reference = db.collection("Products");
//        reference.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(NewProductActivity.this, "Add success", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(NewProductActivity.this, "Add fail", Toast.LENGTH_SHORT);
//                    }
//                });
//        firestore = FirebaseFirestore.getInstance();
//        String key = firestore.collection("Products").document().getId();
//        product.setProductId(key);
//        final CollectionReference reference = firestore.collection("Products");
//        reference.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(NewProductActivity.this, "Add success", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(NewProductActivity.this, "Add fail", Toast.LENGTH_SHORT);
//                    }
//                });
    }

    private void getDataCategory() {

        CollectionReference subjectsRef = db.collection(FirebaseFireStoreConstants.CATEGORY);
        Spinner spinner = (Spinner) findViewById(R.id.product_category_Spinner);
        List<String> category = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("categoryName");
                        Log.d("ShowEventInfo:", subject);
                        category.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getDataBrand() {

        CollectionReference brandRef = db.collection(FirebaseFireStoreConstants.BRAND);
        Spinner spinner = (Spinner) findViewById(R.id.product_brand_Spinner);
        List<String> brand = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, brand);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        brandRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String brandName = document.getString("brandName");
                        Log.d("ShowEventInfo:", brandName);
                        brand.add(brandName);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getDataSizeType() {

        CollectionReference brandRef = db.collection(FirebaseFireStoreConstants.SIZE_TYPE);
        Spinner spinner = (Spinner) findViewById(R.id.product_size_type_Spinner);
        List<String> sizeType = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sizeType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        brandRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String sizeTypeName = document.getString("sizeName");
                        Log.d("ShowEventInfo:", sizeTypeName);
                        sizeType.add(sizeTypeName);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}


