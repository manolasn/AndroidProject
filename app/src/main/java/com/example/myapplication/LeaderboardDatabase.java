package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import java.util.List;

/**
 * That is the class that implements the database of the game
 */
public class LeaderboardDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="Leaderboard";
    private static final String TABLE = "score_table";
    private static final String KEY_NAME="NAME";
    private static final String KEY_SCORE="SCORE";


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

    /**
     * We add a player to the database with this method
     * @param player is a struct that has name and score
     */
    void addPlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(KEY_NAME,player.getName());
        values.put(KEY_SCORE,player.getScore());

        db.insert(TABLE,null,values);
        db.close();

    }

    /**
     * This method returns a player, his name and his score
     * @param id the player name that we are looking as the name is the key on our database
     * @return the player that we are looking for
     */
    Player getPlayer(String id){
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.query(TABLE,new String[] {KEY_NAME,KEY_SCORE},KEY_NAME +"=?",new String[] {String.valueOf(id)},null,null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Player player = new Player(cursor.getString(0),cursor.getInt(1));

        cursor.close();
        db.close();
        return player;
    }


    /**
     *  This method return a list of all players' names that are in the database
     * @return the list of players
     */
    List<String> getAllNames(){
        List<String> playerList=new ArrayList<>();

        String selectQuery = "SELECT NAME FROM " + TABLE ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

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

    /**
     * This method returns all players and their scores
     * @return an arraylist of all players
     */
    ArrayList<Player> getAll(){
        ArrayList<Player> playerList=new ArrayList<>();

        String selectQuery = "SELECT NAME,SCORE FROM score_table ORDER BY SCORE DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT NAME,SCORE FROM score_table",null);




        if (cursor.moveToFirst()){
            do{
                playerList.add(new Player(cursor.getString(0),cursor.getInt(1)));

//                System.out.println(cursor.getString(0)+"   "+cursor.getInt(1));

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return playerList;
    }


    /**
     * This class is used to update an existing player
     * @param player a player that has scored
     */
    void updatePlayer(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SCORE,player.getScore());


        db.update(TABLE, values, KEY_NAME + "=?", new String[]{player.getName()});


    }



}
