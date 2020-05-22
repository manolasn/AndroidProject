package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayEverything extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    private ArrayList<String> mother_nicknames =new ArrayList<>();
    private TextView title_mother;
    private TextView title_players;
    private TextView player_that_gets_points,timer_text;
    private Button add_1_point,add_2_points,next_round,timer;
    private CountDownTimer count_down;
    private long timeLeftMillsec;
    private MediaPlayer timer_sound;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_everything);
        title_mother=findViewById(R.id.textView5);
        title_players=findViewById(R.id.textView6);
        player_that_gets_points=findViewById(R.id.add_points_name);
        add_1_point=findViewById(R.id.button_1_point);
        add_2_points=findViewById(R.id.button_2_points);
        next_round=findViewById(R.id.button6);
        timer=findViewById(R.id.button_timer);
        timer_text=findViewById(R.id.timer_text);


        names_nicknames= (HashMap<String, String>) (getIntent().getSerializableExtra("NAMES_NICKNAMES"));

        String mother=getIntent().getStringExtra("MOTHER");

        mother_nicknames =(ArrayList<String>)(getIntent().getSerializableExtra("MOTHERNAMES"));

        if(names_nicknames.size()+1==3)
        {
            timeLeftMillsec=20000;
        }
        else if (names_nicknames.size()+1>3&&names_nicknames.size()+1<7)
        {
            timeLeftMillsec=30000;
        }
        else
        {
            timeLeftMillsec=55000;
        }



        for (int i = 0; i< mother_nicknames.size(); i++)
        {
            title_mother.append( "\n" +  (i+1) + " : " + mother_nicknames.get(i));
        }

        if(scoreboard!=null) {
            scoreboard.put(mother, 0);
        }

        final int[] j = {0};
        names_nicknames.entrySet().forEach(stringStringEntry -> {
                j[0]++;
                title_players.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());

                if(scoreboard!=null) {
                    scoreboard.put(stringStringEntry.getKey(), 0);
                }
        });


        if(player_that_gets_points.getVisibility()==View.VISIBLE)
        {
            player_that_gets_points.setVisibility(View.INVISIBLE);
        }

        if(add_1_point.getVisibility()== View.VISIBLE)
        {
            add_1_point.setVisibility(View.INVISIBLE);
        }

        if(add_2_points.getVisibility()== View.VISIBLE)
        {
            add_2_points.setVisibility(View.INVISIBLE);
        }


        //Buttons add 1 point and add 2 points must be hidden until a timer of 30 secs starts and ends.
        add_1_point.setOnClickListener(v -> {
            if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!scoreboard.containsKey(player_that_gets_points.getText().toString()))
            {
                player_that_gets_points.setText("");
                Toast.makeText(DisplayEverything.this, "Please insert a valid name to add point to.", Toast.LENGTH_SHORT).show();
            }
            else if (scoreboard.containsKey(player_that_gets_points.getText().toString()))
            {

                Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                scoreboard.replace(player_that_gets_points.getText().toString(),temp+1);
                Toast.makeText(DisplayEverything.this, "1 point was added to : " + player_that_gets_points.getText().toString(), Toast.LENGTH_SHORT).show();

                player_that_gets_points.setText("");

            }



        });

        //Buttons add 1 point and add 2 points must be hidden until a timer of 30 secs starts and ends.
        add_2_points.setOnClickListener(v -> {
            if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!scoreboard.containsKey(player_that_gets_points.getText().toString()))
            {
                player_that_gets_points.setText("");
                Toast.makeText(DisplayEverything.this, "Please insert a valid name to add points to.", Toast.LENGTH_SHORT).show();
            }
            else if (scoreboard.containsKey(player_that_gets_points.getText().toString()))
            {
                Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                scoreboard.replace(player_that_gets_points.getText().toString(),temp+2);

                Toast.makeText(DisplayEverything.this, "2 points were added to : " + player_that_gets_points.getText().toString(), Toast.LENGTH_SHORT).show();

                player_that_gets_points.setText("");

            }




        });

        next_round.setOnClickListener(v -> {

            Intent i=new Intent(this,StartGame.class);
            i.putExtra("SCOREBOARD",scoreboard);
            startActivity(i);


            System.out.print("\n");
            if(scoreboard!=null) {
                scoreboard.entrySet().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + " " + stringStringEntry.getValue()));
            }
        });



        timer.setOnClickListener(v -> {
            if(timer.getVisibility()== View.VISIBLE)
            {
                timer.setVisibility(View.INVISIBLE);
            }

            startTimer();


        });













    }

    public void startTimer(){

        count_down=new CountDownTimer(timeLeftMillsec,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillsec= millisUntilFinished;
                if(timeLeftMillsec/1000==11)
                {
                    play_sound();
                }
                updatetimer_text();
            }

            @Override
            public void onFinish() {



                if(timer_text.getVisibility()==View.VISIBLE){
                    timer_text.setVisibility(View.INVISIBLE);
                }
                if(add_1_point.getVisibility()== View.INVISIBLE)
                {
                    add_1_point.setVisibility(View.VISIBLE);
                }
                if(add_2_points.getVisibility()== View.INVISIBLE)
                {
                    add_2_points.setVisibility(View.VISIBLE);
                }
                if(player_that_gets_points.getVisibility()==View.INVISIBLE)
                {
                    player_that_gets_points.setVisibility(View.VISIBLE);
                }

                timer_sound.release();


            }
        }.start();

    }

    public void updatetimer_text(){
        int seconds = (int) timeLeftMillsec/1000;
        String timeLeftText;
        timeLeftText="00";
        timeLeftText+=":";
        if(seconds<10) {
            timeLeftText+="0";
            timer_text.setTextColor(Color.RED);
        }
        timeLeftText+=seconds;
        timer_text.setText(timeLeftText);
    }


    public void play_sound()
    {
        timer_sound = MediaPlayer.create(this,R.raw.timer_sound);
        timer_sound.start();
    }


}
