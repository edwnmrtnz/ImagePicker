package com.github.em4rtinez.imagepicker;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.em4rtinez.imagepicker.ImagePicker.ImageDataReceiver;
import com.github.em4rtinez.imagepicker.ImagePicker.ImagePicker;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView ivImage;
    private Button btnPickFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage     = findViewById(R.id.ivImage);
        btnPickFrom = findViewById(R.id.btnPickFrom);



        btnPickFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImagePicker.Builder(MainActivity.this)
                        .enablePickImageFromCamera()
                        .enablePickImageFromGallery()
                        .getFile(new ImageDataReceiver() {
                            @Override
                            public void onImageReceived(File file) {
                                ivImage.setImageURI(Uri.fromFile(file));
                            }
                        }).show();
            }
        });

    }
}
