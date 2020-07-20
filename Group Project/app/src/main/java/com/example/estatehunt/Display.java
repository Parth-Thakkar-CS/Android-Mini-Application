package com.example.estatehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Display extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Toolbar toolbar;
    TextView mTitle, mLocation, mAvailable, mPrice, mPosition;
    ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        //Hooks
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.title);
        mLocation = findViewById(R.id.location);
        mAvailable = findViewById(R.id.available);
        mPrice = findViewById(R.id.price);
        mPosition = findViewById(R.id.txtPosition);
        mImage = findViewById(R.id.imageView);

        //Toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle("Display");

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);

        //Get Data From Selected Option
        Intent intent = getIntent();

        String mItemPostition = intent.getStringExtra("iPostition");
        String mItemTitle = intent.getStringExtra("iType");
        String mItemLocation = intent.getStringExtra("iLocation");
        String mItemAvailable = intent.getStringExtra("iAvailable");
        String mItemPrice = intent.getStringExtra("iPrice");

        byte[] mBytes = getIntent().getByteArrayExtra("iImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes,0, mBytes.length);

        mPosition.setText(mItemPostition);
        mTitle.setText(mItemTitle);
        mLocation.setText(mItemLocation);
        mAvailable.setText(mItemAvailable);
        mPrice.setText(mItemPrice);
        mImage.setImageBitmap(bitmap);
    }


    //Navigate to selected Menu Option
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LogIn.class));
                finish();
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Add To Listing
    public void addItem(View view) {
        startActivity(new Intent(getApplicationContext(),UploadProperty.class));
    }

    public void checkAvailablity(View view) {
        Intent intent = new Intent(this,CheckAvailablity.class);
        intent.putExtra("iPosition", mPosition.getText().toString());
        startActivity(intent);
    }
}
