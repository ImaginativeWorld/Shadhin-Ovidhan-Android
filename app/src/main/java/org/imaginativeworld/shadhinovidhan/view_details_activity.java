/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class view_details_activity extends AppCompatActivity implements OnClickListener {

    TextView clickedTxtView;
    Boolean IsSendToServer;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> synoAdapter;
    HashMap<String, String> hashMap;
    TextToSpeech textToSpeech;
    View DialogView;
    TextView subTitleDialog;
    private TextView txtWord;
    private TextView txtpos;
    private TextView txtViewMeaning;
    private EditText txtEditMeaning;
    private ListView meaningList;
    private ListView synonymsList;
    private ImageButton btnClose;
    private ImageButton btnDelete;
    private ImageButton btnEdit;
    private ImageButton btnCloseOptions;
    private ImageButton btnMeaningPartDelete;
    private ImageButton btnDeleteEntry;
    private ImageButton btnFavorite;
    private ImageButton btnSpeak;
    private ImageButton btnSendToCloud;
    private View OptionView;
    private String sWord;
    private String sPos;
    private String sMeaning;
    private String sSynonyms;
    private String tEXT, tEXTsyno, tEXTmeaning;
    private int pOSITION;
    private boolean isDBchanged = false;
    private ArrayList<String> sMeaningArrList;
    private ArrayList<String> sSynonymsArrList;
    private DBManager dbManager;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details_view_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //================================================================

        OptionView = findViewById(R.id.layout_options);
        OptionView.setVisibility(View.GONE);

        //================================================================

        btnMeaningPartDelete = (ImageButton) findViewById(R.id.btn_delete_meaning_part);
        btnMeaningPartDelete.setOnClickListener(this);

        btnEdit = (ImageButton) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        btnCloseOptions = (ImageButton) findViewById(R.id.btn_close_options);
        btnCloseOptions.setOnClickListener(this);

        btnDelete = (ImageButton) findViewById(R.id.btn_add);
        btnDelete.setOnClickListener(this);

        btnDeleteEntry = (ImageButton) findViewById(R.id.btn_delete_entry);
        btnDeleteEntry.setOnClickListener(this);

        btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        btnSpeak = (ImageButton) findViewById(R.id.btn_speak);
        btnSpeak.setOnClickListener(this);

        btnSendToCloud = (ImageButton) findViewById(R.id.btn_send_to_cloud);
        btnSendToCloud.setOnClickListener(this);

        //================================================================

        txtViewMeaning = (TextView) findViewById(R.id.txtView_meaning);

        //================================================================

        txtEditMeaning = (EditText) findViewById(R.id.txtEdit_meaning);

        //================================================================

        dbManager = new DBManager(this);
        dbManager.open();

        hashMap = new HashMap<>();

        //================================================================

        txtWord = (TextView) findViewById(R.id.txt_word);
        txtpos = (TextView) findViewById(R.id.txt_pos);

        Intent intent = getIntent();
        sWord = intent.getStringExtra("word");
        sPos = intent.getStringExtra("pos");
        sMeaning = intent.getStringExtra("meaning");
        sSynonyms = intent.getStringExtra("synonyms");

        txtWord.setText(sWord);
        txtpos.setText(sPos);

        //Set Listener for Buttons
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        //================================================================

        String[] sMeaningArray = sMeaning.split(";");
        String[] sSynonymsArray = sSynonyms.split(";");
        /*
        Delete extra space from meaning lists
         */

        int len, i;
        len = sMeaningArray.length;
        //sMeaning = sMeaningArray[0];
        for (i = 1; i < len; i++) {
            if (sMeaningArray[i].startsWith(" ")) {
                sMeaningArray[i] = sMeaningArray[i].substring(1);
            }
        }

        len = sSynonymsArray.length;
        //sSynonyms = sSynonymsArray[0];
        for (i = 1; i < len; i++) {
            if (sSynonymsArray[i].startsWith(" ")) {
                sSynonymsArray[i] = sSynonymsArray[i].substring(1);
            }
        }

        //String[] changed to ArrayList<> for Entry Modification Support
        sMeaningArrList = new ArrayList<>(Arrays.asList(sMeaningArray));
        sSynonymsArrList = new ArrayList<>(Arrays.asList(sSynonymsArray));

        // Get ListView object from xml
        meaningList = (ListView) findViewById(R.id.meaning_list);
        meaningList.setEmptyView(findViewById(R.id.txt_empty));

        synonymsList = (ListView) findViewById(R.id.synonyms_list);
        synonymsList.setEmptyView(findViewById(R.id.txt_empty_syno));

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view, sMeaningArrList);

        // Assign adapter to ListView
        meaningList.setAdapter(adapter);

        synoAdapter = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view, sSynonymsArrList);

        Log.v("soa", "=" + sSynonymsArray.length + "=");
        if (sSynonymsArray.length == 1 && sSynonymsArray[0].equals(""))
            synoAdapter.clear();

        // Assign adapter to ListView
        synonymsList.setAdapter(synoAdapter);


        //=============================================================

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);

        //================================================================
        // Get the layout inflater
        DialogView = (LayoutInflater.from(view_details_activity.this)).inflate(R.layout.input_alert_dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        subTitleDialog = (TextView) DialogView.findViewById(R.id.txtDialogueSubtitle);
        //================================================================

        meaningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.text_view);

                tEXT = text.getText().toString();

                if (!tEXT.equals(getString(R.string.no_meaning_text)) &&
                        !tEXT.equals(getString(R.string.new_item_text))) {
                    //Copy to Clip Board (Only support API >=11)
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("SO_Meaning", tEXT);
                    clipboard.setPrimaryClip(clip);

                    Toast t = Toast.makeText(view_details_activity.this, getString(R.string.text_copied_to_clipboard), Toast.LENGTH_LONG);
                    t.show();
                } else {
                    // NOTE: Same Code: longClickListener

                    pOSITION = position;

                    txtEditMeaning.setVisibility(View.GONE);
                    txtViewMeaning.setText(tEXT);
                    txtViewMeaning.setVisibility(View.VISIBLE);

                    OptionView.setVisibility(View.VISIBLE);

                    final Animation animationFade =
                            AnimationUtils.loadAnimation(view_details_activity.this, android.R.anim.fade_in);

                    OptionView.clearAnimation();
                    OptionView.startAnimation(animationFade);
                }


                //====================================================

            }
        });

        meaningList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

//                clickedTxtView = (TextView) view.findViewById(R.id.text_view);
//
//                // NOTE: Same Code: longClickListener
//                tEXT = clickedTxtView.getText().toString();
//                pOSITION = position;
//
//                txtEditMeaning.setVisibility(View.GONE);
//                txtViewMeaning.setText(tEXT);
//                txtViewMeaning.setVisibility(View.VISIBLE);
//
//                OptionView.setVisibility(View.VISIBLE);
//
//                final Animation animationFade =
//                        AnimationUtils.loadAnimation(view_details_activity.this, android.R.anim.fade_in);
//
//                OptionView.clearAnimation();
//                OptionView.startAnimation(animationFade);

                //====================================================

                /// TODO: editing this [Completed]

                TextView text = (TextView) view.findViewById(R.id.text_view);

                tEXTmeaning = text.getText().toString();

                AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                adb.setTitle(getString(R.string.what_do_you_want));
                //adb.setMessage("?");

                adb.setPositiveButton(getString(R.string.str_delete), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                        adb.setTitle(getString(R.string.question_remove_meaning_part_title));
                        adb.setMessage(getString(R.string.question_remove_meaning_part_description));
                        adb.setNegativeButton(getString(R.string.no), null);
                        adb.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /// TODO: completed
                                sMeaningArrList.remove(position); //pOSITION
                                adapter.notifyDataSetChanged();

                                updateDB();
                                closeEditView();

                            }
                        });
                        adb.show();

                    }
                });
                adb.setNeutralButton(getString(R.string.str_edit),
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final EditText userInput = (EditText) DialogView.findViewById(R.id.txtInput);
                                //userInput.setText("");

                                AlertDialog.Builder builder = new AlertDialog.Builder(view_details_activity.this);
                                builder.setTitle(getString(R.string.ui_txt_edit_meaning));

                                //Multiple parent fix
                                if (DialogView.getParent() != null)
                                    ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                                builder.setView(DialogView);

                                subTitleDialog.setText(getString(R.string.enter_change_meanings));
                                userInput.setText(tEXTmeaning);
                                userInput.setHint(R.string.edited_meanings);

                                builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        /// TODO: completed
                                        if (!userInput.getText().toString().equals("")) {

                                            sMeaningArrList.set(position, userInput.getText().toString().replace("\n", " ").replace("\r", " "));

                                            adapter.notifyDataSetChanged();

                                            updateDB();
                                        }

                                    }
                                });
                                builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();

                            }
                        });

                adb.setNegativeButton(getString(R.string.str_view_cancel), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /// TODO: completed
                        //finishWithResult(true);
                    }
                });

                adb.show();

                return true;
            }
        });

        //===================================================

        synonymsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.text_view);

                tEXTsyno = text.getText().toString();

                if (tEXTsyno.equals(getString(R.string.new_item_text))) {

                    final EditText userInput = (EditText) DialogView.findViewById(R.id.txtInput);
                    userInput.setText("");

                    AlertDialog.Builder builder = new AlertDialog.Builder(view_details_activity.this);
                    builder.setTitle(getString(R.string.ui_txt_edit_synonyms));

                    //Multiple parent fix
                    if (DialogView.getParent() != null)
                        ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                    builder.setView(DialogView);

                    subTitleDialog.setText(getString(R.string.enter_new_synonyms));
                    //userInput.setText(tEXTsyno);
                    userInput.setHint(R.string.new_synonyms);

                    builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (!userInput.getText().toString().equals("")) {
                                sSynonymsArrList.set(position, userInput.getText().toString().replace("\n", " ").replace("\r", " "));
                                synoAdapter.notifyDataSetChanged();

                                updateDB();
                            }

                        }
                    });
                    builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();


                } else {


                    AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                    adb.setTitle(getString(R.string.what_do_you_want));
                    //adb.setMessage("?");

                    adb.setNeutralButton(getString(R.string.str_delete), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //dbManager.deleteInfoFavorite(tEXT);
                            sSynonymsArrList.remove(position);
                            synoAdapter.notifyDataSetChanged();

                            updateDB();

                        }
                    });
                    adb.setNegativeButton(getString(R.string.str_edit),
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    final EditText userInput = (EditText) DialogView.findViewById(R.id.txtInput);
                                    //userInput.setText("");


                                    AlertDialog.Builder builder = new AlertDialog.Builder(view_details_activity.this);
                                    builder.setTitle(getString(R.string.ui_txt_edit_synonyms));

                                    //Multiple parent fix
                                    if (DialogView.getParent() != null)
                                        ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                                    builder.setView(DialogView);

                                    subTitleDialog.setText(getString(R.string.enter_change_synonyms));
                                    userInput.setText(tEXTsyno);
                                    userInput.setHint(R.string.edited_synonyms);

                                    builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            /// TODO: No change needed
                                            if (!userInput.getText().toString().equals("")) {
                                                sSynonymsArrList.set(position, userInput.getText().toString().replace("\n", " ").replace("\r", " "));
                                                synoAdapter.notifyDataSetChanged();

                                                updateDB();

                                                //========
                                                txtEditMeaning.setVisibility(View.GONE);

                                                //text.replace("\n", "").replace("\r", "");
                                                tEXT = txtEditMeaning.getText().toString().replace("\n", " ").replace("\r", " ");

                                                txtViewMeaning.setText(tEXT);
                                                txtViewMeaning.setVisibility(View.VISIBLE);

                                                btnEdit.setImageResource(R.drawable.ic_edit_black_48dp);

                                                sMeaningArrList.set(pOSITION, tEXT);
                                                adapter.notifyDataSetChanged();

                                                updateDB();
                                            }

                                        }
                                    });
                                    builder.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.show();

                                }
                            });
                    adb.setPositiveButton(getString(R.string.str_view_meaning), new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finishWithResult(true);
                        }
                    });
                    adb.show();
                }
            }
        });

        //===================================================

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        //===================================================

        if (dbManager.isInFavorite(sWord)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_black_48dp);
            isFavorite = true;
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_outline_black_48dp);
            isFavorite = false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                if (isDBchanged) {
                    if (IsSendToServer) {
                        so_tools.sendData(getString(R.string.server_txt_modified), sWord, sPos, sMeaning, sSynonyms);
                    }
                }

                finishWithResult(false);

                break;

            case R.id.btn_add:

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(view_details_activity.this, findViewById(R.id.btn_add));
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_add, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.item_add_meaning:

                                sMeaningArrList.add(getString(R.string.new_item_text));
                                adapter.notifyDataSetChanged();

                                break;

                            case R.id.item_add_synonym:

                                sSynonymsArrList.add(getString(R.string.new_item_text));
                                synoAdapter.notifyDataSetChanged();

                                break;
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu

                break;

            case R.id.btn_delete_meaning_part:
                AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                adb.setTitle(getString(R.string.question_remove_meaning_part_title));
                adb.setMessage(getString(R.string.question_remove_meaning_part_description));
                adb.setNegativeButton(getString(R.string.no), null);
                adb.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sMeaningArrList.remove(pOSITION);
                        adapter.notifyDataSetChanged();

                        updateDB();
                        closeEditView();

                    }
                });
                adb.show();

                break;

            case R.id.btn_edit:

                if (txtViewMeaning.getVisibility() == View.VISIBLE) {

                    txtViewMeaning.setVisibility(View.GONE);

                    txtEditMeaning.setText(tEXT);
                    txtEditMeaning.setVisibility(View.VISIBLE);

                    btnEdit.setImageResource(R.drawable.ic_save_black_48dp);

                } else {

                    txtEditMeaning.setVisibility(View.GONE);

                    //text.replace("\n", "").replace("\r", "");
                    tEXT = txtEditMeaning.getText().toString().replace("\n", " ").replace("\r", " ");

                    txtViewMeaning.setText(tEXT);
                    txtViewMeaning.setVisibility(View.VISIBLE);

                    btnEdit.setImageResource(R.drawable.ic_edit_black_48dp);

                    sMeaningArrList.set(pOSITION, tEXT);
                    adapter.notifyDataSetChanged();

                    updateDB();

                }

                break;

            case R.id.btn_delete_entry:
                AlertDialog.Builder adb2 = new AlertDialog.Builder(view_details_activity.this);
                adb2.setTitle(getString(R.string.question_delete_entry_title));
                adb2.setMessage(getString(R.string.question_delete_entry_description, sWord));
                adb2.setNegativeButton(getString(R.string.no), null);
                adb2.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (dbManager.delete(sWord) != 0)
                            isDBchanged = true;

                        finishWithResult(false);
                    }
                });
                adb2.show();

                break;

            case R.id.btn_favorite:

                if (isFavorite) {
                    if (dbManager.deleteFromFavorite(sWord) != 0) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_outline_black_48dp);
                        isFavorite = false;
                        Toast t = Toast.makeText(view_details_activity.this,
                                getString(R.string.msg_removed_from_favorite_list), Toast.LENGTH_LONG);
                        t.show();
                    }
                } else {
                    if (dbManager.insertIntoFavorite(sWord) != -1) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_black_48dp);
                        isFavorite = true;

                        Toast t = Toast.makeText(view_details_activity.this,
                                getString(R.string.msg_added_to_favorite_list), Toast.LENGTH_SHORT);
                        t.show();
                    }
                }

                break;

            case R.id.btn_close_options:
                closeEditView();

                //Reset (if close before save)
                btnEdit.setImageResource(R.drawable.ic_edit_black_48dp);

                break;

            case R.id.btn_speak:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(txtWord.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(txtWord.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;

            case R.id.btn_send_to_cloud:

                so_tools.sendData(getString(R.string.server_txt_sent_by_button), sWord, sPos, sMeaning, sSynonyms);

                break;

        }
    }

    void updateDB() {

        String meaning;
        int len, i;

        len = meaningList.getAdapter().getCount();
        if (len > 0) {
            meaning = meaningList.getItemAtPosition(0).toString();
            for (i = 1; i < len; i++) {
                if (!meaningList.getItemAtPosition(i).toString().equals("") &&
                        !meaningList.getItemAtPosition(i).toString().equals(getString(R.string.new_item_text)))
                    meaning += "; " + meaningList.getItemAtPosition(i).toString();
            }
        } else
            meaning = "";

        sMeaning = meaning;

        //============================
        len = synonymsList.getAdapter().getCount();
        if (len > 0) {
            meaning = synonymsList.getItemAtPosition(0).toString();
            for (i = 1; i < len; i++) {
                if (!synonymsList.getItemAtPosition(i).toString().equals("") &&
                        !synonymsList.getItemAtPosition(i).toString().equals(getString(R.string.new_item_text)))
                    meaning += "; " + synonymsList.getItemAtPosition(i).toString();
            }
        } else
            meaning = "";

        sSynonyms = meaning;

        //==================================

        if (dbManager.update(sWord, sPos, sMeaning, sSynonyms) != 0) {
            isDBchanged = true;
        }

    }

    void closeEditView() {
        final Animation animationFade =
                AnimationUtils.loadAnimation(view_details_activity.this, android.R.anim.fade_out);
        OptionView.clearAnimation();
        OptionView.startAnimation(animationFade);
        OptionView.setVisibility(View.GONE);
    }

    private void finishWithResult(boolean isViewSynonymMeaning) {
        Bundle conData = new Bundle();
        conData.putBoolean("results", isDBchanged);
        if (isViewSynonymMeaning)
            conData.putString("synonym", tEXTsyno);
        else
            conData.putString("synonym", null);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }


}























