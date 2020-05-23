package com.example.myapplication;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

class Assisting_Class {

        private static HashMap<String,Integer> scoreboard=new HashMap<>();
        private static MediaPlayer mediaPlayer;

        static boolean isplayingAudio=false;
        static void playAudio(Context c){
            mediaPlayer = MediaPlayer.create(c, R.raw.mainsound);
            if(!mediaPlayer.isPlaying())
            {
                isplayingAudio=true;
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        }
        static void stopAudio(){
            isplayingAudio=false;
            mediaPlayer.stop();
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
