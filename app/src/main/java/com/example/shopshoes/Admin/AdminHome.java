package com.example.shopshoes.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.shopshoes.Activity.LoginActivity;
import com.example.shopshoes.R;
import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // code logic bắt sự kiện khi click váo item nav_view_admin
        NavigationView navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);
//
//        replaceFragment(new HomeFragment());
//        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home_admin) {
            Intent intent = new Intent(getApplicationContext(), ViewAllProductsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_add_product) {
            Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
            startActivity(intent);
        }
//        if (id == R.id.nav_orders) {
//            Intent intent = new Intent(getApplicationContext(), CustomersOrders.class);
//            intent.putExtra("type","user");
//            startActivity(intent);
//        }
        else if (id == R.id.nav_logout) {
//            mAuth.signOut();
//            Paper.book().delete("active");
//            Paper.book().delete("user");
            Intent intent = new Intent(AdminHome.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
//            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);
        return true;
//        int id = item.getItemId();
//        if(id== R.id.nav_home_admin){
//            if(mCurrentFragment != VIEW_PRODUCT){
//                replaceFragment(new ViewAllProductsActivity());
//                mCurrentFragment = VIEW_PRODUCT;
//            }
//        } else if(id== R.id.nav_add_product){
//            if(mCurrentFragment != FRAGMENT_FAVORITE){
//                replaceFragment(new FavoriteFragment());
//                mCurrentFragment = FRAGMENT_FAVORITE;
//            }
//        } else if(id== R.id.nav_logout){
//            if(mCurrentFragment != FRAGMENT_HISTORY){
//                replaceFragment(new HistoryFragment());
//                mCurrentFragment = FRAGMENT_HISTORY;
//            }
//        }
//
//        //Đóng drawer
//        mDrawerLayout.closeDrawer(GravityCompat.START);
//        return true;
    }

    //Thực hiện đóng drawer mở khi click nut back và click back khi để thoát app
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    private  void replaceFragment(Fragment fragment){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }
}


//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.shopshoes.Model.Utils;
//import com.example.shopshoes.R;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
//
//import com.google.android.gms.tasks.Continuation;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.navigation.NavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import io.paperdb.Paper;
//
//public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
//
//    private AppBarConfiguration mAppBarConfiguration;
//
//    //related profile image
//    int PICK_IMAGE_REQUEST = 111;
//    Uri filePath;
//
//    private ProgressBar progressBar;
//    private CircleImageView UserProfileImg;
//    private CircleImageView ChangeProfileBtn;
//    private TextView UserNameDrawer;
//    private String downloadImageUrl;
//    StorageReference storageRef;
//    DatabaseReference myRootRef;
//    private FirebaseAuth mAuth;
//
//
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_admin);
//        Toolbar toolbar = findViewById(R.id.toolbar);
////        toolbar.setTitle(getString(R.string.app_name));
//        getSupportActionBar().setTitle("Shop shoes");
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
//        NavigationView navigationView = findViewById(R.id.nav_view_admin);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home_admin)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_admin);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//        navigationView.setNavigationItemSelectedListener(this);
//
//        View hView = navigationView.getHeaderView(0);
//        ChangeProfileBtn = hView.findViewById(R.id.nav_edit_profile_button_admin);
//        UserProfileImg = hView.findViewById(R.id.layout_profile_picture_image_preferred_admin);
//        UserNameDrawer = hView.findViewById(R.id.username_drawer_admin);
//        progressBar = hView.findViewById(R.id.profile_progress_bar_admin);
//        storageRef = FirebaseStorage.getInstance().getReference();
//        myRootRef = FirebaseDatabase.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
//
//        Paper.init(getApplicationContext());
//
//        settingUpClickListners();
////        getProfileData();
////
////
//        Utils.statusBarColor(AdminHome.this);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkLocationPermission();
//        }
//    }
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(AdminHome.this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(AdminHome.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(AdminHome.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            } else {
//                ActivityCompat.requestPermissions(AdminHome.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(AdminHome.this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                    }
//                } else {
//                    Toast.makeText(AdminHome.this, "permission denied",
//                            Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        //casting
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_admin);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    //setting on click listners
//    private void settingUpClickListners() {
//        //profile iamge change buttion Nav header
//        ChangeProfileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
//            }
//        });
//
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_admin);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
//
//    private void UploadImage() {
//        if (filePath != null) {
//            progressBar.setVisibility(View.VISIBLE);
//            final StorageReference childRef = storageRef.child("user_profiles").child(System.currentTimeMillis()+".jpg");
//
////            //uploading the image
//            final UploadTask uploadTask = childRef.putFile(filePath);
//
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    String message = e.toString();
//                    Toast.makeText(AdminHome.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(AdminHome.this, "Photo uploaded...", Toast.LENGTH_SHORT).show();
//                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//
//                            downloadImageUrl = childRef.getDownloadUrl().toString();
//                            return childRef.getDownloadUrl();
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                downloadImageUrl = task.getResult().toString();
//                                Log.d("imagUrl", downloadImageUrl);
//                                SaveInfoToDatabase();
//                            }
//                        }
//                    });
//                }
//            });
//
//        } else {
//            Toast.makeText(AdminHome.this, "Select an image", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void SaveInfoToDatabase() {
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        myRootRef.child("Users").child(currentUserId).child("photoUrl").setValue(downloadImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("TAG", "Saved to firebase");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("test", e.toString());
//                    }
//                });
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//            try {
//                //getting image from gallery
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //Setting image to ImageView
//                UserProfileImg.setImageBitmap(bitmap);
//                UploadImage();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
////    private void getProfileData() {
////
////        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
////        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
////        UsersRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                if (dataSnapshot.exists()) {
////                    User user=new User();
////                    user=dataSnapshot.getValue(User.class);
////
////                    if(user!=null){
////                        Log.d("usertest",user.getPhotoUrl());
////                    }
////                    else{
////                        Log.d("usertest",user.toString());
////                    }
////                    String image = dataSnapshot.child("photoUrl").getValue().toString();
////                    String name = dataSnapshot.child("name").getValue().toString();
//////                    Email = dataSnapshot.child("email").getValue().toString();
//////                    Password = dataSnapshot.child("pass").getValue().toString();
////                    try {
////                        if (image != null && !image.equals("")) {
////                            Picasso.get().load(image).placeholder(R.drawable.profile).into(UserProfileImg);
////                        }
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////
////                    UserNameDrawer.setText(name);
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////    }
//
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.nav_home_admin) {
////            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////            startActivity(intent);
//        }
////        if (id == R.id.nav_add_product) {
////            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
////            startActivity(intent);
////        }
////        if (id == R.id.nav_orders) {
////            Intent intent = new Intent(getApplicationContext(), CustomersOrders.class);
////            intent.putExtra("type","user");
////            startActivity(intent);
////        }
////        else if (id == R.id.nav_logout) {
////            mAuth.signOut();
////            Paper.book().delete("active");
//////            Paper.book().delete("user");
////            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////            startActivity(intent);
////            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
////            finish();
////        }
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//}
