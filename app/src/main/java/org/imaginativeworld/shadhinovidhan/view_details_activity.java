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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class view_details_activity
        extends AppCompatActivity
        implements OnClickListener, CustomListView.OnItemClickListener, CustomListView.OnItemLongClickListener,
        View.OnLongClickListener {

    /**
     * @final MAX_POS
     * Maximum number of Layout for meaning.
     * After changing it please add new views in the following functions:
     * - OnItemClick()
     * - onItemLongClick()
     */
    final int MAX_POS = 12;

    /**
     * @variable totalPost
     * It maintain the total number of POS created.
     */
    int _totalPos;

    Boolean IsSendToServer;
    ArrayAdapter<String>[] adapter = new ArrayAdapter[MAX_POS];
    ArrayAdapter<String> synoAdapter;
    HashMap<String, Integer> mapPos = new HashMap<>();

    TextToSpeech textToSpeech;
    View DialogView;

    LinearLayout[] meaningListLayout = new LinearLayout[MAX_POS];
    TextView[] txtViewPos = new TextView[MAX_POS];
    CustomListView[] meaning_list = new CustomListView[MAX_POS];


    private ListView synonymsList;
    private ImageButton btnFavorite;
    private String sWord;

    private String sMeaning;
    private String sSynonyms;
    private String tEXTsyno, tEXT;
    private boolean isDBchanged = false;
    private ArrayList<String>[] sMeaningArrList = new ArrayList[MAX_POS];
    private ArrayList<String> sSynonymsArrList;
    private DBManager dbManager;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Set Dialog Theme
         */
        String UI_theme = sharedPref.getString(preference_activity.pref_ui_theme, "light_green");
        so_tools.setDialogUItheme(UI_theme, view_details_activity.this);

        setContentView(R.layout.details_view_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //================================================================

        /**
         * Set Listener for Buttons
         */
        btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);
        btnFavorite.setOnLongClickListener(this);

        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btn_speak);
        btnSpeak.setOnClickListener(this);
        btnSpeak.setOnLongClickListener(this);

        ImageButton btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
        btnClose.setOnLongClickListener(this);

        ImageButton btnOptions = (ImageButton) findViewById(R.id.btn_options);
        btnOptions.setOnClickListener(this);

        Class cls = R.id.class;

        for (int i = 0; i < MAX_POS; i++) {
            meaningListLayout[i] =
                    (LinearLayout) findViewById(so_tools.getResId("meaning_layout_" + String.valueOf(i), cls));
        }

        for (int i = 0; i < MAX_POS; i++) {
            txtViewPos[i] =
                    (TextView) findViewById(so_tools.getResId("meaning_pos_" + String.valueOf(i), cls));
        }

        for (int i = 0; i < MAX_POS; i++) {
            meaning_list[i] =
                    (CustomListView) findViewById(so_tools.getResId("meaning_list_" + String.valueOf(i), cls));
            meaning_list[i].setEmptyView(findViewById(R.id.txt_empty));
            meaning_list[i].setOnItemClickListener(view_details_activity.this);
            meaning_list[i].setOnItemLongClickListener(view_details_activity.this);
        }

        //================================================================

        dbManager = new DBManager(this);
        dbManager.open();

        //================================================================

        Intent intent = getIntent();
        sWord = intent.getStringExtra("word");
        sMeaning = intent.getStringExtra("meaning");
        sSynonyms = intent.getStringExtra("synonyms");

        setTitle(sWord);

        /**
         * ***********************************
         *             MEANING PART
         * ***********************************
         */
        /**
         * Steps:
         * - break "meaning by POS" using '|'
         * - get the POS
         * - break meanings using ';'
         */

        String[] meaningByPos = sMeaning.split("\\|");
        _totalPos = meaningByPos.length;
        int loc;
        String _POS, strTemp;

        /**
         * Loop for every PoS
         */
        for (int i = 0; i < _totalPos; i++) {
            /**
             * Split the PoS part from "Main Meaning String"
             */
            loc = meaningByPos[i].indexOf("]");
            if (loc > 0) {
                _POS = meaningByPos[i].substring(1, loc);
                txtViewPos[i].setText(_POS);
                mapPos.put(_POS, i);
            } else {
                txtViewPos[i].setText("");
                txtViewPos[i].setVisibility(View.GONE);
            }

            /**
             * Split the meaning part from "Main Meaning String"
             */
            meaningByPos[i] = meaningByPos[i].substring(loc + 1);

            meaningListLayout[i].setVisibility(View.VISIBLE);

            /**
             * Split the meanings using semi-colon
             */
            String[] sMeaningArray = meaningByPos[i].split(";");

            /**
             * Delete extra space from meaning lists
             */
            int len, j;
            len = sMeaningArray.length;
            for (j = 1; j < len; j++) {
                if (sMeaningArray[j].startsWith(" ")) {
                    sMeaningArray[j] = sMeaningArray[j].substring(1);
                }
            }

            /**
             * Using ArrayList<> for add, edit, and remove feature
             */
            sMeaningArrList[i] = new ArrayList<>(Arrays.asList(sMeaningArray));

            adapter[i] = new ArrayAdapter<>(this,
                    R.layout.meaning_list_layout, R.id.text_view, sMeaningArrList[i]);

            meaning_list[i].setAdapter(adapter[i]);

        }


        /**
         * ***********************************
         *             SYNONYM PART
         * ***********************************
         */

        String[] sSynonymsArray = sSynonyms.split(";");

        /**
         * Delete extra space from meaning lists
         */
        int len, i;

        len = sSynonymsArray.length;
        for (i = 1; i < len; i++) {
            if (sSynonymsArray[i].startsWith(" ")) {
                sSynonymsArray[i] = sSynonymsArray[i].substring(1);
            }
        }

        /**
         * String[] changed to ArrayList<> for Entry Modification Support
         *
         */

        sSynonymsArrList = new ArrayList<>(Arrays.asList(sSynonymsArray));

        /**
         * Get ListView object from xml
         */
//
        synonymsList = (ListView) findViewById(R.id.synonyms_list);
        synonymsList.setEmptyView(findViewById(R.id.txt_empty_syno));

        /**
         * Define a new Adapter
         * First parameter - Context
         * Second parameter - Layout for the row
         * Third parameter - ID of the TextView to which the data is written
         * Forth - the Array of data
         */

        synoAdapter = new ArrayAdapter<>(this,
                R.layout.meaning_list_layout, R.id.text_view, sSynonymsArrList);

        if (sSynonymsArray.length == 1 && sSynonymsArray[0].equals(""))
            synoAdapter.clear();

        // Assign adapter to ListView
        synonymsList.setAdapter(synoAdapter);

        //=============================================================

        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);

        //================================================================
        // Get the layout inflater
        DialogView = (LayoutInflater.from(view_details_activity.this)).inflate(R.layout.input_alert_dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //subTitleDialog = (TextView) DialogView.findViewById(R.id.txtDialogueSubtitle);
        //================================================================

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

                                    userInput.setText(tEXTsyno);
                                    userInput.setHint(R.string.edited_synonyms);

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
            btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            isFavorite = true;
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            isFavorite = false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                if (isDBchanged) {
                    if (IsSendToServer) {
                        so_tools.sendData(getString(R.string.server_txt_modified), sWord, sMeaning, sSynonyms);
                    }
                }

                finishWithResult(false);

                break;

            case R.id.btn_options:
                //Creating the instance of PopupMenu
                PopupMenu popupOpts = new PopupMenu(view_details_activity.this, findViewById(R.id.btn_options));
                //Inflating the Popup using xml file
                popupOpts.getMenuInflater()
                        .inflate(R.menu.popup_menu_details_view, popupOpts.getMenu());

                //registering popup with OnMenuItemClickListener
                popupOpts.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.item_add_meaning:

                                final String[] items = {
                                        "noun",
                                        "pronoun",
                                        "verb",
                                        "adverb",
                                        "adjective",
                                        "preposition",
                                        "conjunction",
                                        "interjection",
                                        "phrase",
                                        "idiom",
                                        "article"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(view_details_activity.this);
                                builder.setTitle(getString(R.string.select_a_pos));
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        // Do something with the selection
                                        //mDoneButton.setText(items[item]);
                                        if (mapPos.get(items[item]) == null) {

                                            if (_totalPos == MAX_POS) {

                                                AlertDialog.Builder msgDlg = new AlertDialog.Builder(view_details_activity.this);
                                                msgDlg.setTitle(getString(R.string.dialog_message_error));
                                                msgDlg.setMessage(getString(R.string.dialog_message_max_pos_reached));
                                                msgDlg.setPositiveButton(getString(R.string.dialog_message_ok), null);
                                                AlertDialog alertDialog = msgDlg.create();
                                                alertDialog.show();

                                            } else {

                                                String[] sMeaningArray = new String[]{getString(R.string.new_item_text)};

                                                sMeaningArrList[_totalPos] = new ArrayList<>(Arrays.asList(sMeaningArray));

                                                adapter[_totalPos] = new ArrayAdapter<>(view_details_activity.this,
                                                        R.layout.meaning_list_layout, R.id.text_view, sMeaningArrList[_totalPos]);

                                                // Assign adapter to ListView
                                                meaning_list[_totalPos].setAdapter(adapter[_totalPos]);

                                                txtViewPos[_totalPos].setText(items[item]);
                                                mapPos.put(items[item], _totalPos);

                                                meaningListLayout[_totalPos].setVisibility(View.VISIBLE);

                                                _totalPos++;

                                            }

                                        } else {
                                            sMeaningArrList[mapPos.get(items[item])].add(getString(R.string.new_item_text));
                                            adapter[mapPos.get(items[item])].notifyDataSetChanged();
                                        }
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                                break;

                            case R.id.item_add_synonym:

                                sSynonymsArrList.add(getString(R.string.new_item_text));
                                synoAdapter.notifyDataSetChanged();

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

                            case R.id.btn_send_to_cloud:

                                AlertDialog.Builder adb3 = new AlertDialog.Builder(view_details_activity.this);
                                adb3.setTitle(getString(R.string.question_report_title));
                                adb3.setMessage(getString(R.string.question_report_description, sWord));
                                adb3.setNegativeButton(getString(R.string.no), null);
                                adb3.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        so_tools.sendData(getString(R.string.server_txt_sent_by_button_report), sWord, sMeaning, sSynonyms);

                                    }
                                });
                                adb3.show();

                                break;

                        }

                        return true;
                    }
                });

                // Enable icon in popup menu
                try {
                    Field mFieldPopup = popupOpts.getClass().getDeclaredField("mPopup");
                    mFieldPopup.setAccessible(true);
                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupOpts);
                    mPopup.setForceShowIcon(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupOpts.show(); //showing popup menu

                break;


            case R.id.btn_favorite:

                if (isFavorite) {
                    if (dbManager.deleteFromFavorite(sWord) != 0) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        isFavorite = false;
                        Toast t = Toast.makeText(view_details_activity.this,
                                getString(R.string.msg_removed_from_favorite_list), Toast.LENGTH_LONG);
                        t.show();
                    }
                } else {
                    if (dbManager.insertIntoFavorite(sWord) != -1) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        isFavorite = true;

                        Toast t = Toast.makeText(view_details_activity.this,
                                getString(R.string.msg_added_to_favorite_list), Toast.LENGTH_SHORT);
                        t.show();
                    }
                }

                break;

            case R.id.btn_speak:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(getTitle().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    textToSpeech.speak(getTitle().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;


        }
    }

    void updateDB() {

        /**
         * ***********************************
         *      Generating the meanings
         * ***********************************
         */
        /**
         * Meaning pattern:
         * ----------------
         * meaning1; meaning2; meaning3|[PoS1]meaning1; meaning2; meaning3|[PoS2]meaning4; meaning5; meaning6
         */
        String meaning = "", strTemp;
        int len, i, j;
        boolean addSemiColon = false, addVline = false;

        for (i = 0; i < _totalPos; i++) {
            /**
             * Get total meaning count
             */
            len = meaning_list[i].getAdapter().getCount();

            /**
             * If there is no meaning found then nothing will happen
             */
            if (len > 0) {

                /**
                 * Checking PoS
                 */
                if (txtViewPos[i].getText() != "") {

                    if (addVline)
                        meaning += "|";


                    meaning += "[" + txtViewPos[i].getText() + "]";
                    addSemiColon = false;
                    addVline = true;
                }

                /**
                 * Collecting meaning
                 */
                for (j = 0; j < len; j++) {

                    strTemp = meaning_list[i].getItemAtPosition(j).toString();

                    /**
                     * Checking the string, is it blank or "default new text"?
                     */
                    if (!strTemp.equals("") && !strTemp.equals(getString(R.string.new_item_text))) {
                        if (addSemiColon)
                            meaning += "; ";
                    }
                    meaning += strTemp;
                    addSemiColon = true;
                    addVline = true;
                }
            }
        }

        if (TextUtils.isEmpty(meaning))
            sMeaning = getString(R.string.new_item_text);
        else
            sMeaning = meaning;

        /**
         * ***********************************
         *      Generating the synonyms
         * ***********************************
         */
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

        /**
         * Update database
         */
        if (dbManager.update(sWord, sMeaning, sSynonyms) != 0) {
            isDBchanged = true;
        }

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


    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {

        switch (parent.getId()) {
            case R.id.meaning_list_0:
            case R.id.meaning_list_1:
            case R.id.meaning_list_2:
            case R.id.meaning_list_3:
            case R.id.meaning_list_4:
            case R.id.meaning_list_5:
            case R.id.meaning_list_6:
            case R.id.meaning_list_7:
            case R.id.meaning_list_8:
            case R.id.meaning_list_9:
            case R.id.meaning_list_10:
            case R.id.meaning_list_11:
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
                    // NOTE: Same Code: setOnItemLongClickListener: edit section

                    final EditText userInput = (EditText) DialogView.findViewById(R.id.txtInput);

                    AlertDialog.Builder builder = new AlertDialog.Builder(view_details_activity.this);
                    builder.setTitle(getString(R.string.ui_txt_edit_meaning));

                    //Multiple parent fix
                    if (DialogView.getParent() != null)
                        ((ViewGroup) DialogView.getParent()).removeView(DialogView);

                    builder.setView(DialogView);

                    //subTitleDialog.setText(getString(R.string.enter_change_meanings));
                    userInput.setText(tEXT);
                    userInput.setHint(R.string.new_meaning);

                    builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /**
                             * Get the id
                             */
                            String _resName = getResources().getResourceName(parent.getId());
                            _resName = _resName.substring(_resName.length() - 1, _resName.length());
                            int _resId = Integer.parseInt(_resName);

                            if (!userInput.getText().toString().equals("") &&
                                    !userInput.getText().toString().equals(getString(R.string.new_item_text))) {

                                sMeaningArrList[_resId].set(position, userInput.getText().toString().replace("\n", " ").replace("\r", " "));

                                adapter[_resId].notifyDataSetChanged();

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
                break;
        }

    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {

        switch (parent.getId()) {
            case R.id.meaning_list_0:
            case R.id.meaning_list_1:
            case R.id.meaning_list_2:
            case R.id.meaning_list_3:
            case R.id.meaning_list_4:
            case R.id.meaning_list_5:
            case R.id.meaning_list_6:
            case R.id.meaning_list_7:
            case R.id.meaning_list_8:
            case R.id.meaning_list_9:
            case R.id.meaning_list_10:
            case R.id.meaning_list_11:

                TextView text = (TextView) view.findViewById(R.id.text_view);

                tEXT = text.getText().toString();

                AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                adb.setTitle(getString(R.string.what_do_you_want));

                adb.setPositiveButton(getString(R.string.str_delete), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AlertDialog.Builder adb = new AlertDialog.Builder(view_details_activity.this);
                        adb.setTitle(getString(R.string.question_remove_meaning_part_title));
                        adb.setMessage(getString(R.string.question_remove_meaning_part_description));
                        adb.setNegativeButton(getString(R.string.no), null);
                        adb.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                /**
                                 * Get the id
                                 */
                                String _resName = getResources().getResourceName(parent.getId());
                                _resName = _resName.substring(_resName.length() - 1, _resName.length());
                                int _resId = Integer.parseInt(_resName);

                                sMeaningArrList[_resId].remove(position);
                                adapter[_resId].notifyDataSetChanged();

                                updateDB();

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

                                //subTitleDialog.setText(getString(R.string.enter_change_meanings));
                                userInput.setText(tEXT);
                                userInput.setHint(R.string.edited_meaning);

                                builder.setPositiveButton(getString(R.string.str_add), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (!userInput.getText().toString().equals("") &&
                                                !userInput.getText().toString().equals(getString(R.string.new_item_text))) {

                                            /**
                                             * Get the id
                                             */
                                            String _resName = getResources().getResourceName(parent.getId());
                                            _resName = _resName.substring(_resName.length() - 1, _resName.length());
                                            int _resId = Integer.parseInt(_resName);

                                            sMeaningArrList[_resId].set(position, userInput.getText().toString().replace("\n", " ").replace("\r", " "));

                                            adapter[_resId].notifyDataSetChanged();

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

                    }
                });

                adb.show();

                break;

        }

        /**
         * return true to indicate that you have handled the event and it should stop here;
         * return false if you have not handled it and/or the event should continue to any
         * other on-click listeners.
         */
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isDBchanged) {
            if (IsSendToServer) {
                so_tools.sendData(getString(R.string.server_txt_modified), sWord, sMeaning, sSynonyms);
            }
        }

        finishWithResult(false);
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.btn_favorite:

                Toast.makeText(view_details_activity.this,
                        getString(R.string.desc_favorite), Toast.LENGTH_LONG).show();

                break;

            case R.id.btn_speak:

                Toast.makeText(view_details_activity.this,
                        getString(R.string.desc_speak), Toast.LENGTH_LONG).show();

                break;

            case R.id.btn_close:

                Toast.makeText(view_details_activity.this,
                        getString(R.string.desc_back), Toast.LENGTH_LONG).show();

                break;
        }

        return true;
    }
}























