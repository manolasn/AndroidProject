package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class EnterNamesOfPlayers extends AppCompatActivity {

    private HashMap<String,String> names_nicknames=new HashMap<>();
    private int players;
    private EditText names,nicknames;
    private Button submitnames;
    int clickcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names_of_players);
        players = getIntent().getIntExtra("NUM_PLAYERS",0);
        names = findViewById(R.id.realname);
        nicknames = findViewById(R.id.nickname);
        submitnames = findViewById(R.id.button5);
        submitnames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcount=clickcount+1;
                if(names.getText().toString().equals("") || nicknames.getText().toString().equals(""))
                {
                    Toast.makeText(EnterNamesOfPlayers.this, "Please insert both a name and a nickname", Toast.LENGTH_SHORT).show();
                }
                else if(clickcount==players)
                {
                    System.out.println("HELLLOOOOO WORLDDDDDDSDSDAS");
                    //HERE WE TERMINATE THE SUBMITS AS ALL THEY PLAYERS ARE OK !
                }

                else {
                    names_nicknames.put(names.getText().toString(),nicknames.getText().toString());
                }
            }
        });
    }
}

