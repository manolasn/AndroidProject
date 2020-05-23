package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;



public class MainActivity extends AppCompatActivity {


    private Button leaderboard;
    private ImageButton muted;
    private ImageButton playing;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;


        if(!Assisting_Class.isplayingAudio) {
            Assisting_Class.playAudio(context);
        }



        leaderboard=findViewById(R.id.button_leaderboard);

        Button button2 =  findViewById(R.id.button2);
        button2.setOnClickListener(v -> openHelpnew());

        playing=findViewById(R.id.button_play);
        muted=findViewById(R.id.button_mute);

        Button buttonStart = findViewById(R.id.button1);
        buttonStart.setOnClickListener(v -> startGame());

        leaderboard.setOnClickListener(v -> {
            //Put code here apoel
        });


        muted.setVisibility(View.INVISIBLE);

        playing.setOnClickListener(v -> {


            Assisting_Class.stopAudio();
            muted.setVisibility(View.VISIBLE);
            playing.setVisibility(View.INVISIBLE);

        });

        muted.setOnClickListener(v -> {

            muted.setVisibility(View.INVISIBLE);
            playing.setVisibility(View.VISIBLE);
            Assisting_Class.playAudio(context);

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






}
