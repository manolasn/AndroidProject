package com.example.myapplication;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FinalGame extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private ArrayList<String> motherNicknames=new ArrayList<>();
    private ArrayList<String> allNicks=new ArrayList<>();
    private ArrayList<String> temp=new ArrayList<>();
    private TextView motherNames;
    private Button randomize;
    private boolean flag=true;


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_game);

        names_nicknames= (HashMap<String, String>) (getIntent().getSerializableExtra("NAMES_NICKNAMES"));
        String mother=getIntent().getStringExtra("MOTHER");

        motherNicknames=(ArrayList<String>)(getIntent().getSerializableExtra("MOTHERNAMES"));

        if (motherNicknames != null) {
            motherNicknames.add(names_nicknames.get(mother));
        }

        motherNames=findViewById(R.id.textView9);
        randomize=findViewById(R.id.button3);

        names_nicknames.remove(mother);

        allNicks.addAll(motherNicknames);
        allNicks.addAll(names_nicknames.values());








        for (int i=0;i<allNicks.size();i++)
        {
            motherNames.append( "\n" +  (i+1) + " : " + allNicks.get(i));
        }
       // motherNames.setText(motherNicknames.get(0));



        Random rand=new Random();
        randomize.setOnClickListener(v -> {
            int i = 0 ;
            if(flag) {
                flag=false;
                temp.addAll(allNicks);
            } else {
                allNicks.addAll(temp);
            }

            motherNames.setText("");



            while (!allNicks.isEmpty())
            {
                i++;
                int ran=rand.nextInt(allNicks.size());
                motherNames.append( "\n" +  i  + " : " + allNicks.get(ran));
                allNicks.remove(ran);
            }


        });








        System.out.print("\n");
        if(names_nicknames!=null) {
            names_nicknames.entrySet().forEach(stringStringEntry -> System.out.println(stringStringEntry.getKey() + " " + stringStringEntry.getValue()));
        }
        System.out.print("\n");
        motherNicknames.forEach(s -> System.out.println("mother nickname : " + s));





    }
}
