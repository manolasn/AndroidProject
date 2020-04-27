package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class EnterNamesOfPlayers extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private int players;
    private EditText names,nicknames;
    private TextView readyToPlay;
    private Button submitnames;
    int clickcount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_of_players);
        players = getIntent().getIntExtra("NUM_PLAYERS",0);
        readyToPlay = findViewById(R.id.textView4);
        names = findViewById(R.id.realname);
        nicknames = findViewById(R.id.nickname);
        submitnames = findViewById(R.id.button5);
        submitnames.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                clickcount++;
                if(names.getText().toString().equals("") || nicknames.getText().toString().equals(""))
                {
                    clickcount--;
                    Toast.makeText(EnterNamesOfPlayers.this, "Please insert both a name and a nickname", Toast.LENGTH_SHORT).show();
                }
                else if (names_nicknames.containsKey(names.getText().toString()))
                {
                    clickcount--;
                    Toast.makeText(EnterNamesOfPlayers.this, "The name : " + names.getText().toString() + " already exists, real names must be unique.", Toast.LENGTH_SHORT).show();
                    names.setText("");
                    nicknames.setText("");
                }
                else if(clickcount==players)
                {
                    readyToPlay.setText("Ready to go, have fun !");
                    submitnames.setText("OK !");
                    names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                    Random random = new Random();
                    List<String> keys = new ArrayList<String>(names_nicknames.keySet());
                    String randomName = keys.get(random.nextInt(keys.size()));
                    names.setActivated(false);
                    nicknames.setActivated(false);
                    nicknames.setBackgroundColor(Color.GREEN);
                    names.setText("The organizer is :");
                    nicknames.setText(randomName);
                    Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" is playing as: "+ nicknames.getText().toString(), Toast.LENGTH_SHORT).show();
                    //HERE WE TERMINATE THE SUBMITS AS ALL THEY PLAYERS ARE OK !
                }
                else if(clickcount>players){
                    //Open new activity with known orginizer and all nicknames and names in hashmap
                    System.out.println("NEW ACTIVITY HERE");
                    //we print the hashmap to see if evrything is ok !
                    names_nicknames.entrySet().forEach(stringStringEntry -> {
                        System.out.println(stringStringEntry.getKey()+" "+ stringStringEntry.getValue());
                    });
                }
                else {
                    names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                    Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" is playing as: "+ nicknames.getText().toString(), Toast.LENGTH_SHORT).show();
                    names.setText("");
                    nicknames.setText("");

                }




            }
        });
    }
}

