package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;

/**
 * Created by Shohag on 26 Jul 15.
 */
public class add_new_entry extends Activity implements OnClickListener {

    private ImageButton btnAdd, btnClose;

    private EditText wordEditText;
    private EditText posEditText;
    private EditText meaningEditText;

    // private CheckBox chkBoxSend;

    private DBManager dbManager;

    private Boolean isDBchanged = false;

    Boolean IsSendToServer;

    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Entry");

        setContentView(R.layout.add_entry_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //=================================================================

        dbManager = new DBManager(this);
        dbManager.open();

        hashMap = new HashMap<String, String>();

        //=================================================================

        wordEditText = (EditText) findViewById(R.id.txt_edit_word);
        posEditText = (EditText) findViewById(R.id.txt_edit_pos);
        meaningEditText = (EditText) findViewById(R.id.txt_edit_meaning);

        //=================================================================

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(add_new_entry.this);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(add_new_entry.this);

        //=================================================================

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add:

                final String word = wordEditText.getText().toString();
                final String pos = posEditText.getText().toString();
                final String meaning = meaningEditText.getText().toString();

                if (word.equals("") || pos.equals("") || meaning.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);

                    builder.setMessage(R.string.dialog_message_fill_all_field)
                            .setTitle(R.string.dialog_message_error)
                            .setPositiveButton(R.string.dialog_message_ok, null);

                    AlertDialog dialog = builder.create();

                    dialog.show();

                    break;

                } else {

                    long r = dbManager.insert(word, pos, meaning);

                    if (r != -1) {

                        isDBchanged = true;

                        if (IsSendToServer) {
                            sendData(getString(R.string.server_txt_new), word, pos, meaning);
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);

                        builder.setMessage(R.string.dialog_message_insert_successful)
                                .setTitle(R.string.dialog_message_successful)
                                .setPositiveButton(R.string.dialog_message_add_another_entry, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        wordEditText.setText("");
                                        posEditText.setText("");
                                        meaningEditText.setText("");

                                    }
                                })

                                .setNegativeButton(R.string.dialog_message_close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        finishWithResult();

                                    }
                                });

                        AlertDialog dialog = builder.create();

                        dialog.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);

                        builder.setMessage(R.string.dialog_message_insert_error)
                                .setTitle(R.string.dialog_message_error)
                                .setPositiveButton(R.string.dialog_message_ok, null);

                        AlertDialog dialog = builder.create();

                        dialog.show();
                    }

                }

                break;
            case R.id.btn_close:
                finish();
                break;
        }

    }

    void sendData(String info, String word, String pos, String meaning) {
        hashMap.clear();

        //?arg1=val1&arg2=val2
        hashMap.put("info", info);
        hashMap.put("word", so_tools.removeSymbolFromText(word));
        hashMap.put("pron", word);
        hashMap.put("pos", pos);
        hashMap.put("meaning", meaning);

        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new sendDataToServer(hashMap, getString(R.string.server_post_url));
        } else {
            //Keep Silent :)
            //textview.setText("No network connection available.");
        }
    }

    private void finishWithResult() {
        Bundle conData = new Bundle();
        conData.putBoolean("results", isDBchanged);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}



























