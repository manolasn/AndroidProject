package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


/**
 * This is the first activity after we have pressed Start at the main activity where we are asked about the number of players
 */
public class StartGame extends AppCompatActivity {

    private int number_of_players;
    private Button submit;
    private EditText activity_title;
    private HomeWatcher mHomeWatcher;
    private View contextView ;
    private boolean mIsBound = false;
    private MusicService mServ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        activity_title =  findViewById(R.id.editText);
        submit = findViewById(R.id.button4);
        contextView = findViewById(R.id.start_game_act);
        activity_title.setImeOptions(activity_title.getImeOptions()| EditorInfo.IME_FLAG_NO_EXTRACT_UI);


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




        //Submit button listener with value checking
        submit.setOnClickListener(v -> {
            if(activity_title.getText().toString().replaceAll("\\s","").equals("")) {
                Snackbar.make(contextView, "Please insert number before submitting", Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();
            }
            else {

                number_of_players = Integer.parseInt(activity_title.getText().toString());
                if(number_of_players >2&& number_of_players <11) {
                    Intent i = new Intent(this,EnterNamesOfPlayers.class);
                    i.putExtra("NUM_PLAYERS", number_of_players);
                    this.startActivity(i);
                }
                else{

                    Snackbar.make(contextView, "The game is played with 3 to 10 people", Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                            .getColor(R.color.colorAccent)).show();
                    activity_title.setText("");
                }

            }
        });

    }



    /**
     * The code below is about the MediaPlayer playing on background
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


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
