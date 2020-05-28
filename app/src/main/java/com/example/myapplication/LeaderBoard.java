package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * That's the activity for the database to display the database contains the score of all players as it is a leaderboard it is ordered by  the highest to the lowest score
 */
public class LeaderBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        TextView leaderboard = findViewById(R.id.textLeaderboard);
        Button menu = findViewById(R.id.buttonmenu);
        leaderboard.setMovementMethod(new ScrollingMovementMethod());

        ArrayList<Player> arrayList = (ArrayList<Player>) getIntent().getSerializableExtra("LEADERBOARD");

        leaderboard.append("( Name ----> Score )");

        for(int i=0;i<arrayList.size();i++)
        {
            leaderboard.append("\n" + (i+1) + " : " + arrayList.get(i).getName() + " ----> " + arrayList.get(i).getScore());
        }

        menu.setOnClickListener(v -> {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        });

    }
}
