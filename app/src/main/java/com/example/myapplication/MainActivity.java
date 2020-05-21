package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private MediaPlayer mysong;
    private Button leaderboard;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        mysong = MediaPlayer.create(this,R.raw.mainsound);
        mysong.setLooping(true);
        mysong.start();

        leaderboard=findViewById(R.id.button_leaderboard);

        Button button2 =  findViewById(R.id.button2);
        button2.setOnClickListener(v -> openHelpnew());

        Button buttonStart = findViewById(R.id.button1);
        buttonStart.setOnClickListener(v -> startGame());

        leaderboard.setOnClickListener(v -> {

            //Put code here apoel

        });



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


//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        mysong.release();
//        finish();
//    }

}
