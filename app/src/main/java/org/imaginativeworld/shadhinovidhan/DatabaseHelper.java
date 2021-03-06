/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    // Table Name
    public static final String TABLE_NAME = "ovidhan";
    public static final String TABLE_FAVORITE_NAME = "favorite";

    // Table columns
    public static final String _WORD = "_id"; // It must be "_id", or many error happened.. :(
    public static final String SO_PRON = "pron";
    public static final String SO_MEANING = "meaning";
    public static final String SO_SYNONYMS = "synonyms";
    public static final String SO_NEW = "new";
    public static final String SO_MODIFY = "modify";

    public static final String SO_FAVORITE = "word";

    // Database Information //
    static final String DB_NAME = "IWSO.DB"; // DON'T USE UNDERSCORE ("_") in database name.. :3

    /**
     * DB_VERSION : database version
     * <p>
     * ********[ HISTORY ]*********
     * <p>
     * DB_version: App_version : [Total DB entry] (file_name) Comments
     * --------------------------------------------------------------------------
     * 1: 1.0 to 1.1
     * 2: 1.2 to 1.3
     * 3: 1.4 : New Word added, semi-colonize
     * 4: 1.5 : (IWSO_2.3) Remove [favorite] table and make Favourite independent.. :)
     * 5: 1.6 : [19539] (IWSO_2.4) New entry added and old entry corrected
     * 6: 2.0 to -.- : [25799] (IWSO_3.0) New Database rule, 6686 (6267 unique) new entry (from GT)
     * 7: Fix: Android 9 Pie crash problem.
     */
    private static final int DB_VERSION = 7;

    //The Android's default system path of your application database.
    private static String DB_PATH =
            //"/data/data/org.imaginativeworld.shadhinovidhan/databases/";
            appContextHelper.getAppContext().getDatabasePath(DB_NAME).getAbsolutePath();

    private final Context myContext;

    private SQLiteDatabase myDataBase;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application
     * assets and resources.
     *
     * @param context app context
     */
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;

    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myContext);

            if (settings.getBoolean(myContext.getString(R.string.pref_update_db), false)) {

                this.getReadableDatabase();

                // Fix: Android 9 Pie crash problem.
                this.close();

                try {

                    copyDataBase();


                } catch (IOException e) {

                    throw new Error(e.getMessage());

                }
            }

        } else {

            this.getReadableDatabase();

            // Fix: Android 9 Pie crash problem.
            this.close();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error(e.getMessage());

            }
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

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        try {

            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);


            // Path to the just created empty db
            String outFileName = DB_PATH; // DB_PATH + DB_NAME;


            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);


            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception e) {
            Log.e("soa", e.getMessage());
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myContext);
        settings.edit().putBoolean(myContext.getString(R.string.pref_update_db), false).apply();

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

        if (oldVersion < newVersion) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(myContext);
            settings.edit().putBoolean(myContext.getString(R.string.pref_update_db), true).apply();
            //settings.edit().putBoolean(myContext.getString(R.string.pref_is_fav_clear_notify_read), false).apply();


            new AlertDialog.Builder(myContext)
                    .setTitle(myContext.getString(R.string.app_name) + " has new database update!")
                    .setMessage("Please restart " + myContext.getString(R.string.app_name) + " to upgrade database.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) myContext).finishAffinity();
                        }
                    })
                    .show();
        }


    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        // Fix: Android 9 Pie crash problem.
        db.disableWriteAheadLogging();
    }
}





























