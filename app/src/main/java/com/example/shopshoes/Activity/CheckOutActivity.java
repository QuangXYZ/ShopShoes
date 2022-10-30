package com.example.shopshoes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopshoes.Adapter.CartCustomAdapter;
import com.example.shopshoes.Model.Order;
import com.example.shopshoes.Model.Product;
import com.example.shopshoes.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

public class CheckOutActivity extends AppCompatActivity {
    private ImageView checkOutBackBtn;
    private TextView orderPrice,shipmentPrice,totalPayablePrice, checkOutBtn,streetAddress;
    private EditText usercomments;

    private ProgressDialog pd;
    private AlertDialog.Builder builder;

    private Order order;
    private ArrayList<Product> productArrayList;

    private String street;
    private String comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        initAll();
        getOrderFormFirebase();

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Xác nhận");
        builder.setMessage("Xác nhận thanh toán");





        OnClickListeners();



    }

    private void OnClickListeners() {
        checkOutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                street=streetAddress.getText().toString();
                comments=usercomments.getText().toString();

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if(order.getTotalPrice()>0){
                            order.setStatus("Pending");
                            updateOrderToFirebase();

                        }
                        else{
                            Toast.makeText(CheckOutActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private void updateOrderToFirebase()  {
        pd.show(this,"Please Wait..","Submitting order..");

        DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("OrderBill");
        String key=root.push().getKey();
        order.setId(key);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        order.setDateOfOrder(currentDateTimeString);
        order.setTotalPrice(order.getTotalPrice()+10);
        order.setStreet(street);
        order.setComments(comments);

        root.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(key).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                order.getCartProductList().clear();
                order.setTotalPrice(0.0);
                productArrayList.clear();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();;
                myRootRef.child("Order").child(currentUserId).setValue(null);
                //cartCustomAdapter.notifyDataSetChanged();
                //totalPriceView.setText("0.0");
                pd.dismiss();
                Toast.makeText(CheckOutActivity.this,"Thanh toán thành công",Toast.LENGTH_LONG).show();
            }
        });
        Intent intent=new Intent(CheckOutActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void initAll() {
        //view
        checkOutBackBtn=findViewById(R.id.checkout_back_btn);
        orderPrice=findViewById(R.id.checkout_order_price_view);
        shipmentPrice=findViewById(R.id.checkout_shipping_price_view);
        totalPayablePrice=findViewById(R.id.checkout_total_price_view);
        streetAddress=findViewById(R.id.checkout_address_view);
        usercomments=findViewById(R.id.checkout_comment_view);
        checkOutBtn=findViewById(R.id.checkout_btn);
        pd=new ProgressDialog(this);
        order=new Order();
        productArrayList =new ArrayList<>();
    }
    private void getOrderFormFirebase(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Order").child(currentUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    order = snapshot.getValue(Order.class);
                    productArrayList = (ArrayList<Product>) order.getProductArrayList().clone();
                    streetAddress.setText(order.getAddress());
                    //setting values of prices
                    //orderPrice.setText("Rs. "+ order.getTotalPrice());
                    orderPrice.setText(order.getTotalPrice()+" VND");
                    // totalPayablePrice.setText("Rs."+ (order.getTotalPrice()+200));
                    totalPayablePrice.setText((order.getTotalPrice()+10000)+" VND");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckOutActivity.this,"Error, Can't read data form firebase", Toast.LENGTH_LONG).show();

            }
        });
    }

}
