package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * This is the class that represents the first screen of the app, the main activity.
 */
public class MainActivity extends AppCompatActivity {


    private Button leaderboard;
    private ImageButton muted;
    private ImageButton playing;
    HomeWatcher mHomeWatcher;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;


        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);

        if(!Assisting_Class.getMute()) {
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





        leaderboard=findViewById(R.id.button_leaderboard);

        Button button2 =  findViewById(R.id.button2);
        button2.setOnClickListener(v -> openHelpnew());

        playing=findViewById(R.id.button_play);
        muted=findViewById(R.id.button_mute);

        Button buttonStart = findViewById(R.id.button1);

        //Start button listener
        buttonStart.setOnClickListener(v -> startGame());

        //Leaderboard button listener
        leaderboard.setOnClickListener(v -> {
            //Put code here apoel
        });


        if(Assisting_Class.getMute()){
            playing.setVisibility(View.INVISIBLE);
            muted.setVisibility(View.VISIBLE);
        } else
        {
            muted.setVisibility(View.INVISIBLE);
            playing.setVisibility(View.VISIBLE);
        }


        //Mute and unmute buttons below
        playing.setOnClickListener(v -> {

            Assisting_Class.setMute(true);
            mServ.pauseMusic();
            muted.setVisibility(View.VISIBLE);
            playing.setVisibility(View.INVISIBLE);

        });

        muted.setOnClickListener(v -> {

            Assisting_Class.setMute(false);
            muted.setVisibility(View.INVISIBLE);
            playing.setVisibility(View.VISIBLE);
            mServ.resumeMusic();

        });



    }

    /**
     * This method is called to when the button How To Play is pressed and opens HelpActivity
     */
    public void openHelpnew(){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);

    }

    /**
     * This method is called when Start button is pressed and opens StartGame activity
     */
    public void startGame(){
        Intent intent = new Intent (this,StartGame.class);
        startActivity(intent);
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
                Scon,Context.BIND_AUTO_CREATE);
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

        if (mServ != null  && !Assisting_Class.getMute() ) {
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
