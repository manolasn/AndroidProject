package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.HashMap;
/**
 * This activity is used to display all the information to the mother as when the round has ended to add points to the winning players
 * and if one of them has more than 3 points wins and the game ends
 */
public class DisplayEverything extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private int timesTimerPressed = 0 ;
    private HashMap<String,Integer> scoreboard=new HashMap<>();
    private ArrayList<String> mother_nicknames =new ArrayList<>();
    private TextView title_mother;
    private TextView title_players;
    private TextInputLayout floatinghint4;
    private TextView timer_text;
    private AutoCompleteTextView player_that_gets_points;
    private Button add_1_point,add_2_points,next_round, timer_button,endRound;
    private long timeLeftMillsec,tempMillsec;
    private MediaPlayer timer_sound;
    private HomeWatcher mHomeWatcher;
    private CountDownTimer count_down;
    private ImageView click_icon;
    private boolean countrunning;
    private View contextView ;
    private LeaderboardDatabase db=new LeaderboardDatabase(this);
    private boolean mIsBound = false;
    private MusicService mServ;
    //declaration

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_everything);
        title_mother = findViewById(R.id.textView5);
        title_players = findViewById(R.id.textView6);
        player_that_gets_points = findViewById(R.id.add_points_name);
        add_1_point = findViewById(R.id.button_1_point);
        add_2_points = findViewById(R.id.button_2_points);
        next_round = findViewById(R.id.button6);
        timer_button = findViewById(R.id.button_timer);
        timer_text = findViewById(R.id.timer_text);
        endRound = findViewById(R.id.buttonendround);
        floatinghint4 = findViewById(R.id.floating_hint_4);
        click_icon = findViewById(R.id.imageView);
        contextView = findViewById(R.id.disp_every_act);
        player_that_gets_points.setImeOptions(player_that_gets_points.getImeOptions()| EditorInfo.IME_FLAG_NO_EXTRACT_UI);


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

        scoreboard= Assisting_Class.getScoreboard();


        names_nicknames= (HashMap<String, String>) (getIntent().getSerializableExtra("NAMES_NICKNAMES"));

        String mother=getIntent().getStringExtra("MOTHER");

        mother_nicknames =(ArrayList<String>)(getIntent().getSerializableExtra("MOTHERNAMES"));

        //Here is the auto fill for add score texview from players that are registered in current session(lines 113-128)
        String[] temp1 =new String[names_nicknames.size()+1];
        temp1[0]=mother;

        final int[] index = {1};
        names_nicknames.entrySet().forEach(stringStringEntry -> {

            temp1[index[0]]=stringStringEntry.getKey();
            index[0]++;


        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.custon_layout_hint, temp1);
        player_that_gets_points.setThreshold(1);
        player_that_gets_points.setAdapter(adapter);

        //Here we set the timers according to the number of players
        if(names_nicknames.size()+1==3)
        {
            timeLeftMillsec=20000;
            tempMillsec=20000;
        }
        else if (names_nicknames.size()+1>3&&names_nicknames.size()+1<7)
        {
            timeLeftMillsec=60000;
            tempMillsec=60000;
        }
        else
        {
            timeLeftMillsec=90000;
            tempMillsec=90000;
        }


        //Here we display the mother nicknames
        title_mother.append("Mother ("+mother+") Nicknames :");
        for (int i = 0; i< mother_nicknames.size(); i++)
        {
            title_mother.append( "\n" +  (i+1) + " : " + mother_nicknames.get(i));
        }

        //Here we display each player's names and nicknames
        final int[] j = {0};
        names_nicknames.entrySet().forEach(stringStringEntry -> {
                j[0]++;
                title_players.append("\n" + j[0] + " : " + stringStringEntry.getKey()+" ----> "+ stringStringEntry.getValue());


        });


        //We hide some UI elements that we dont need for now.
        if(player_that_gets_points.getVisibility() == View.VISIBLE)
        {
            player_that_gets_points.setVisibility(View.INVISIBLE);
            floatinghint4.setVisibility(View.INVISIBLE);

        }

        if(click_icon.getVisibility() == View.VISIBLE) {
            click_icon.setVisibility(View.INVISIBLE);
            timer_text.setVisibility(View.INVISIBLE);
        }

        if(add_1_point.getVisibility() == View.VISIBLE)
        {
            add_1_point.setVisibility(View.INVISIBLE);
        }

        if(add_2_points.getVisibility() == View.VISIBLE)
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



                    if(db.getAllNames().contains(player_that_gets_points.getText().toString())) {
                        Player a=db.getPlayer(player_that_gets_points.getText().toString());
                        a.setScore(a.getScore() + 1);
                        db.updatePlayer(a);
                    } else {
                        db.addPlayer(new Player(player_that_gets_points.getText().toString(),1));
                    }


                }
                else {
                    Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                    scoreboard.replace(player_that_gets_points.getText().toString(),temp+1);



                    if(db.getAllNames().contains(player_that_gets_points.getText().toString())) {
                        Player a=db.getPlayer(player_that_gets_points.getText().toString());
                        a.setScore(a.getScore() + 1);
                        db.updatePlayer(a);
                    } else {
                        db.addPlayer(new Player(player_that_gets_points.getText().toString(),1));
                    }


                }

                if(next_round.getVisibility() == View.INVISIBLE) {

                    next_round.setVisibility(View.VISIBLE);

                }

                Snackbar.make(contextView, "1 point added to : " + player_that_gets_points.getText().toString(), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();

                player_that_gets_points.setText("");


            }
           else if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!names_nicknames.containsKey(player_that_gets_points.getText().toString())||!player_that_gets_points.getText().toString().equals(mother))

           {

                player_that_gets_points.setText("");
                Snackbar.make(contextView, "Please insert a valid name to add point to." + player_that_gets_points.getText().toString(), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                       .getColor(R.color.colorAccent)).show();

           }




        });

        add_2_points.setOnClickListener(v -> {

            if (names_nicknames.containsKey(player_that_gets_points.getText().toString())||player_that_gets_points.getText().toString().equals(mother))
            {

                if(!scoreboard.containsKey(player_that_gets_points.getText().toString()))
                {

                    scoreboard.put(player_that_gets_points.getText().toString(),2);


                    if(db.getAllNames().contains(player_that_gets_points.getText().toString())) {
                        Player a=db.getPlayer(player_that_gets_points.getText().toString());
                        a.setScore(a.getScore() + 2);
                        db.updatePlayer(a);
                    } else {
                        db.addPlayer(new Player(player_that_gets_points.getText().toString(),2));
                    }


                }
                else {

                    Integer temp= scoreboard.get(player_that_gets_points.getText().toString());
                    scoreboard.replace(player_that_gets_points.getText().toString(),temp+2);

                    if(db.getAllNames().contains(player_that_gets_points.getText().toString())) {
                        Player a=db.getPlayer(player_that_gets_points.getText().toString());
                        a.setScore(a.getScore() + 2);
                        db.updatePlayer(a);
                    } else {
                        db.addPlayer(new Player(player_that_gets_points.getText().toString(),2));
                    }

                }

                if(next_round.getVisibility() == View.INVISIBLE) {

                    next_round.setVisibility(View.VISIBLE);

                }

                Snackbar.make(contextView, "2 points added to : " + player_that_gets_points.getText().toString(), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();

                player_that_gets_points.setText("");

            }

            else if(player_that_gets_points.getText().toString().replaceAll("\\s","").equals("")||!names_nicknames.containsKey(player_that_gets_points.getText().toString())||!player_that_gets_points.getText().toString().equals(mother))
            {

                player_that_gets_points.setText("");
                Snackbar.make(contextView, "Please insert a valid name to add point to." + player_that_gets_points.getText().toString(), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();
            }


        });

        if(next_round.getVisibility() == View.VISIBLE) {

            next_round.setVisibility(View.INVISIBLE);

        }

        //Each time next round button is pressed we check if one or more players' points have exceeded 3 points and we announce them winners
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

            if(scoreboard!=null) {
                scoreboard.entrySet().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + " " + stringStringEntry.getValue()));
                Assisting_Class.setScoreboard(scoreboard);
            }
            //if at least one player has more that 3 points we end the game else we go to the next round
            if(maxScore>2){
                Intent i=new Intent(this,EndOfTheGame.class);
                i.putExtra("WINNER",winner);
                i.putExtra("PLAYERS",names_nicknames);
                i.putExtra("MOTHER",mother);
                startActivity(i);
            } else {

                Intent i = new Intent(this, StartGame.class);

                startActivity(i);
            }



        });



        timer_text.setOnClickListener(v -> {



            if(countrunning)
            {
                count_down.cancel();
                if(timer_sound!=null) {

                    timer_sound.release();

                }
            }

            if(timer_button.getVisibility()==View.INVISIBLE){
                timer_button.setVisibility(View.VISIBLE);
                click_icon.setVisibility(View.INVISIBLE);
                timer_text.setVisibility(View.INVISIBLE);
            }

            if(timesTimerPressed==6) {

                if(next_round.getVisibility() == View.INVISIBLE) {

                    next_round.setVisibility(View.VISIBLE);

                }

                if(endRound.getVisibility()==View.VISIBLE){

                    endRound.setVisibility(View.INVISIBLE);

                }

                if(timer_button.getVisibility() == View.VISIBLE) {

                    timer_button.setVisibility(View.INVISIBLE);

                }


                if (add_1_point.getVisibility() == View.INVISIBLE) {

                    add_1_point.setVisibility(View.VISIBLE);

                }

                if (add_2_points.getVisibility() == View.INVISIBLE) {

                    add_2_points.setVisibility(View.VISIBLE);

                }

                player_that_gets_points.setVisibility(View.VISIBLE);

                floatinghint4.setVisibility(View.VISIBLE);



            }

            timeLeftMillsec=tempMillsec;
        });


        //We use timer each time a team has a chance to find all nicknames and end the round
        timer_button.setOnClickListener(v -> {
            if(timer_button.getVisibility()== View.VISIBLE)
            {
                timer_button.setVisibility(View.INVISIBLE);
            }

            timesTimerPressed++;
            timer_button.setText("START TIMER "+(timesTimerPressed+1));

            startTimer();


        });

        //If a team finds the nicknames before all the tries are used the game ends abruptly and we go to the next round
        endRound.setOnClickListener(v -> {


           if(countrunning)
           {
               count_down.cancel();
               if(timer_sound!=null) {

                   timer_sound.release();

               }
           }





            if(endRound.getVisibility()==View.VISIBLE){
                endRound.setVisibility(View.INVISIBLE);
            }

            if(timer_button.getVisibility()==View.VISIBLE){
                timer_button.setVisibility(View.INVISIBLE);
            }

            if (timer_text.getVisibility() == View.VISIBLE) {
                timer_text.setVisibility(View.INVISIBLE);
                click_icon.setVisibility(View.INVISIBLE);

            }
            if (add_1_point.getVisibility() == View.INVISIBLE) {
                add_1_point.setVisibility(View.VISIBLE);
            }
            if (add_2_points.getVisibility() == View.INVISIBLE) {
                add_2_points.setVisibility(View.VISIBLE);
            }
            if (player_that_gets_points.getVisibility() == View.INVISIBLE) {
                player_that_gets_points.setVisibility(View.VISIBLE);
                floatinghint4.setVisibility(View.VISIBLE);
            }

        });













    }

    /**
     * This method implements the timer that we use for the teams to meet
     */
    public void startTimer(){


            count_down = new CountDownTimer(timeLeftMillsec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countrunning=true;
                if(click_icon.getVisibility() == View.INVISIBLE) {
                    click_icon.setVisibility(View.VISIBLE);
                    timer_text.setVisibility(View.VISIBLE);
                }
                timeLeftMillsec = millisUntilFinished;
                if (timeLeftMillsec / 1000 == 11) {
                    play_sound();
                }
                updatetimer_text();
            }



            @Override
            public void onFinish() {

                countrunning=false;

                if(timesTimerPressed==6) {

                    if(endRound.getVisibility()==View.VISIBLE)
                    {
                        endRound.setVisibility(View.INVISIBLE);
                    }

                    if (add_1_point.getVisibility() == View.INVISIBLE) {
                        add_1_point.setVisibility(View.VISIBLE);
                    }
                    if (add_2_points.getVisibility() == View.INVISIBLE) {
                        add_2_points.setVisibility(View.VISIBLE);
                    }
                    if (player_that_gets_points.getVisibility() == View.INVISIBLE) {
                        player_that_gets_points.setVisibility(View.VISIBLE);
                        floatinghint4.setVisibility(View.VISIBLE);
                    }
                }
                else if(timer_button.getVisibility()== View.INVISIBLE)
                {
                    timeLeftMillsec=tempMillsec;
                    timer_button.setVisibility(View.VISIBLE);
                }

                if(click_icon.getVisibility() == View.VISIBLE)
                {
                    timer_text.setVisibility(View.INVISIBLE);
                    click_icon.setVisibility(View.INVISIBLE);
                }
//TODO:531c4854a33de2fa crash report number
                if(timer_sound!=null) {
                    if (timer_sound.isPlaying()) {
                        timer_sound.release();
                    }
                }


            }
        }.start();

    }

    /**
     * Here on each tick of the timer we update the text that the UI is showing
     */
    public void updatetimer_text(){
        int minutes = (int) (timeLeftMillsec/1000)/60;
        int seconds = (int) (timeLeftMillsec/1000) % 60;
        String timeLeftText;
        timeLeftText ="0" + minutes;


        if(minutes==0)
        {
            timeLeftText="00";
        }


        timeLeftText+=":";


        if(seconds<10&&minutes==0) {
            timeLeftText+="0";
            timer_text.setTextColor(Color.RED);
        } else
        {
            timer_text.setTextColor(Color.BLACK);
        }




        timeLeftText+=seconds;
        timer_text.setText(timeLeftText);
    }


    //Sound for timer
    public void play_sound()
    {
        timer_sound = MediaPlayer.create(this,R.raw.timer_sound);
        timer_sound.start();
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
