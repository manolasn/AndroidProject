package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
    private ArrayList<String> motherNicknames=new ArrayList<>();
    private Button submitnames;
    int clickcount=0;
    int mothernamescount=0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_of_players);
        players = getIntent().getIntExtra("NUM_PLAYERS",0);
        readyToPlay = findViewById(R.id.textView4);
        names = findViewById(R.id.realname);
        nicknames = findViewById(R.id.nickname);
        submitnames = findViewById(R.id.button5);
        final String[] motherName = {null};
        submitnames.setOnClickListener(v -> {
            clickcount++;
            if (mothernamescount>2){
                Intent i=new Intent(this, FinalGame.class);

                i.putExtra("NAMES_NICKNAMES",names_nicknames);
                i.putExtra("MOTHER",motherName[0]);
                i.putExtra("MOTHERNAMES",motherNicknames);

                this.startActivity(i);

            }
            else if(names.getText().toString().replaceAll("\\s","").equals("") || nicknames.getText().toString().replaceAll("\\s","").equals(""))
            {
                clickcount--;
                Toast.makeText(EnterNamesOfPlayers.this, "Please insert both a name and a nickname", Toast.LENGTH_SHORT).show();
            }
            else if (names_nicknames.containsKey(names.getText().toString().replaceAll("\\s","")))
            {
                clickcount--;
                Toast.makeText(EnterNamesOfPlayers.this, "The name : " + names.getText().toString() + " already exists, real names must be unique.", Toast.LENGTH_SHORT).show();
                names.setText("");
                nicknames.setText("");
            }
            else if(clickcount==players)
            {
                readyToPlay.setText("Now give the phone to the \"mother\" and the game begins, have fun !");
                submitnames.setText("OK !");
                names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                Random random = new Random();
                List<String> keys = new ArrayList<>(names_nicknames.keySet());
                motherName[0] = keys.get(random.nextInt(keys.size()));
                names.setActivated(false);
                nicknames.setActivated(false);
                nicknames.setBackgroundColor(Color.GREEN);
                Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" submitted his nickname, pass the phone to " + motherName[0] + ".", Toast.LENGTH_SHORT).show();
                names.setText("The mother is :");
                nicknames.setText(motherName[0]);

                //HERE WE TERMINATE THE SUBMITS AS ALL THEY PLAYERS ARE OK !
            }
            else if(clickcount>players){
                //Open new activity with known orginizer and all nicknames and names in hashmap
                readyToPlay.setText("Mother have to submit 2 more nicknames.");
                names.setVisibility(View.GONE);
                nicknames.setActivated(true);
                nicknames.setBackgroundColor(Color.WHITE);

                submitnames.setText("SUBMIT");
                if(mothernamescount<2&&clickcount>players+1) {
                    mothernamescount++;
                    motherNicknames.add(nicknames.getText().toString());
                    Toast.makeText(EnterNamesOfPlayers.this, "Nickname : " + nicknames.getText().toString() + " added successfully.", Toast.LENGTH_SHORT).show();
                    if (mothernamescount == 2) {
                        mothernamescount++;
                        readyToPlay.setText("They game shall now start.");
                        nicknames.setVisibility(View.GONE);
                        submitnames.setText("OK !");
                    }

                }
                nicknames.setText("");

                //we print the hashmap to see if evrything is ok !
//                names_nicknames.entrySet().forEach(stringStringEntry -> {
//                    System.out.println(stringStringEntry.getKey()+" "+ stringStringEntry.getValue());
//                });
            }
            else {
                names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                Toast.makeText(EnterNamesOfPlayers.this, names.getText().toString() +" submitted his nickname, pass the phone to the next player.", Toast.LENGTH_SHORT).show();
                names.setText("");
                nicknames.setText("");

            }




        });
    }
}

