package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Locale;

/**
 * This is a simple class just to have access globally to some methods and variables
 */
class Assisting_Class {

        private static HashMap<String,Integer> scoreboard=new HashMap<>();
        private static boolean mute=false;
        private static boolean throwKonfetti=true;

        public static boolean ThrowKonfetti() {
            return throwKonfetti;
        }

        public static void setThrowKonfetti(boolean throwKonfetti) {
            Assisting_Class.throwKonfetti = throwKonfetti;
        }

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

         static void setLocale(String lang, Context context) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale=locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
            SharedPreferences.Editor editor= context.getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
            editor.putString("my_lang",lang);
            System.out.println(editor.commit());

        }

        //load  language saved in preferences
        static void loadlocale(Context context){
            SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
            String language = prefs.getString("my_lang", "");
            System.out.println(prefs.getString("my_lang", ""));
            setLocale(language,context);

        }




}
