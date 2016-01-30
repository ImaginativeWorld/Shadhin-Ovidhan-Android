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

            //Unfortunately without activity or service class you can't use resources.. :'(
            //But you can cheat to use resources.. ;)
            throw new Error(appContextHelper.getAppContext()
                    .getString(R.string.err_msg_unable_to_create_database));

        }

        dbHelper.openDataBase();

        //Log.v("soa", "getWritable");
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String _word, String _pos, String _meaning) {


        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper._WORD, so_tools.removeSymbolFromText(_word));
        contentValue.put(DatabaseHelper.SO_PRON, _word);
        contentValue.put(DatabaseHelper.SO_POS, _pos);
        contentValue.put(DatabaseHelper.SO_MEANING, _meaning);
        contentValue.put(DatabaseHelper.SO_NEW, true);

        // if error return -1
        return database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor searchEN(String _word, String type) {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD, //_id column must needed for cursor adaptor.. O.o
                DatabaseHelper.SO_PRON,
                DatabaseHelper.SO_POS,
                DatabaseHelper.SO_MEANING
        };

        String whereClause = DatabaseHelper._WORD + " like ?";

        String[] whereArgs;

        switch (type) {
            case "1":

                whereArgs = new String[]{
                        so_tools.removeSymbolFromText(_word) + "%"
                };

                break;
            case "2":

                whereArgs = new String[]{
                        "%" + so_tools.removeSymbolFromText(_word) + "%"
                };

                break;
            default:

                whereArgs = new String[]{
                        "%" + so_tools.removeSymbolFromText(_word)
                };

                break;
        }

        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public Cursor searchBN(String _word, String type) {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD,
                DatabaseHelper.SO_PRON,
                DatabaseHelper.SO_POS,
                DatabaseHelper.SO_MEANING
        };
        String whereClause =
                DatabaseHelper.SO_MEANING + " like ?";

        String[] whereArgs;

        switch (type) {
            case "1":

                whereArgs = new String[]{
                        _word + "%"
                };

                break;
            case "2":

                whereArgs = new String[]{
                        "%" + _word + "%"
                };

                break;
            default:

                whereArgs = new String[]{
                        "%" + _word
                };

                break;
        }

        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }


    public Cursor getBackupData() {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD, //_id column must needed for cursor adaptor.. O.o
                DatabaseHelper.SO_PRON,
                DatabaseHelper.SO_POS,
                DatabaseHelper.SO_MEANING,
                DatabaseHelper.SO_SYNONYMS
        };

        String whereClause =
                DatabaseHelper.SO_NEW + " =? OR " + DatabaseHelper.SO_MODIFY + " =?";

        String[] whereArgs = new String[]{"1", "1"};

        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public Cursor getFavBackupData() {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD, //_id column must needed for cursor adaptor.. O.o
                DatabaseHelper.SO_FAVORITE
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITE_NAME, tableColumns, null, null,
                null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public int update(String _word, String _pos, String _meaning) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SO_PRON, _word);
        contentValues.put(DatabaseHelper.SO_POS, _pos);
        contentValues.put(DatabaseHelper.SO_MEANING, _meaning);
        contentValues.put(DatabaseHelper.SO_MODIFY, true);

        return database.update(DatabaseHelper.TABLE_NAME, contentValues,
                DatabaseHelper._WORD + " = \"" + so_tools.removeSymbolFromText(_word) + "\"", null);
    }

    public int delete(String _word) {
        //Must use double quotation mark for string logic in sql language.
        return database.delete(DatabaseHelper.TABLE_NAME,
                DatabaseHelper._WORD + " = \"" + so_tools.removeSymbolFromText(_word) + "\"", null);
    }

    //================

    public Cursor getFavorite() {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD,
                DatabaseHelper.SO_FAVORITE
        };

        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITE_NAME, tableColumns, null, null,
                null, null, DatabaseHelper.SO_FAVORITE + " ASC");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;

    }

    public long insertIntoFavorite(String _word) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.SO_FAVORITE, _word);

        return database.insert(DatabaseHelper.TABLE_FAVORITE_NAME, null, contentValue);
    }

    public boolean isInFavorite(String _word) {

        String[] tableColumns = new String[]{
                DatabaseHelper._WORD
        };

        String whereClause =
                DatabaseHelper.SO_FAVORITE + " =?";

        String[] whereArgs = new String[]{_word};

        //String orderBy = "_id";
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITE_NAME, tableColumns, whereClause, whereArgs,
                null, null, null);

        return cursor.moveToFirst();

    }

    public int deleteFromFavorite(String _word) {

        //Must use double quotation mark for string logic in sql language.
        //it return the location from where the value was deleted. or return 0.
        return database.delete(DatabaseHelper.TABLE_FAVORITE_NAME,
                DatabaseHelper.SO_FAVORITE + " = \"" + so_tools.removeSymbolFromText(_word) + "\"", null);
    }

    public int deleteInfoFavorite(String _word) {
        //Must use double quotation mark for string logic in sql language.
        return database.delete(DatabaseHelper.TABLE_FAVORITE_NAME,
                DatabaseHelper.SO_FAVORITE + " = \"" + _word + "\"", null);
    }

}



















