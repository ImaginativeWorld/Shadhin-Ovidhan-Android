/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

public class DbHelperFavorites extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "sofavorites.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE [favorite] (" +
                    "[_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "[word] CHAR(255)  UNIQUE NOT NULL" +
                    ");";
    //The Android's default system path of your application database.
    private static String DB_PATH =
            //"/data/data/org.imaginativeworld.shadhinovidhan/databases/";
            appContextHelper.getAppContext().getDatabasePath(DB_NAME).getAbsolutePath();

    private SQLiteDatabase myDataBase;

    public DbHelperFavorites(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist) {

            // Gets the data repository in write mode
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL(SQL_CREATE_ENTRIES);

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH; // DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null;
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH; //DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
