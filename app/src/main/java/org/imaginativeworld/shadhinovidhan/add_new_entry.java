package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shohag on 26 Jul 15
 */
public class add_new_entry extends Activity implements OnClickListener {

    int POSITION;
    int POSITION_Syno;
    Boolean IsSendToServer;
    HashMap<String, String> hashMap;
    Button btnAddMeaning, btnDelMeaning;
    Button btnAddSynonyms, btnDelSynonyms;
    ListView listView;
    ListView listViewSyno;
    ArrayList<String> listItemsMeaning = new ArrayList<>();
    ArrayList<String> listItemsSynonyms = new ArrayList<>();
    ArrayAdapter<String> adapterMeaning;
    ArrayAdapter<String> adapterSynonyms;
    ImageButton btnAdd;
    ImageButton btnClose;
    EditText wordEditText;
    EditText posEditText;
    DBManager dbManager;
    Boolean isDBchanged = false;

    View DialogView;
    TextView subTitleDialog;

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

        btnAddSynonyms = (Button) findViewById(R.id.btn_add_synonyms);
        btnAddSynonyms.setOnClickListener(add_new_entry.this);

        btnDelSynonyms = (Button) findViewById(R.id.btn_del_synonyms);
        btnDelSynonyms.setOnClickListener(add_new_entry.this);

        //=================================================================

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(add_new_entry.this);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(add_new_entry.this);

        listView = (ListView) findViewById(R.id.listMeaning);
        listViewSyno = (ListView) findViewById(R.id.listSynonyms);

        //=================================================================

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);

        //=================================================================

        TextView txtDefaultText = (TextView) findViewById(R.id.txtView_no_meaning);

        listView.setEmptyView(txtDefaultText);

        TextView txtDefaultTextSyno = (TextView) findViewById(R.id.txt_empty_syno);

        listViewSyno.setEmptyView(txtDefaultTextSyno);

        //=================================================================

        adapterMeaning = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view,
                listItemsMeaning);
        listView.setAdapter(adapterMeaning);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POSITION = position;
            }
        });

        //=================================================================

        adapterSynonyms = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view,
                listItemsSynonyms);
        listViewSyno.setAdapter(adapterSynonyms);

        listViewSyno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                POSITION_Syno = position;
            }
        });

        //=================================================================


        // Get the layout inflater
        DialogView = (LayoutInflater.from(add_new_entry.this)).inflate(R.layout.input_alert_dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        subTitleDialog = (TextView) DialogView.findViewById(R.id.txtDialogueSubtitle);
        //subTitleDialog.setText(getString(R.string.enter_new_synonyms));


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

                String synonyms;
                len = listViewSyno.getAdapter().getCount();
                if (len > 0) {
                    synonyms = listViewSyno.getItemAtPosition(0).toString();
                    for (i = 1; i < len; i++) {
                        if (!listViewSyno.getItemAtPosition(i).toString().equals("") &&
                                !listViewSyno.getItemAtPosition(i).toString().equals(getString(R.string.new_item_text)))
                            synonyms += "; " + listViewSyno.getItemAtPosition(i).toString();
                    }
                } else
                    synonyms = "";
                //====================

                final String word = wordEditText.getText().toString();
                final String pos = posEditText.getText().toString();
                //final String meaning = meaningEditText.getText().toString();

                if (word.equals("") || meaning.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);

                    builder.setMessage(R.string.dialog_message_fill_all_field)
                            .setTitle(R.string.dialog_message_error)
                            .setPositiveButton(R.string.dialog_message_ok, null);

                    AlertDialog dialog = builder.create();

                    dialog.show();

                    break;

                } else {

                    long r = dbManager.insert(word, pos, meaning, synonyms);

                    if (r != -1) {

                        isDBchanged = true;

                        if (IsSendToServer) {
                            so_tools.sendData(getString(R.string.server_txt_new), word, pos, meaning, synonyms);
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
                                        listItemsSynonyms.clear();
                                        adapterSynonyms.notifyDataSetChanged();

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

                final EditText userInput = (EditText) DialogView.findViewById(R.id.txtInput);
                //userInput.setText("");


                AlertDialog.Builder builder = new AlertDialog.Builder(add_new_entry.this);
                builder.setTitle(getString(R.string.ui_txt_edit_meaning));

                //Multiple parent fix
                if (DialogView.getParent() != null)
                    ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                builder.setView(DialogView);

                subTitleDialog.setText(getString(R.string.enter_new_meaning));
                userInput.setText("");
                userInput.setHint(R.string.new_meaning);

                builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!userInput.getText().toString().equals(""))
                            adapterMeaning.add(userInput.getText().toString().replace("\n", " ").replace("\r", " "));

                    }
                });
                builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


                break;

            case R.id.btn_add_synonyms:

                final EditText userInput2 = (EditText) DialogView.findViewById(R.id.txtInput);
                //userInput2.setText("");

                AlertDialog.Builder builderSyno = new AlertDialog.Builder(add_new_entry.this);
                builderSyno.setTitle(getString(R.string.ui_txt_edit_synonyms));

                //Multiple parent fix
                if (DialogView.getParent() != null)
                    ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                builderSyno.setView(DialogView);

                subTitleDialog.setText(getString(R.string.enter_new_synonyms));
                userInput2.setHint(R.string.new_synonyms);
                userInput2.setText("");


                builderSyno.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!userInput2.getText().toString().equals(""))
                            adapterSynonyms.add(userInput2.getText().toString().replace("\n", " ").replace("\r", " "));

                    }
                });
                builderSyno.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderSyno.show();

                break;

            case R.id.btn_del_meaning:
                if (!listItemsMeaning.isEmpty() && POSITION >= 0 && POSITION < listItemsMeaning.size()) {
                    listItemsMeaning.remove(POSITION);
                    adapterMeaning.notifyDataSetChanged();
                }
                break;

            case R.id.btn_del_synonyms:
                if (!listItemsSynonyms.isEmpty() && POSITION_Syno >= 0 && POSITION_Syno < listItemsSynonyms.size()) {
                    listItemsSynonyms.remove(POSITION_Syno);
                    adapterSynonyms.notifyDataSetChanged();
                }
                break;

            case R.id.btn_close:
                AlertDialog.Builder adb = new AlertDialog.Builder(add_new_entry.this);
                adb.setTitle(getString(R.string.sure_to_back_from_add_entry));
                adb.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.setNegativeButton(getString(R.string.no), null);
                adb.show();

                break;
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



























