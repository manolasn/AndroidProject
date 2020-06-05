package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

import android.widget.Button;
import android.widget.TextView;

/**
 * This class is the How to play activity
 */
public class HowToPlay extends AppCompatActivity {

    private HomeWatcher mHomeWatcher;
    private boolean mIsBound = false;
    private MusicService mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assisting_Class.loadlocale(this);
        setContentView(R.layout.help);

        TextView helptxt = findViewById(R.id.textView2);
        String rules=getResources().getString(R.string.rules);
        helptxt.setText(rules);
        Button buttonback =  findViewById(R.id.buttonbackhelp);


        //button to go back to 1st screen
        buttonback.setOnClickListener(v -> goback());


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




    }


    /**
     * This method is to go back to menu when the arrow back button is pressed
     */
    public void goback()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    /**
     * This code is for Media Player playing on background
     */
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
