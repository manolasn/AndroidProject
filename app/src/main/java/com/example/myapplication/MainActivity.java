package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mysong;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        mysong = MediaPlayer.create(this,R.raw.mainsound);
        mysong.setLooping(true);
        mysong.start();

        Button button2 =  findViewById(R.id.button2);
        button2.setOnClickListener(v -> openHelpnew());

        Button buttonStart = findViewById(R.id.button1);
        buttonStart.setOnClickListener(v -> startGame());

    }

    public void openHelpnew(){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void startGame(){
        Intent intent = new Intent (this,StartGame.class);
        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mysong.release();
        finish();
    }

}
