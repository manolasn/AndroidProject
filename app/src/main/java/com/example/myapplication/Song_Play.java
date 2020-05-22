package com.example.myapplication;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

class Song_Play {

        private static MediaPlayer mediaPlayer;
        private static SoundPool soundPool;
        static boolean isplayingAudio=false;
        static void playAudio(Context c){
            mediaPlayer = MediaPlayer.create(c, R.raw.mainsound);
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
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

}
