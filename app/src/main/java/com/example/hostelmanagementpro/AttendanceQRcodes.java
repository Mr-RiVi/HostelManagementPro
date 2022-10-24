package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AttendanceQRcodes extends AppCompatActivity {

    private static int REQUEST_CODE = 100;

    ImageView qrIn, qrOut;
    Button btnSaveIn, btnSaveOut;

    OutputStream outputStream;
    OutputStream oStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_qrcodes);

        qrIn = findViewById(R.id.qrIn);
        qrOut = findViewById(R.id.qrOut);
        btnSaveIn = findViewById(R.id.btnSaveIn);
        btnSaveOut = findViewById(R.id.btnSaveOut);

        btnSaveIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AttendanceQRcodes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }
                else {
                    askPermission();
                }
            }
        });

        btnSaveOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AttendanceQRcodes.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveOutImage();
                }
                else {
                    askPermission();
                }
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar newToolBar = (Toolbar) findViewById(R.id.toolbarNew);
        setSupportActionBar(newToolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void saveOutImage() {
        File dir = new File(Environment.getExternalStorageDirectory(),"HostelManagementPro");

        if (!dir.exists()) {
            dir.mkdir();
        }
        BitmapDrawable drawables = (BitmapDrawable) qrIn.getDrawable();
        Bitmap bitmaps = drawables.getBitmap();

        File files = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            oStream = new FileOutputStream(files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmaps.compress(Bitmap.CompressFormat.JPEG,100,oStream);
        Toast.makeText(this, "Check-Out QR Code Saved", Toast.LENGTH_SHORT).show();

        try {
            oStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(AttendanceQRcodes.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            }
            else {
                Toast.makeText(AttendanceQRcodes.this, "Please Provide Required Permissions", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImage() {
        File dir = new File(Environment.getExternalStorageDirectory(),"HostelManagementPro");

        if (!dir.exists()) {
            dir.mkdir();
        }
        BitmapDrawable drawable = (BitmapDrawable) qrIn.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File file = new File(dir,System.currentTimeMillis()+".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(this, "Check-In QR Code Saved", Toast.LENGTH_SHORT).show();

        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        menu.removeItem(R.id.myProfile);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myProfile:
                Intent intent = new Intent(this, MyProfile.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                Intent intents = new Intent(this, MainActivity.class);
                startActivity(intents);
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}