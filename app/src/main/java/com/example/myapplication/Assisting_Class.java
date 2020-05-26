package com.example.myapplication;
import java.util.HashMap;

/**
 * This is a simple class just to have access globally to some methods and variables
 */
class Assisting_Class {

        private static HashMap<String,Integer> scoreboard=new HashMap<>();

        private static boolean mute=false;


        static void setMute(boolean a){
            mute=a;
        }

        static boolean getMute(){
            return mute;
        }

        static void setScoreboard(HashMap<String,Integer> ascoreboard){
            scoreboard=ascoreboard;
        }

        static HashMap<String,Integer> getScoreboard()
        {
            return scoreboard;
        }

        static void clearScoreboard(){scoreboard.clear();}


}
