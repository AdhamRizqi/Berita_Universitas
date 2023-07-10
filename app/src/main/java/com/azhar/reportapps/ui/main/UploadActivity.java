package com.azhar.reportapps.ui.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.azhar.reportapps.R;
import com.azhar.reportapps.ui.report.ReportActivity;
import com.azhar.reportapps.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {
    private FloatingActionButton uploadButton;
    private ImageView uploadImage;
    EditText uploadCaption,uploadDeskripsi,uploadlokasi,uploadtanggal;

    ProgressBar progressBar;
    private Uri imageUri;
    final  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        uploadButton = findViewById(R.id.uploadButton);
        uploadCaption = findViewById(R.id.uploadCaption);
        uploadImage = findViewById(R.id.uploadImage);
        uploadDeskripsi = findViewById(R.id.uploadDeskripsi);
        uploadlokasi = findViewById(R.id.uploadlokasi);
        uploadtanggal = findViewById(R.id.uploadtanggal);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                } else  {
                    Toast.makeText(UploadActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        uploadtanggal.setOnClickListener(view -> {
            Calendar tanggalJemput = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                tanggalJemput.set(Calendar.YEAR, year);
                tanggalJemput.set(Calendar.MONTH, monthOfYear);
                tanggalJemput.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                uploadtanggal.setText(simpleDateFormat.format(tanggalJemput.getTime()));
            };

            new DatePickerDialog(UploadActivity.this, date,
                    tanggalJemput.get(Calendar.YEAR),
                    tanggalJemput.get(Calendar.MONTH),
                    tanggalJemput.get(Calendar.DAY_OF_MONTH)).show();
        });
        uploadlokasi.setText(Constant.lokasiPengaduan);

    }
    //Outside onCreate
    private void uploadToFirebase(Uri uri){
        String caption = uploadCaption.getText().toString();
        String Deskrip = uploadDeskripsi.getText().toString();
        String tanggal = uploadtanggal.getText().toString();
        String lokasi = uploadlokasi.getText().toString();


        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DataClass dataClass = new DataClass(uri.toString(), caption,Deskrip,tanggal,lokasi);
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(dataClass);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}