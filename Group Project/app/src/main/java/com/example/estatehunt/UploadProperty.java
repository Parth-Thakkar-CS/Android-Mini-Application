package com.example.estatehunt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadProperty extends AppCompatActivity {

    //Variables
    EditText txtType,txtAddress,txtAvailableFor, txtPrice;
    Button btnChooseImage,btnSubmit;
    ImageView imgProperty;
    StorageReference strRef;
    Uri imageURI;
    StorageTask taskStorage;
    DatabaseReference dbRef;
    Estate objProperty;
    ProgressBar progressBar;
    public String imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_property);

        //Hooks
        txtType = findViewById(R.id.txtType);
        txtAddress = findViewById(R.id.txtAddress);
        txtPrice = findViewById(R.id.txtPrice);
        txtAvailableFor = findViewById(R.id.txtAvailableFor);
        imgProperty = findViewById(R.id.imgProperty);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        strRef = FirebaseStorage.getInstance().getReference("Images");
        dbRef = FirebaseDatabase.getInstance().getReference("Estate");
        objProperty = new Estate();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDetails();
            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("impage/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            imageURI = data.getData();
            imgProperty.setImageURI(imageURI);
        }
    }

    private void UploadDetails() {
        if (taskStorage != null && taskStorage.isInProgress()){
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Is in progress!", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.GONE);
            ImageUploader();
        }
    }

    private void ImageUploader() {
        imageId = System.currentTimeMillis() + "." + findExtension(imageURI);
        objProperty.setType(txtType.getText().toString().trim());
        objProperty.setLocation(txtAddress.getText().toString().trim());
        objProperty.setAvailable(txtAvailableFor.getText().toString().trim());
        objProperty.setPrice(Integer.parseInt(txtPrice.getText().toString().trim()));
        objProperty.setimageFileName(imageId);

        StorageReference refStorage = strRef.child(imageId);

        taskStorage = refStorage.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                final Uri taskResult = urlTask.getResult();
                final String downloadUrl = taskResult.toString();

                objProperty.setImageId(downloadUrl);
                dbRef.push().setValue(objProperty).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(UploadProperty.this, "Upload Complete!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private String findExtension(Uri imgUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgUri));
    }
}
