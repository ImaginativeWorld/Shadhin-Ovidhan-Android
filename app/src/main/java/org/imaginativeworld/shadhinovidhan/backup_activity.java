package org.imaginativeworld.shadhinovidhan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * Created by Shohag on 26 Jan 16.
 *
 */
public class backup_activity extends Activity implements View.OnClickListener {

    final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 510;
    //Views
    TextView txtViewDbInfo, txtViewFavInfo;
    Button btnOK;
    private String saveStr;
    private int total = 0;
    private String fileName;
    private String fileNameFav;
    private String folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.backup_restore_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        fileName = getString(R.string.backup_file_name);
        fileNameFav = getString(R.string.backup_fav_file_name);

        folderName = getString(R.string.backup_directory);

        txtViewDbInfo = (TextView) findViewById(R.id.txtInfoDb);
        txtViewFavInfo = (TextView) findViewById(R.id.txtInfoFav);
        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(backup_activity.this);

        //check permission for Marshmallow
        if (ContextCompat.checkSelfPermission(backup_activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Log.v("soa", "Permission denied");

            ActivityCompat.requestPermissions(backup_activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);

            txtViewDbInfo.setText(getString(R.string.allow_parmission_for_creating_backup));

        } else {

            if (isExternalStorageWritable()) {

                makeDatabaseBackup();
                makeFavListBackup();

            } else {
                txtViewDbInfo.setText(getString(R.string.external_storage_not_writable));
            }
        }

    }


    private void makeDatabaseBackup() {

        DBManager dbManager = new DBManager(this);

        dbManager.open();

        Cursor cursor;

        cursor = dbManager.getBackupData();

        dbManager.close();

        total = cursor.getCount();

        if (total == 0) {

            txtViewDbInfo.setText(getString(R.string.no_new_modified_entry_found));
            cursor.close();

        } else {

            //WORD | POS | MEANING | SYNONYMS

            saveStr = "";

            do {
                String tempStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SO_PRON)) + "|" +
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SO_POS)) + "|" +
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SO_MEANING)) + "|" +
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SO_SYNONYMS));

                saveStr += tempStr + "\n";

            } while (cursor.moveToNext());

            cursor.close();

            /**
             * Save to file
             */

            File file = new File(Environment.getExternalStorageDirectory(), folderName);
            //Log.v("soa", Environment.getExternalStorageDirectory().getPath());

            if (!file.exists()) {
                if (!file.mkdirs()) {

                    txtViewDbInfo.setText(getString(R.string.file_not_created));

                    return;

                    //Log.v("soa", "File Not Created!");
                }
            }

            //Log.v("soa", file.getAbsolutePath());


            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath() + "/" + fileName);
                fileOutputStream.write(saveStr.getBytes());
                fileOutputStream.close();

                txtViewDbInfo.setText(
                        String.format(getString(R.string.entry_backup_summery),
                                String.valueOf(total), file.getAbsolutePath(), fileName));


            } catch (IOException e) {

                txtViewDbInfo.setText(String.format(getString(R.string.file_write_failed), e.toString()));

                // Log.v("soa", "File write failed: " + e.toString());
            }

        }

    }


    private void makeFavListBackup() {

        DBManager dbManager = new DBManager(this);

        dbManager.open();

        Cursor cursor;

        cursor = dbManager.getFavBackupData();

        dbManager.close();

        total = cursor.getCount();

        if (total == 0) {

            txtViewFavInfo.setText(getString(R.string.no_fav_entry_found));
            cursor.close();

        } else {

            saveStr = "";

            do {
                String tempStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SO_FAVORITE));

                saveStr += tempStr + "\n";

            } while (cursor.moveToNext());

            cursor.close();

            /**
             * Save to file
             */

            File file = new File(Environment.getExternalStorageDirectory(), folderName);
            //Log.v("soa", Environment.getExternalStorageDirectory().getPath());

            if (!file.exists()) {
                if (!file.mkdirs()) {

                    txtViewFavInfo.setText(getString(R.string.file_not_created));

                    return;

                    //Log.v("soa", "File Not Created!");
                }
            }

            //Log.v("soa", file.getAbsolutePath());


            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath() + "/" + fileNameFav);
                fileOutputStream.write(saveStr.getBytes());
                fileOutputStream.close();

                txtViewFavInfo.setText(
                        String.format(getString(R.string.entry_fav_backup_summery),
                                String.valueOf(total), file.getAbsolutePath(), fileNameFav));


            } catch (IOException e) {

                txtViewFavInfo.setText(String.format(getString(R.string.file_write_failed), e.toString()));

                // Log.v("soa", "File write failed: " + e.toString());
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Log.v("soa", "Permission Granted!");

                    android.os.Process.killProcess(android.os.Process.myPid());

                } else {

                    txtViewDbInfo.setText(getString(R.string.permission_not_granted));

//                    Log.v("soa", "Permission not Granted!");
                }
                break;
            }

        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                finish();
                break;
        }
    }
}
