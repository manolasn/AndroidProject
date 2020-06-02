package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;

import android.os.IBinder;
import android.os.PowerManager;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;


/**
 * This is the activity that everything is setting up for the game we ask the players
 * one by one their name and we put it in a HashMap as key the name and value nickname
 */
public class EnterNamesOfPlayers extends AppCompatActivity {
    private HashMap<String,String> names_nicknames=new HashMap<>();
    private int number_of_players;
    private EditText nicknames;
    private AutoCompleteTextView names;
    private TextView activity_title,hintnicknames;
    private ArrayList<String> mother_nicknames =new ArrayList<>();
    private ArrayList<String> hint_nicknames =new ArrayList<>();
    private TextInputLayout floatinghint1, floatinghint2;
    private Button submit_names;
    int click_count = 0 ;
    int mother_add_count = 0 ;
    private HomeWatcher mHomeWatcher;
    private GifImageView gifanimation;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_of_players);
        number_of_players = getIntent().getIntExtra("NUM_PLAYERS",0);
        activity_title = findViewById(R.id.textView4);
        names = findViewById(R.id.realname);
        nicknames = findViewById(R.id.nickname);
        submit_names = findViewById(R.id.button5);
        hintnicknames = findViewById(R.id.text_hints);
        floatinghint1 = findViewById(R.id.floating_hint);
        floatinghint2 = findViewById(R.id.floating_hint_2);
        gifanimation = findViewById(R.id.gif_animation);

        if(gifanimation.getVisibility() == View.VISIBLE)
        {
            gifanimation.setVisibility(View.INVISIBLE);
        }


        LeaderboardDatabase db=new LeaderboardDatabase(this);

        ArrayList<Player> aHash=db.getAll();

        String [] temp=new String[aHash.size()];
        for(int i=0;i<aHash.size();i++)
        {
            temp[i]=aHash.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.custon_layout_hint, temp);
        names.setThreshold(1);
        names.setAdapter(adapter);



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





        if(hintnicknames.getVisibility() == View.VISIBLE)
        {
            hintnicknames.setVisibility(View.INVISIBLE);
        }

        List<String> newList= Arrays.asList("Κομπολόι","source", "tooth", "merit" ,"kettle", "play" ,"precision" ,"equation" ,"transform" ,"trail", "reach" ,"delicate" ,"recovery", "empire", "post", "band", "delivery", "climate", "number", "flourish", "increase" ,"convince","bounce" ,"connection" ,"record", "hook","cross", "trade" ,"hostage" ,"menu", "delete", "offspring" ,"examination" ,"rob" ,"cherry" ,"hostile", "fit", "myth", "quote", "evaluate", "exposure", "warn", "consultation", "designer", "cooperate" ,"invite", "shape", "canvas" ,"chimpanzee" ,"ride","παπαπαπ",
                "πηγή", "δόντι", "αξία", "βραστήρας", "παιχνίδι", "ακρίβεια", "εξίσωση", "μεταμόρφωση", "ίχνος", "προσέγγιση", "παπαρούνα", "ανάκτηση", "αυτοκρατορία "," ανάρτηση "," μπάντα "," παράδοση "," κλίμα "," αριθμός "," περικαμψύλιο "," αύξηση "," ανοχή "," αναπήδηση "," σύνδεση "," ηχογράφηση "," γάντζος ", "κορνίζα", "εμπόριο", "όμηρος", "rca", "διαγραφή", "απόγονος", "εξέταση", "ληστεία", "κεράσι", "εχθρικός", "θηλιά", "μύθος", "απόσπασμα "," αξιολόγηση "," έκθεση "," προειδοποίηση "," διαβούλευση "," σχεδιαστής "," συνεργασία "," πρόσκληση "," σχήμα "," καμβάς "," χιμπατζής "," βόλτα "," hyperX ");

        hint_nicknames.addAll(newList);

        names_nicknames.entrySet().forEach(stringStringEntry -> {
            hint_nicknames.remove(stringStringEntry.getValue());
        });

        ToggleButton hint = findViewById(R.id.hint_button);
        hint.setOnClickListener(v -> {

            if(hint_nicknames.size()<7)
            {
                hint_nicknames.clear();
                hint_nicknames.addAll(newList);
            }



            if(hint.isChecked()){

                if(hintnicknames.getVisibility() == View.INVISIBLE)
                {
                    hintnicknames.append("Some nicknames :");
                    for(int i=0; i<5&&i<hint_nicknames.size();i++)
                    {

                        hintnicknames.append("\n"+hint_nicknames.get(i));
                        hint_nicknames.remove(i);
                    }
                    hintnicknames.setVisibility(View.VISIBLE);
                }

            }
            else {

                if(hintnicknames.getVisibility() == View.VISIBLE)
                {
                    hintnicknames.setVisibility(View.INVISIBLE);
                    hintnicknames.setText("");
                }

            }
        });






        final String[] motherName = {null};
        //The submit button listener is implemented here we count how many times its pressed to dynamically change the UI
        submit_names.setOnClickListener(v -> {
            click_count++;

            if(hint.isChecked())
            {
                hint.setChecked(false);
                if(hintnicknames.getVisibility() == View.VISIBLE)
                {
                    hintnicknames.setVisibility(View.INVISIBLE);
                    hintnicknames.setText("");
                }
            }

            //This is where the programm goes when the mother added 2 nicknames and a new activity starts
            if (mother_add_count >2){
                Intent i=new Intent(this, RandomizeNicknames.class);

                i.putExtra("NAMES_NICKNAMES",names_nicknames);
                i.putExtra("MOTHER",motherName[0]);
                i.putExtra("MOTHERNAMES", mother_nicknames);

                this.startActivity(i);

            }
            else if(names.getText().toString().replaceAll("\\s","").equals("") || nicknames.getText().toString().replaceAll("\\s","").equals(""))
            {
                click_count--;
                if(click_count>=number_of_players){
                    Toast.makeText(EnterNamesOfPlayers.this, "Please insert a nickname", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EnterNamesOfPlayers.this, "Please insert both a name and a nickname", Toast.LENGTH_SHORT).show();
                }
            }
            else if (names_nicknames.containsKey(names.getText().toString().replaceAll("\\s","")))
            {
                click_count--;
                Toast.makeText(EnterNamesOfPlayers.this, "The name : " + names.getText().toString() + " already exists, real names must be unique.", Toast.LENGTH_SHORT).show();
                names.setText("");
            }
            else if (names_nicknames.containsValue(nicknames.getText().toString().replaceAll("\\s","")))
            {
                click_count--;
                Toast.makeText(EnterNamesOfPlayers.this, "Wow duplicate nickname that's rare! Put another nickname for the game to progress", Toast.LENGTH_SHORT).show();
                nicknames.setText("");

            }
            //Here we check if all players have clicked the submit button, thus they entered they credentials and we randomly choose the mother
            else if(click_count == number_of_players)
            {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                floatinghint1.setHint("");
                floatinghint2.setHint("");
                activity_title.setText("Now give the device to the \"mother\" and the game begins, have fun .");
                if(hintnicknames.getVisibility() == View.VISIBLE)
                {
                    hintnicknames.setVisibility(View.INVISIBLE);
                    hintnicknames.setText("");
                }
                if(hint.getVisibility() == View.VISIBLE)
                {
                    hint.setVisibility(View.INVISIBLE);
                }
                submit_names.setText("OK !");
                names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                Random random = new Random();
                List<String> keys = new ArrayList<>(names_nicknames.keySet());
                motherName[0] = keys.get(random.nextInt(keys.size()));
                Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" submitted his nickname, pass the phone to " + motherName[0] + ".", Toast.LENGTH_SHORT).show();

                names.setText("The mother is :");
                nicknames.setText(motherName[0]+" !");


                names.setFocusable(false);
                nicknames.setFocusable(false);

            }
            //Here as the mother is chosen the UI dynamically changes again by counting the clicks on the submit button and we ask for 2 more nicknames from the mother.
            else if(click_count > number_of_players){
                //Open new activity with known orginizer and all nicknames and names in hashmap
                nicknames.setFocusableInTouchMode(true);
                activity_title.setText("Mother has to submit 2 more nicknames.");
                floatinghint2.setHint("Insert nickname");
                if(hint.getVisibility() == View.INVISIBLE)
                {
                    hint.setVisibility(View.VISIBLE);
                }
                names.setVisibility(View.GONE);


                submit_names.setText("SUBMIT");
                if(mother_add_count <2&& click_count > number_of_players +1) {
                    mother_add_count++;

                    if (names_nicknames.containsValue(nicknames.getText().toString().replaceAll("\\s", ""))) {
                        click_count--;
                        mother_add_count--;
                        Toast.makeText(EnterNamesOfPlayers.this, "Wow duplicate nickname that's rare! Put another nickname for the game to progress", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        mother_nicknames.add(nicknames.getText().toString());
                        Toast.makeText(EnterNamesOfPlayers.this, "Nickname : " + nicknames.getText().toString() + " added successfully.", Toast.LENGTH_SHORT).show();

                    }
                    if (mother_add_count == 2)
                    {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert inputManager != null;
                        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        mother_add_count++;
                        activity_title.setText("");
                        activity_title.setText("They game shall now start.");
                        nicknames.setVisibility(View.GONE);
                        submit_names.setText("OK !");
                        if(gifanimation.getVisibility() == View.INVISIBLE)
                        {
                            gifanimation.setVisibility(View.VISIBLE);
                        }
                        if(hintnicknames.getVisibility() == View.VISIBLE)
                        {
                            hintnicknames.setVisibility(View.INVISIBLE);
                        }
                        if(hint.getVisibility() == View.VISIBLE)
                        {
                            hint.setVisibility(View.INVISIBLE);
                        }
                    }

                }
                nicknames.setText("");

                //we print the hashmap to see if evrything is ok !
//                names_nicknames.entrySet().forEach(stringStringEntry -> {
//                    System.out.println(stringStringEntry.getKey()+" "+ stringStringEntry.getValue());
//                });
            }
            else {
                names_nicknames.put(names.getText().toString().replaceAll("\\s",""),nicknames.getText().toString().replaceAll("\\s",""));
                Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" submitted his nickname, pass the phone to the next player.", Toast.LENGTH_SHORT).show();
                names.setText("");
                nicknames.setText("");
            }

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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


}

