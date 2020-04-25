package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        Button buttonback =  findViewById(R.id.buttonbackhelp);
        buttonback.setOnClickListener(v -> goback());

    }

    public void goback()
    {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
