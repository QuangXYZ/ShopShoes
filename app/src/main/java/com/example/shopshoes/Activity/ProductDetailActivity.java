package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private CardView addToCartBtn;
    private ImageView productImg;
    private TextView plusBTn,minusBtn,quantityTV;
    private TextView productName,productDescription,price;

    Product product;

    int quantity=1;

    private Order order;
    private String productID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initAll();
        ClickListeners();
        productID = getIntent().getExtras().getString("productID");
        product= (Product) getIntent().getSerializableExtra("product");


        if(product.getPhotoUrl()!=null){
            if(!product.getPhotoUrl().equals("")){
                Picasso.get().load(product.getPhotoUrl()).placeholder(R.drawable.icon).into(productImg);
            }
        }
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        price.setText(product.getPrice()+"VND");

    }



    private void ClickListeners() {
        plusBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity+=1;
                quantityTV.setText(String.valueOf(quantity));
                product.setQuantityInCart(quantity);
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>1){
                    quantity-=1;
                    quantityTV.setText(String.valueOf(quantity));
                    product.setQuantityInCart(quantity);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInCart=false;
                for(int i=0;i<order.getCartProductList().size();i++){
                    if(product.getProductId().equals(order.getCartProductList().get(i).getProductId())){
                        isInCart=true;
                        break;
                    }
                }
                if(!isInCart){

                    product.setQuantityInCart(quantity);
                    order.addProduct(product);
                    addOrderToFirebase(order);


                }
                else{
                    Toast.makeText(ProductDetailActivity.this,"Already in cart",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initAll() {
        addToCartBtn=findViewById(R.id.add_to_cart_btn);
        productImg=findViewById(R.id.product_img);
        plusBTn=findViewById(R.id.plus_btn);
        minusBtn=findViewById(R.id.minus_btn);
        quantityTV=findViewById(R.id.quantity_tv);
        productName=findViewById(R.id.product_name);
        price=findViewById(R.id.product_price);
        productDescription=findViewById(R.id.product_description);

        product=new Product();

        order=new Order();
        getOrderFormFirebase();
    }

    public void goBack(View view) {
        finish();
    }

    private void addOrderToFirebase(Order order){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();;
        myRootRef.child("Cart").child(currentUserId).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        });
    }
    private void getOrderFormFirebase(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Cart").child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    order = snapshot.getValue(Order.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}