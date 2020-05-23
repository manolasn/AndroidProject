package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

        scoreboard= Assisting_Class.getScoreboard();


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
            timeLeftMillsec=50000;
        }


        title_mother.append("Mother ("+mother+") Nicknames :");
        for (int i = 0; i< mother_nicknames.size(); i++)
        {
            title_mother.append( "\n" +  (i+1) + " : " + mother_nicknames.get(i));
        }



        final int[] j = {0};
        names_nicknames.entrySet().forEach(stringStringEntry -> {
                j[0]++;
                title_players.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());


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


        //Buttons that add 1 point and 2 points are implemented here
        add_1_point.setOnClickListener(v -> {
           if (names_nicknames.containsKey(player_that_gets_points.getText().toString())||player_that_gets_points.getText().toString().equals(mother))
            {


                if(!scoreboard.containsKey(player_that_gets_points.getText().toString()))
                {
                    scoreboard.put(player_that_gets_points.getText().toString(),1);

                } else
                {
                    Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                    scoreboard.replace(player_that_gets_points.getText().toString(),temp+1);

                }


                Toast.makeText(DisplayEverything.this, "1 point was added to : " + player_that_gets_points.getText().toString(), Toast.LENGTH_SHORT).show();

                player_that_gets_points.setText("");


            }
           else if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!names_nicknames.containsKey(player_that_gets_points.getText().toString())||!player_that_gets_points.getText().toString().equals(mother))

           {

                player_that_gets_points.setText("");
                Toast.makeText(DisplayEverything.this, "Please insert a valid name to add point to.", Toast.LENGTH_SHORT).show();

           }




        });

        add_2_points.setOnClickListener(v -> {
            if (names_nicknames.containsKey(player_that_gets_points.getText().toString())||player_that_gets_points.getText().toString().equals(mother))
            {


                if(!scoreboard.containsKey(player_that_gets_points.getText().toString()))
                {
                    scoreboard.put(player_that_gets_points.getText().toString(),2);
                } else
                {
                    Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                    scoreboard.replace(player_that_gets_points.getText().toString(),temp+2);

                }





                Toast.makeText(DisplayEverything.this, "2 points were added to : " + player_that_gets_points.getText().toString(), Toast.LENGTH_SHORT).show();

                player_that_gets_points.setText("");


            }
            else if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!names_nicknames.containsKey(player_that_gets_points.getText().toString())||!player_that_gets_points.getText().toString().equals(mother))
            {

                player_that_gets_points.setText("");
                Toast.makeText(DisplayEverything.this, "Please insert a valid name to add point to.", Toast.LENGTH_SHORT).show();

            }




        });

        next_round.setEnabled(false);

        next_round.setOnClickListener(v -> {


           int maxScore=0;
           ArrayList<String> winner=new ArrayList<>();
           if(scoreboard!=null) {

                for(HashMap.Entry<String, Integer> entry : scoreboard.entrySet() ) {

                    if(entry.getValue()>maxScore) {
                        maxScore = entry.getValue();
                    }

                }
               for(HashMap.Entry<String, Integer> entry : scoreboard.entrySet() ) {

                   if (entry.getValue() == maxScore)
                   {
                       winner.add(entry.getKey());
                   }

               }

           }

            System.out.print("\n");
            if(scoreboard!=null) {
                scoreboard.entrySet().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + " " + stringStringEntry.getValue()));
                Assisting_Class.setScoreboard(scoreboard);
            }

            if(maxScore>=3){
                Intent i=new Intent(this,EndOfTheGame.class);
                i.putExtra("WINNER",winner);
                startActivity(i);
            } else {

                Intent i = new Intent(this, StartGame.class);

                startActivity(i);
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

        CountDownTimer count_down = new CountDownTimer(timeLeftMillsec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillsec = millisUntilFinished;
                if (timeLeftMillsec / 1000 == 11) {
                    play_sound();
                }
                updatetimer_text();
            }

            @Override
            public void onFinish() {


                next_round.setEnabled(true);

                if (timer_text.getVisibility() == View.VISIBLE) {
                    timer_text.setVisibility(View.INVISIBLE);
                }
                if (add_1_point.getVisibility() == View.INVISIBLE) {
                    add_1_point.setVisibility(View.VISIBLE);
                }
                if (add_2_points.getVisibility() == View.INVISIBLE) {
                    add_2_points.setVisibility(View.VISIBLE);
                }
                if (player_that_gets_points.getVisibility() == View.INVISIBLE) {
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
