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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Shohag on 26 Jul 15.
 *
 */
public class add_new_entry extends Activity implements OnClickListener {

    public int POSITION;
    Boolean IsSendToServer;
    HashMap<String, String> hashMap;
    Button btnAddMeaning, btnDelMeaning;
    ListView listView;
    ArrayList<String> listItemsMeaning = new ArrayList<>();
    ArrayAdapter<String> adapterMeaning;
    private ImageButton btnAdd, btnClose;
    private EditText wordEditText;
    // private CheckBox chkBoxSend;
    private EditText posEditText;
    private DBManager dbManager;
    private Boolean isDBchanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.add_entry_title));

        setContentView(R.layout.add_entry_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //=================================================================

        dbManager = new DBManager(this);
        dbManager.open();

        hashMap = new HashMap<>();

        //=================================================================

        wordEditText = (EditText) findViewById(R.id.txt_edit_word);
        posEditText = (EditText) findViewById(R.id.txt_edit_pos);
        //meaningEditText = (EditText) findViewById(R.id.txt_edit_meaning);

        btnAddMeaning = (Button) findViewById(R.id.btn_add_meaning);
        btnAddMeaning.setOnClickListener(add_new_entry.this);

        btnDelMeaning = (Button) findViewById(R.id.btn_del_meaning);
        btnDelMeaning.setOnClickListener(add_new_entry.this);

        //=================================================================

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(add_new_entry.this);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(add_new_entry.this);

        listView = (ListView) findViewById(R.id.listMeaning);

        //=================================================================

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);

        //=================================================================

        adapterMeaning = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view,
                listItemsMeaning);
        listView.setAdapter(adapterMeaning);

        TextView txtDefaultText = (TextView) findViewById(R.id.txtView_no_meaning);

        listView.setEmptyView(txtDefaultText);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POSITION = position;
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add:

                //===================
                String meaning;
                int len, i;

                len = listView.getAdapter().getCount();
                if (len > 0) {
                    meaning = listView.getItemAtPosition(0).toString();
                    for (i = 1; i < len; i++) {
                        if (!listView.getItemAtPosition(i).toString().equals("") &&
                                !listView.getItemAtPosition(i).toString().equals(getString(R.string.new_item_text)))
                            meaning += "; " + listView.getItemAtPosition(i).toString();
                    }
                } else
                    meaning = "";
                //====================

                final String word = wordEditText.getText().toString();
                final String pos = posEditText.getText().toString();
                //final String meaning = meaningEditText.getText().toString();

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
                                        listItemsMeaning.clear();
                                        adapterMeaning.notifyDataSetChanged();

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

            case R.id.btn_add_meaning:


                AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);
                builder.setTitle(getString(R.string.ui_txt_edit_meaning));

                // Set up the input
                //final EditText input = new EditText(add_new_entry.this);
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                // Get the layout inflater
                View view = (LayoutInflater.from(add_new_entry.this)).inflate(R.layout.input_alert_dialog_layout, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view);
                final EditText userInput = (EditText) view.findViewById(R.id.txtInput);

                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!userInput.getText().toString().equals(""))
                            adapterMeaning.add(userInput.getText().toString().replace("\n", " ").replace("\r", " "));

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


                break;

            case R.id.btn_del_meaning:
                if (!listItemsMeaning.isEmpty() && POSITION >= 0 && POSITION < listItemsMeaning.size()) {
                    listItemsMeaning.remove(POSITION);
                    adapterMeaning.notifyDataSetChanged();
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

            Toast t = Toast.makeText(add_new_entry.this,
                    getString(R.string.txt_new_entry_sent_to_server), Toast.LENGTH_SHORT);
            t.show();
        }


//        else {
        //Keep Silent :)
        //textview.setText("No network connection available.");
//        }
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



























