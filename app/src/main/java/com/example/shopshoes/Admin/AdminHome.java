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
import com.example.shopshoes.Admin.Brand.NewBrandActivity;
import com.example.shopshoes.Admin.Brand.ViewAllBrandActivity;
import com.example.shopshoes.Admin.Category.NewCategoryActivity;
import com.example.shopshoes.Admin.Category.ViewAllCategoryActivity;
import com.example.shopshoes.Admin.Oder.CustomerOderActivity;
import com.example.shopshoes.Admin.Product.NewProductActivity;
import com.example.shopshoes.Admin.Product.ViewAllProductsActivity;
import com.example.shopshoes.R;
import com.google.android.material.navigation.NavigationView;

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
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(AdminHome.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_customer_statistic) {
            Intent intent = new Intent(AdminHome.this, statisticActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_add_category) {
            Intent intent = new Intent(AdminHome.this, NewCategoryActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_view_category) {
            Intent intent = new Intent(AdminHome.this, ViewAllCategoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_brand) {
            Intent intent = new Intent(AdminHome.this, NewBrandActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_view_brand) {
            Intent intent = new Intent(AdminHome.this, ViewAllBrandActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_customer_order) {
            Intent intent = new Intent(AdminHome.this, CustomerOderActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
