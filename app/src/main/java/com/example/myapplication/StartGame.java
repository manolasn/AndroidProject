package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class StartGame extends AppCompatActivity {
    private int number_of_players;
    private Button submit;
    private EditText activity_title;
    private HashMap<String,Integer> scoreboard=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        activity_title =  findViewById(R.id.editText);
        submit = findViewById(R.id.button4);



        submit.setOnClickListener(v -> {
            if(activity_title.getText().toString().replaceAll("\\s","").equals("")) {
                Toast.makeText(StartGame.this, "Please insert number before submitting", Toast.LENGTH_SHORT).show();
            }
            else {

                number_of_players = Integer.parseInt(activity_title.getText().toString());
                if(number_of_players >2&& number_of_players <11) {
                    Toast.makeText(StartGame.this, "Players submitted : " + number_of_players, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,EnterNamesOfPlayers.class);
                    i.putExtra("NUM_PLAYERS", number_of_players);
                    this.startActivity(i);
                }
                else{
                    Toast.makeText(StartGame.this, "The game is played with 3 to 10 people", Toast.LENGTH_SHORT).show();
                    activity_title.setText("");
                }

            }
        });

    }




}
