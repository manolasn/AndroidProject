package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class StartGame extends AppCompatActivity {
    private int number_of_players;
    private Button submit;
    private EditText activity_title;
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    HomeWatcher mHomeWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        activity_title =  findViewById(R.id.editText);
        submit = findViewById(R.id.button4);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);


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




        submit.setOnClickListener(v -> {
            if(activity_title.getText().toString().replaceAll("\\s","").equals("")) {
                Toast.makeText(StartGame.this, "Please insert number before submitting", Toast.LENGTH_SHORT).show();
            }
            else {

                number_of_players = Integer.parseInt(activity_title.getText().toString());
                if(number_of_players >2&& number_of_players <11) {
                    Toast.makeText(StartGame.this, "Players submitted : " + number_of_players, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,EnterNamesOfPlayers.class);
                    i.putExtra("NUM_PLAYERS", number_of_players);
                    this.startActivity(i);
                }
                else{
                    Toast.makeText(StartGame.this, "The game is played with 3 to 10 people", Toast.LENGTH_SHORT).show();
                    activity_title.setText("");
                }

            }
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

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

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
