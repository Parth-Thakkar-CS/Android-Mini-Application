package com.example.estatehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class CheckAvailablity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Variables
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    Toolbar toolbar;
    EditText txtName, txtEmail, txtNote;
    TextView txtPosition;
    Button btnQuerry;
    StorageReference strRef;
    StorageTask taskStorage;
    DatabaseReference dbRef;
    Querry objQuerry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availablity);

        //Hooks
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtNote = findViewById(R.id.txtNote);
        txtPosition = findViewById(R.id.txtPosition);
        btnQuerry = findViewById(R.id.btnQuerry);

        strRef = FirebaseStorage.getInstance().getReference("Images");
        dbRef = FirebaseDatabase.getInstance().getReference("Querry");
        objQuerry = new Querry();

        Intent intent = getIntent();
        String mItemPostition = intent.getStringExtra("iPostition");
        txtPosition.setText(mItemPostition);

        btnQuerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadQuerry();
                Toast.makeText(CheckAvailablity.this, "Your Request is being processed!", Toast.LENGTH_SHORT).show();
            }
        });

        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.bringToFront();
        nav_view.setNavigationItemSelectedListener(this);
    }

    private void UploadQuerry() {
        if (taskStorage != null && taskStorage.isInProgress()){
            Toast.makeText(this, "Is in progress!", Toast.LENGTH_SHORT).show();
        }
        else {
            QuerryUploader();
        }
    }

    private void QuerryUploader() {
        objQuerry.setName(txtName.getText().toString().trim());
        objQuerry.setEmail(txtEmail.getText().toString().trim());
        objQuerry.setNotes(txtNote.getText().toString().trim());
        objQuerry.setPosition(txtPosition.getText().toString().trim());

        dbRef.push().setValue(objQuerry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CheckAvailablity.this, "Request Complete!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

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
}
