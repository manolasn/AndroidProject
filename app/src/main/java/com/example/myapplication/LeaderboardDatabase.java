package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderboardDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="Leaderboard";
    public static final String TABLE = "score_table";
    public static final String KEY_NAME="NAME";
    public static final String KEY_SCORE="SCORE";


    public LeaderboardDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + KEY_NAME +" TEXT PRIMARY KEY," + KEY_SCORE + " INTEGER" + ")";




        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);

        onCreate(db);
    }

    void addPlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(KEY_NAME,player.getName());
        values.put(KEY_SCORE,player.getScore());

        db.insert(TABLE,null,values);
        db.close();

    }

    Player getPlayer(String id){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.query(TABLE,new String[] {KEY_NAME,KEY_SCORE},KEY_NAME
        +"=?",new String[] {String.valueOf(id)},KEY_NAME+" DESC ",null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Player player = new Player(cursor.getString(0),cursor.getInt(1));
        cursor.close();
        db.close();
        return player;
    }

    public List<String> getAllNames(){
        List<String> playerList=new ArrayList<>();

        String selectQuery = "SELECT NAME FROM " + TABLE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{KEY_NAME, KEY_SCORE},KEY_SCORE,null,null,null,KEY_SCORE+" desc");

        if (cursor.moveToFirst()){
            do{
               playerList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        for(int i =0;i<playerList.size();i++){
            System.out.println(playerList.get(i));
        }

        cursor.close();
        db.close();

        return playerList;
    }

    @SuppressLint("Recycle")
    public HashMap<String,Integer> getAll(){
        HashMap<String,Integer> playerList=new HashMap<>();

        String selectQuery = "SELECT NAME,SCORE FROM score_table ORDER BY SCORE DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        //db.rawQuery( " select NAME,SCORE from "+TABLE+" ORDER BY "+KEY_SCORE+" DESC", null );
        Cursor cursor = db.query(TABLE, new String[]{KEY_NAME, KEY_SCORE},KEY_SCORE,null,null,null,KEY_SCORE+" desc");



        if (cursor.moveToFirst()){
            do{
                playerList.put(cursor.getString(0),cursor.getInt(1));

                System.out.println(cursor.getString(0)+"   "+cursor.getInt(1));

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return playerList;
    }





    public int updatePlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SCORE,player.getScore());



        return db.update(TABLE, values,KEY_NAME+"=?", new String[]{player.getName()});




    }



}
