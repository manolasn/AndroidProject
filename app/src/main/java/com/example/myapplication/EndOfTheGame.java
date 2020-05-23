package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EndOfTheGame extends AppCompatActivity {

    private TextView winner;
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    private MediaPlayer player;
    private ArrayList<String> winners;
    private Button playAgain;
    private Button menu;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_the_game);

        playAgain=findViewById(R.id.buttonnewgame);
        menu=findViewById(R.id.buttonmenu);

        player = MediaPlayer.create(this,R.raw.win_sound);
        player.start();



        if(!player.isPlaying()){
            player.release();
        }

        winner=findViewById(R.id.textView7);

        winners =(ArrayList<String>) getIntent().getSerializableExtra("WINNER");

        assert winners != null;
        if(winners.size() == 1){
            winner.append("The Winner is :\n"+winners.get(0));
        }
        else if(winners.size()>1)
        {

            winner.append("The Winners are :");
            for(int i=0;i<winners.size();i++)
            {
                winner.append("\n"+winners.get(i));
            }


        }


        winner.append("\n \n");
        winner.append("Game Score Board :"+"\n");



        scoreboard=Assisting_Class.getScoreboard();

        final int[] j = {0};
        scoreboard.entrySet().forEach(stringStringEntry -> {
            j[0]++;
            winner.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());


        });


        playAgain.setOnClickListener(v -> {
            Intent i=new Intent(this,StartGame.class);
            Assisting_Class.clearScoreboard();
            startActivity(i);

        });

        menu.setOnClickListener(v -> {
            Intent i=new Intent(this,MainActivity.class);
            Assisting_Class.clearScoreboard();
            startActivity(i);

        });




    }



}
