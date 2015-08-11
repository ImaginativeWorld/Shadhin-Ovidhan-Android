package org.imaginativeworld.shadhinovidhan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by Shohag on 24 Jul 15.
 */
public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);

        try {

            dbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("@strings/unable_create_database");

        }

        try {

            dbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String _word, String _pos, String _meaning) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper._WORD, _word);
        contentValue.put(DatabaseHelper.SO_POS, _pos);
        contentValue.put(DatabaseHelper.SO_MEANING, _meaning);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor searchEN(String _word) {

        String[] tableColumns = new String[] {
                DatabaseHelper._WORD,
                DatabaseHelper.SO_POS,
                DatabaseHelper.SO_MEANING
        };
        String whereClause = DatabaseHelper._WORD + " like ?";
        String[] whereArgs = new String[] {
                _word + "%"
        };
        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        // since we have a named column we can do
        //int idx = c.getColumnIndex("max");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public Cursor searchBN(String _word) {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD,
                DatabaseHelper.SO_POS,
                DatabaseHelper.SO_MEANING
        };
        String whereClause =
                DatabaseHelper.SO_MEANING + " like ?";
        String[] whereArgs = new String[]{
                "%" + _word + "%"
        };
        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        // since we have a named column we can do
        //int idx = c.getColumnIndex("max");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public int update(String _word, String _pos, String _meaning) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.SO_POS, _pos);
        contentValues.put(DatabaseHelper.SO_MEANING, _meaning);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._WORD + " = \"" + _word + "\"", null);
        return i;
    }

//    public void delete(long _id) {
//        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._WORD + "=" + _id, null);
//    }

}



















