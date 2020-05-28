package com.example.myapplication;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int score;


    public Player(){

    }

    public Player(int score)
    {
        this.score=score;
    }

    Player(String name, int score)
    {

        this.name=name;
        this.score=score;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
