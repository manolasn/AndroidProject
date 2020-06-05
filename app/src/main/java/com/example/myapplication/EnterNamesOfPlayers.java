package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;


import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.os.IBinder;
import android.os.PowerManager;

import android.util.Log;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

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
    private View contextView ;
    private boolean mIsBound = false;
    private MusicService mServ;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assisting_Class.loadlocale(this);
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
        contextView = findViewById(R.id.enter_names_act);
        ToggleButton hint = findViewById(R.id.hint_button);


        names.setImeOptions(names.getImeOptions()| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        nicknames.setImeOptions(nicknames.getImeOptions()| EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        if(gifanimation.getVisibility() == View.VISIBLE)
        {
            gifanimation.setVisibility(View.INVISIBLE);
        }


        //From line 93 to 106 is auto fill name function
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

        //Some hints
        List<String> newList= Arrays.asList("Σκύλος", "Κομπολόι","source", "tooth", "κλειδί", "merit", "Νώε" ,"kettle", "play" ,"πρόβατο","precision", "Super Mario", "Αρχηγός" ,"equation" ,
                "χάμστερ", "transform" ,"Μήτσος", "trail", "reach", "αλεπουδάκι" ,"delicate", "τουτούκος" ,"recovery", "απολυμένος", "empire", "βλέμα", "post", "band", "υποβρύχιο" ,"πάντα",
                "delivery", "climate", "άγγελος", "number", "flourish", "increase", "καραγκιόζης", "φασολάδα","convince","bounce" ,"connection", "κοιμάμαι", "record", "hook","cross", "παριζάκι",
                "trade" ,"hostage", "java","menu", "οδηγός", "delete", "offspring" ,"examination" , "μαύρο", "πατέρας", "rob" ,"cherry" ,"hostile","αρχαίος",  "fit", "myth", "quote", "evaluate",
                "exposure","βράδυ", "warn", "consultation", "designer", "cooperate" ,"invite", "πυροσβεστήρας", "shape", "canvas" ,"chimpanzee" ,"ride","παπαπαπ", "πηγή", "δόντι", "αξία", "winner","βραστήρας",
                "παιχνίδι", "ακρίβεια", "εξίσωση","hide and seek", "μεταμόρφωση", "ίχνος", "hippo", "προσέγγιση", "παπαρούνα", "Kappa", "αυτοκρατορία "," ανάρτηση "," μπάντα "," παράδοση "," κλίμα "," αριθμός ",
                " περικαμψύλιο ", "game", " αύξηση "," ανοχή "," αναπήδηση ", "pacman", "σύνδεση", " ηχογράφηση ","firefly", " γάντζος ","game boy", "κορνίζα", "εμπόριο", "όμηρος", "rca", "διαγραφή", "απόγονος",
                "εξέταση","NickNames: the Game", "ληστεία", "ninja","κεράσι", "εχθρικός", "θηλιά","opera", "69", "μύθος", "απόσπασμα ","Fortnite", " αξιολόγηση ", "karate", " έκθεση "," προειδοποίηση ",
                " διαβούλευση ", "emulator"," συνεργασία ","Naruto", " πρόσκληση ","Einstein"," σχήμα ", "one hundred","Michael Jackson"," καμβάς ", "python"," χιμπατζής ","tsunami"," βόλτα ", "Γιώργος"," hyperX ");

        hint_nicknames.addAll(newList);

        names_nicknames.entrySet().forEach(stringStringEntry -> {
            hint_nicknames.remove(stringStringEntry.getValue());
        });


        Random rand=new Random();
        hint.setOnClickListener(v -> {

            if(hint_nicknames.size()<6)
            {
                hint_nicknames.clear();
                hint_nicknames.addAll(newList);
            }



            if(hint.isChecked()){

                if(hintnicknames.getVisibility() == View.INVISIBLE)
                {
                    hintnicknames.append(getResources().getString(R.string.some_nicks));

                    int i=0;
                    while (!hint_nicknames.isEmpty()&&i<6)
                    {
                        i++;
                        int ran=rand.nextInt(hint_nicknames.size());
                        hintnicknames.append("\n"+hint_nicknames.get(ran));
                        hint_nicknames.remove(ran);
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

            //Hide hint if a player left it open
            if(hint.isChecked())
            {
                hint.setChecked(false);
                if(hintnicknames.getVisibility() == View.VISIBLE)
                {
                    hintnicknames.setVisibility(View.INVISIBLE);
                    hintnicknames.setText("");
                }
            }

            //This is where the program goes when the mother added 2 nicknames and a new activity starts
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
                //Here it means that we are at the screen that mother adds 2 more nicknames
                if(click_count>=number_of_players){
                    Snackbar.make(contextView, getResources().getString(R.string.plz_insert_nick), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                            .getColor(R.color.colorAccent)).show();
                }
                else {
                    Snackbar.make(contextView,getResources().getString(R.string.plz_inster_both), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                            .getColor(R.color.colorAccent)).show();
                }
            }
            else if (names_nicknames.containsKey(names.getText().toString().replaceAll("\\s","")))
            {
                click_count--;
                Snackbar.make(contextView,getResources().getString(R.string.the_name) + names.getText().toString() + getResources().getString(R.string.already_exists), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();
                names.setText("");
            }
            else if (names_nicknames.containsValue(nicknames.getText().toString().replaceAll("\\s","")))
            {
                click_count--;
                Snackbar.make(contextView,getResources().getString(R.string.wow_dup_nick), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();
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
                activity_title.setText(getResources().getString(R.string.give_phon_mother));
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
                Snackbar.make(contextView,names.getText().toString() +getResources().getString(R.string.submited_pass_phon) + motherName[0], Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();

                names.setText(getResources().getString(R.string.mother_is));
                nicknames.setText(motherName[0]+" !");


                names.setFocusable(false);
                nicknames.setFocusable(false);

            }
            //Here as the mother is chosen the UI dynamically changes again by counting the clicks on the submit button and we ask for 2 more nicknames from the mother.
            else if(click_count > number_of_players){
                //Open new activity with known orginizer and all nicknames and names in hashmap
                nicknames.setFocusableInTouchMode(true);
                activity_title.setText(getResources().getString(R.string.mother_has_2_more));
                floatinghint2.setHint(getResources().getString(R.string.insert_nickname_here));
                if(hint.getVisibility() == View.INVISIBLE)
                {
                    hint.setVisibility(View.VISIBLE);
                }

                names.setVisibility(View.GONE);

                submit_names.setText(getResources().getText(R.string.submit));

                if(mother_add_count <2&& click_count > number_of_players +1) {
                    mother_add_count++;

                    if (names_nicknames.containsValue(nicknames.getText().toString().replaceAll("\\s", ""))) {
                        click_count--;
                        mother_add_count--;
                        Snackbar.make(contextView,getResources().getString(R.string.wow_dup_nick), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                                .getColor(R.color.colorAccent)).show();

                    }
                    else {

                        mother_nicknames.add(nicknames.getText().toString());
                        Snackbar.make(contextView,getResources().getString(R.string.the_nickname) + nicknames.getText().toString() + getResources().getString(R.string.added_success), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                                .getColor(R.color.colorAccent)).show();

                    }
                    if (mother_add_count == 2)
                    {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if(inputManager!=null) {
                            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        mother_add_count++;
                        activity_title.setText("");
                        activity_title.setText(getResources().getString(R.string.the_game_starts));
                        nicknames.setVisibility(View.GONE);
                        submit_names.setText("OK !");
                        //We use this external library : pl.droidsonroids.gif:android-gif-drawable:1.2.20 to use a GifView
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

                Snackbar.make(contextView,names.getText().toString() +getResources().getString(R.string.pass_to_next_player), Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.colorPrimaryDark)).setBackgroundTint(getResources()
                        .getColor(R.color.colorAccent)).show();

                names.setText("");
                nicknames.setText("");
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

    /**
     * this method is called when the configuration changes and we need to keep the locale of the current configuration to the next one
     * @param newConfig landscape or portrait
     */
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //int orientation = this.getResources().getConfiguration().orientation;

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Assisting_Class.loadlocale(EnterNamesOfPlayers.this);
            Log.e("On Config Change","LANDSCAPE");
        }else{
            Assisting_Class.loadlocale(EnterNamesOfPlayers.this);
            Log.e("On Config Change","PORTRAIT");
        }

    }



}

