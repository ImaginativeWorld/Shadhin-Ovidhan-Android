package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shohag on 23 Aug 15.
 */
public class favorite_list_activity extends Activity implements View.OnClickListener {

    // for DB favorite
    final String[] from = new String[]{
            DatabaseHelper.SO_FAVORITE
    };
    final int[] to = new int[]{
            android.R.id.text1
    };
    ImageButton btnClose, btnDelete;
    int pOSITION;
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private String tEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorite_view_layout); // select default layout

        //============================================================

        dbManager = new DBManager(this);
        dbManager.open();

        //============================================================

        final Cursor cursor = null;

        listView = (ListView) findViewById(R.id.favorite_list);

        listView.setEmptyView(findViewById(R.id.empty)); // for empty view

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        adapter.changeCursor(dbManager.getFavorite());

        //============================================================

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                tEXT = text.getText().toString();
                //pOSITION = position;

                AlertDialog.Builder adb = new AlertDialog.Builder(favorite_list_activity.this);
                adb.setTitle("What do you want?");
                //adb.setMessage("?");

                adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbManager.deleteInfoFavorite(tEXT);
                        adapter.changeCursor(dbManager.getFavorite());
                        adapter.notifyDataSetChanged();

                    }
                });
                adb.setNegativeButton("Copy to\nClipboard", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //Copy to Clip Board (Only support API >=11)
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("SO_Meaning", tEXT);
                        clipboard.setPrimaryClip(clip);

                        Toast t = Toast.makeText(favorite_list_activity.this,
                                "Text Copied to Clipboard.",
                                Toast.LENGTH_LONG);
                        t.show();

                    }
                });
                adb.setPositiveButton("View\nmeaning", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishWithResult();
                    }
                });
                adb.show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private void finishWithResult() {
        Bundle conData = new Bundle();
        conData.putString("results", tEXT);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}























