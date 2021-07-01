package com.example.plantkoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    MediaPlayer splashMusic;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        splashMusic=MediaPlayer.create(SplashScreen.this,R.raw.intro);
        splashMusic.start();
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        };

        handler = new Handler();

        handler.postDelayed(runnable,2500);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null && runnable != null)
        {
            splashMusic.release();
            handler.removeCallbacks(runnable);
        }
    }
}