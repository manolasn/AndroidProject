package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EndOfTheGame extends AppCompatActivity {

    private TextView winner,score;
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    private MediaPlayer player;
    private ArrayList<String> winners;
    private Button playAgain;
    private Button menu;
    HomeWatcher mHomeWatcher;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_the_game);

        playAgain=findViewById(R.id.buttonnewgame);
        menu=findViewById(R.id.buttonmenu);
        score = findViewById(R.id.textViewscore);

        player = MediaPlayer.create(this,R.raw.win_sound);
        player.start();



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


        score.append("Game Score Board :"+"\n");



        scoreboard=Assisting_Class.getScoreboard();

        final int[] j = {0};
        scoreboard.entrySet().forEach(stringStringEntry -> {
            j[0]++;
            score.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());


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
