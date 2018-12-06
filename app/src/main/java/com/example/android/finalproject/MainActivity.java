package com.example.android.finalproject;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button right = findViewById(R.id.buttonRight);
        final Button left = findViewById(R.id.buttonLeft);
        final Button mid = findViewById(R.id.buttonMid);
        ImageView image = (ImageView) findViewById(R.id.catPic);
        image.setImageResource(R.drawable.cat1);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.catPic);
                image.setImageResource(R.drawable.cat);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.catPic);
                image.setImageResource(R.drawable.cat);
            }
        });
        mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.catPic);
                image.setImageResource(R.drawable.cat);
            }
        });

    }
}
