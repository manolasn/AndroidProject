package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.method.ScrollingMovementMethod;

import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    TextView helptxt;

    HomeWatcher mHomeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        helptxt=findViewById(R.id.textView2);
        String rules="Welcome to Nicknames: The Game!\n Number of players: 3-10 \nPurpose of the game: Become the first player to collect 3 points. \nPreparation: Enter a number of players. Then, each player individually enters in the application his name and nickname. Then the \"mother\" is randomly selected, who must then select 2 more nicknames. From this point until the end of the round the mother handles the application. After all the nicknames have been chosen (1 for each player and 3 for the mother) the mother must read aloud and slowly all the nicknames (of the players and her own) in random and different order each time without referring to the players to whom correspond to the pseudonyms. \n The game begins! \n\t4-10 Players: The player sitting to the right of the mother plays first. This player is trying to guess the nickname of any other player he wants (except the mother). If the 2 players find it, they become a team. The player whose nickname remains hidden whispers it to his teammate. Now they make all the decisions together. This team is guessing again. If the nickname is not found correctly, then nothing happens and the player who was asked must in return guess the nickname of a player(he can ask the same person). If a team finds the nickname of a player who is not in a team, this player joins the team and the other members whisper their hidden nickname. Stil, if one team guesses the other's hidden nickname, the teams merge. When there are now only two teams (regardless of individual, for example, a player can be a team on his own and all other players can be the other team) the final phase of the round begins. Each team chooses its leader. The team with the fewest members starts (in case of an equal number, the team that was formed first starts) and has 30 seconds to hold a meeting. When these 30 seconds have passed the leader must correctly say ALL the pseudonyms and the persons to which they correspond (of his team, of the opposing team and of the mother). If each member of the team finds them, he gets 1 point and the game progresses to the next round (in case the team consists of only one player, he gets 2 points). If she doesn't find everything right, the mother says out loud that she has made a mistake and in turn the other group meets for 30 seconds and tries to find all the nicknames. Each group has 3 chances to find all the nicknames correctly. In case no team finds them, the mother gets 1 point and the game progresses to the next round.\n\t3 Players: The process is the same as above, with the difference that the group creation phase is omitted, and the 2 players (the third is the mother) proceed directly to the last phase, where everyone tries to find the nickname of their opponent and the mother's 3 nicknames.";
        helptxt.setText(rules);
        helptxt.setMovementMethod(new ScrollingMovementMethod());
        Button buttonback =  findViewById(R.id.buttonbackhelp);
        buttonback.setOnClickListener(v -> goback());
        //button to go back to 1st screen

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




    }

    public void goback()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
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
