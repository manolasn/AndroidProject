package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartGame extends AppCompatActivity {
    private int numberofplayers;
    private Button submit;
    private EditText numberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        numberInput =  findViewById(R.id.editText);
        submit = findViewById(R.id.button4);
        submit.setOnClickListener(v -> {
            if(numberInput.getText().toString().replaceAll("\\s","").equals("")) {
                Toast.makeText(StartGame.this, "Please insert number before submitting", Toast.LENGTH_SHORT).show();
            }
            else {

                numberofplayers = Integer.parseInt(numberInput.getText().toString());
                if(numberofplayers>2&&numberofplayers<11) {
                    Toast.makeText(StartGame.this, "Players submitted : " + numberofplayers, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,EnterNamesOfPlayers.class);
                    i.putExtra("NUM_PLAYERS",numberofplayers);
                    this.startActivity(i);
                }
                else{
                    Toast.makeText(StartGame.this, "The game is played with 3 to 10 people", Toast.LENGTH_SHORT).show();
                    numberInput.setText("");
                }

            }
        });

    }




}
