package org.imaginativeworld.shadhinovidhan;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by Shohag on 26 Jan 16.
 */
public class restore_activity extends Activity implements View.OnClickListener {

    final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 510;
    //Views
    TextView txtViewDbInfo, txtViewFavInfo;
    Button btnOK;
    boolean ask, isUpdate;
    DBManager dbManager;
    private String fileName;
    private String fileNameFav;
    private String folderName;
    private String[] data = new String[10];

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
        btnOK.setOnClickListener(restore_activity.this);

        //check permission for Marshmallow
        if (ContextCompat.checkSelfPermission(restore_activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Log.v("soa", "Permission denied");

            ActivityCompat.requestPermissions(restore_activity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);

            txtViewDbInfo.setText(getString(R.string.allow_parmission_for_retriving_backup));

        } else {

            if (isExternalStorageReadable()) {
                restoreBackupEntries();
                restoreFavBackupEntries();
            } else {
                txtViewDbInfo.setText(getString(R.string.external_storage_not_readable));
            }

        }


        //finish();


    }

    private void restoreBackupEntries() {
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        //Log.v("soa", Environment.getExternalStorageDirectory().getPath());

        if (!file.exists()) {
            if (!file.mkdirs()) {

                txtViewDbInfo.setText(getString(R.string.backup_folder_not_found));

                return;
            }
        }


        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath() + "/" + fileName);
            int n = fileInputStream.available();
            byte[] b = new byte[n];
            fileInputStream.read(b);

            String str = new String(b, 0, n);

            fileInputStream.close();

            dbManager = new DBManager(this);

            dbManager.open();

            //Log.v("soa", str);
            int i, counter = 0, not_count = 0;
            ask = true;
            isUpdate = false;
            long result;
            StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
            while (stringTokenizer.hasMoreElements()) {
                StringTokenizer strTok = new StringTokenizer((String) stringTokenizer.nextElement(), "|");
                i = 0;
                while (strTok.hasMoreElements()) {
                    data[i] = (String) strTok.nextElement();
                    i++;
                }
                //Log.v("soa", data[0] + " " +data[1] + " "+data[2] + " "+data[3]);
                result = dbManager.insert(data[0], data[1], data[2]);
                if (result == -1) {
                    not_count++;
                } else
                    counter++;

            }

            dbManager.close();


            txtViewDbInfo.setText(
                    String.format(getString(R.string.total_entry_restored_summery),
                            String.valueOf(counter), String.valueOf(not_count)));

        } catch (IOException e) {
            //Log.v("soa", "File write failed: " + e.toString());

            txtViewDbInfo.setText(String.format(getString(R.string.no_backup_file_found), e.toString()));
        }

    }

    private void restoreFavBackupEntries() {
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        //Log.v("soa", Environment.getExternalStorageDirectory().getPath());

        if (!file.exists()) {
            if (!file.mkdirs()) {

                txtViewFavInfo.setText(getString(R.string.backup_folder_not_found));

                return;
            }
        }


        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath() + "/" + fileNameFav);
            int n = fileInputStream.available();
            byte[] b = new byte[n];
            fileInputStream.read(b);

            String str = new String(b, 0, n);

            fileInputStream.close();

            dbManager = new DBManager(this);

            dbManager.open();

            //Log.v("soa", str);
            int counter = 0, not_count = 0;
            ask = true;
            isUpdate = false;
            long result;

            StringTokenizer stringTokenizer = new StringTokenizer(str, "\n");
            while (stringTokenizer.hasMoreElements()) {

                str = (String) stringTokenizer.nextElement();

                result = dbManager.insertIntoFavorite(str);
                if (result == -1) {
                    not_count++;
                } else
                    counter++;

            }

            dbManager.close();


            txtViewFavInfo.setText(
                    String.format(getString(R.string.total_fav_entry_restored_summery),
                            String.valueOf(counter), String.valueOf(not_count)));

        } catch (IOException e) {
            //Log.v("soa", "File write failed: " + e.toString());

            txtViewFavInfo.setText(String.format(getString(R.string.no_backup_file_found), e.toString()));
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
                    //Log.v("soa", "Permission not Granted!");
                }
                break;
            }

        }
    }


    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
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
