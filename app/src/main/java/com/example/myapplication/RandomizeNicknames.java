package com.example.myapplication;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * This activity is used so the mother can randomize the nicknames before reading them aloud
 */
public class RandomizeNicknames extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private ArrayList<String> mother_nicknames =new ArrayList<>();
    private ArrayList<String> all_nicknames =new ArrayList<>();
    private ArrayList<String> temp=new ArrayList<>();
    private TextView activity_title;
    private boolean flag=true;
    private HomeWatcher mHomeWatcher;
    private boolean mIsBound = false;
    private MusicService mServ;



    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomize_nicknames);

        names_nicknames= (HashMap<String, String>) (getIntent().getSerializableExtra("NAMES_NICKNAMES"));
        String mother=getIntent().getStringExtra("MOTHER");

        mother_nicknames =(ArrayList<String>)(getIntent().getSerializableExtra("MOTHERNAMES"));

        if (mother_nicknames != null) {
            mother_nicknames.add(names_nicknames.get(mother));
        }

        activity_title =findViewById(R.id.textView9);
        Button randomize = findViewById(R.id.button3);
        Button done = findViewById(R.id.button_done);

        names_nicknames.remove(mother);

        all_nicknames.addAll(mother_nicknames);
        all_nicknames.addAll(names_nicknames.values());


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





        activity_title.append("Read the nicknames aloud 3 times :");
        for (int i = 0; i< all_nicknames.size(); i++)
        {
            activity_title.append( "\n" +  (i+1) + " : " + all_nicknames.get(i));
        }




        Random rand=new Random();
        //Randomize uses a random int and a temp arraylist and each time takes a random element from the given arraylist and displays it on screen
        randomize.setOnClickListener(v -> {

            int i = 0 ;
            if(flag) {
                flag=false;
                temp.addAll(all_nicknames);
            } else {
                all_nicknames.addAll(temp);
            }

            activity_title.setText("");

            activity_title.append("Read the nicknames aloud 3 times :");

            while (!all_nicknames.isEmpty())
            {
                i++;
                int ran=rand.nextInt(all_nicknames.size());
                activity_title.append( "\n" +  i  + " : " + all_nicknames.get(ran));
                all_nicknames.remove(ran);
            }


        });

        //Done button listener that leads to the next activity
        done.setOnClickListener(v -> {
            Intent i=new Intent(this, DisplayEverything.class);

            i.putExtra("NAMES_NICKNAMES",names_nicknames);
            i.putExtra("MOTHER",mother);
            i.putExtra("MOTHERNAMES", mother_nicknames);

            this.startActivity(i);
        });




//        This part was used for debugging
//
//        System.out.print("\n");
//        if(names_nicknames!=null) {
//            names_nicknames.entrySet().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + " " + stringStringEntry.getValue()));
//        }
//        System.out.print("\n");
//        mother_nicknames.forEach(s -> System.out.println("mother nickname : " + s));





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



}
