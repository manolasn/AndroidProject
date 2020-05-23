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
    private int number_of_players;
    private EditText names,nicknames;
    private TextView activity_title;
    private ArrayList<String> mother_nicknames =new ArrayList<>();
    private Button submit_names;
    int click_count = 0 ;
    int mother_add_count = 0 ;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_of_players);
        number_of_players = getIntent().getIntExtra("NUM_PLAYERS",0);
        activity_title = findViewById(R.id.textView4);
        names = findViewById(R.id.realname);
        nicknames = findViewById(R.id.nickname);
        submit_names = findViewById(R.id.button5);
        final String[] motherName = {null};
        submit_names.setOnClickListener(v -> {
            click_count++;
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
            else if(click_count == number_of_players)
            {
                activity_title.setText("Now give the device to the \"mother\" and the game begins, have fun .");
                submit_names.setText("OK !");
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
            else if(click_count > number_of_players){
                //Open new activity with known orginizer and all nicknames and names in hashmap
                activity_title.setText("Mother have to submit 2 more nicknames.");
                names.setVisibility(View.GONE);
                nicknames.setActivated(true);
                nicknames.setBackgroundColor(Color.WHITE);

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
                        mother_add_count++;
                        activity_title.setText("");
                        activity_title.setText("They game shall now start.");
                        nicknames.setVisibility(View.GONE);
                        submit_names.setText("OK !");
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
}

