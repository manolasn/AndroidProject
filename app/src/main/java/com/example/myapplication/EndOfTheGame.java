package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import nl.dionsegijn.konfetti.KonfettiView;

/**
 * This is the last activity after the winner is announced and we have the choice to play again or go to main menu
 */
public class EndOfTheGame extends AppCompatActivity {

    private TextView winner,score;
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    private MediaPlayer player;
    private ArrayList<String> winners;
    private Button playAgain;
    private Button menu;
    private HashMap<String,String> names_nicknames=new HashMap<>();
    private String mother;

    HomeWatcher mHomeWatcher;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_the_game);

        playAgain=findViewById(R.id.buttonnewgame);
        menu=findViewById(R.id.buttonmenu);
        score = findViewById(R.id.textViewscore);

        //Here we are using the n1.dionsegijn:konfetti external library to make some animations when winner is found.
        final KonfettiView konfettiView = findViewById(R.id.viewKonfetti);
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(-20.0 ,200.0)
                .setSpeed(5f, 20f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(500f, 500f, -50f, -50f)
                .streamFor(500, 4000L);

        //Winning sound
        player = MediaPlayer.create(this,R.raw.win_sound);
        player.start();

        names_nicknames = (HashMap<String, String>) getIntent().getSerializableExtra("PLAYERS");
        mother = getIntent().getStringExtra("MOTHER");



        if(!player.isPlaying()){
            player.release();
        }


        if(!Assisting_Class.getMute()) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

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


        score.append("Game Score Board :");



        scoreboard=Assisting_Class.getScoreboard();


        names_nicknames.entrySet().forEach(stringStringEntry -> {
           if(!scoreboard.containsKey(stringStringEntry.getKey())){
               scoreboard.put(stringStringEntry.getKey(),0);
           }

           if(!scoreboard.containsKey(mother)){
               scoreboard.put(mother,0);
           }


        });


        final int[] j = {0};
        scoreboard.entrySet().forEach(stringStringEntry -> {
            j[0]++;
            score.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());


        });

        //Play again button listener
        playAgain.setOnClickListener(v -> {
            Intent i=new Intent(this,StartGame.class);
            Assisting_Class.clearScoreboard();
            startActivity(i);

        });
        //Menu button listener
        menu.setOnClickListener(v -> {
            Intent i=new Intent(this,MainActivity.class);
            Assisting_Class.clearScoreboard();
            startActivity(i);

        });




    }

    /**
     * The code below is about the MediaPlayer playing on background
     */

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHomeWatcher.startWatch();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHomeWatcher.stopWatch();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }






}
