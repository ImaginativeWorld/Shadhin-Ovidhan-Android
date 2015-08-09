package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shohag on 24 Jul 15.
 */
public class DatabaseHelper_Backup extends SQLiteOpenHelper {


    // Table Name
    public static final String TABLE_NAME = "ovidhan";

    // Table columns
    public static final String _WORD = "_id"; // It must be "_id", or many error happened.. :(
    public static final String SO_POS = "pos";
    public static final String SO_MEANING = "meaning";

    // Database Information
    static final String DB_NAME = "IWSO.DB"; //IWSOn.DB // Must NOT USE UNDERSCORE ("_") in database name

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
//            + " TEXT, " + SO_POS + " TEXT, " + SO_MEANING + " TEXT);";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _WORD
            + " TEXT PRIMARY KEY, " + SO_POS + " TEXT, " + SO_MEANING + " TEXT NOT NULL);";

    public DatabaseHelper_Backup(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
