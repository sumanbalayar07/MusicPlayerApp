package com.balayar.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashactivity extends AppCompatActivity {
ImageView imageView;
Animation slideupanimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);
        imageView=findViewById(R.id.imageView);
        slideupanimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);
        Thread thread=new Thread() {
            public void run() {
                try {
                    imageView.startAnimation(slideupanimation);
                    sleep(1200);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent b=new Intent(splashactivity.this,MainActivity.class);
                    startActivity(b);
                    finish();
                }
            }
        };thread.start();
    }
}