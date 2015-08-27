package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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


/**
 * Created by Shohag on 06 Aug 15.
 */
public class view_details_activity extends Activity implements OnClickListener {

    TextView clickedTxtView;
    Boolean IsSendToServer;
    ArrayAdapter<String> adapter;
    HashMap<String, String> hashMap;
    private TextView txtWord;
    private TextView txtpos;
    private TextView txtViewMeaning;
    private EditText txtEditMeaning;
    private ListView meaningList;
    private ImageButton btnClose;
    private ImageButton btnDelete, btnEdit, btnCloseOptions, btnMeaningPartDelete, btnDeleteEntry, btnFavorite;
    private View OptionView;
    private String sWord;
    private String sPos;
    private String sMeaning, tempSmeaning, tEXT;
    private int pOSITION;
    private boolean isDBchanged = false;
    private String[] sMeaningArray;
    private ArrayList<String> sMeaningArrList;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.view_details_title));

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

        //================================================================

        txtViewMeaning = (TextView) findViewById(R.id.txtView_meaning);

        //================================================================

        txtEditMeaning = (EditText) findViewById(R.id.txtEdit_meaning);

        //================================================================

        dbManager = new DBManager(this);
        dbManager.open();

        hashMap = new HashMap<String, String>();

        //================================================================

        txtWord = (TextView) findViewById(R.id.txt_word);
        txtpos = (TextView) findViewById(R.id.txt_pos);

        Intent intent = getIntent();
        sWord = intent.getStringExtra("word");
        sPos = intent.getStringExtra("pos");
        tempSmeaning = sMeaning = intent.getStringExtra("meaning");

        txtWord.setText(sWord);
        txtpos.setText(sPos);

        //Set Listener for Buttons
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        //================================================================

        sMeaningArray = sMeaning.split(";");
        /*
        Delete extra space from meaning lists
         */

        int len, i;
        len=sMeaningArray.length;
        sMeaning = sMeaningArray[0];
        for(i=1;i<len;i++)
        {
            if(sMeaningArray[i].startsWith(" "))
            {
                sMeaningArray[i] = sMeaningArray[i].substring(1);
            }
        }

        //String[] changed to ArrayList<> for Entry Modification Support
        sMeaningArrList = new ArrayList<String>(Arrays.asList(sMeaningArray));

        // Get ListView object from xml
        meaningList = (ListView) findViewById(R.id.meaning_list);
        meaningList.setEmptyView(findViewById(R.id.txt_empty));
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<String>(this,
                R.layout.meaning_list_layout, R.id.text_view, sMeaningArrList);

        // Assign adapter to ListView
        meaningList.setAdapter(adapter);

        //=============================================================

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        IsSendToServer = sharedPref.getBoolean(preference_activity.pref_key_send_to_server, true);

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
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                clickedTxtView = (TextView) view.findViewById(R.id.text_view);

                // NOTE: Same Code: longClickListener
                tEXT = clickedTxtView.getText().toString();
                pOSITION = position;

                txtEditMeaning.setVisibility(View.GONE);
                txtViewMeaning.setText(tEXT);
                txtViewMeaning.setVisibility(View.VISIBLE);

                OptionView.setVisibility(View.VISIBLE);

                final Animation animationFade =
                        AnimationUtils.loadAnimation(view_details_activity.this, android.R.anim.fade_in);

                OptionView.clearAnimation();
                OptionView.startAnimation(animationFade);

                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                if (isDBchanged) {
                    if (IsSendToServer) {
                        sendData(getString(R.string.server_txt_modified), sWord, sPos, sMeaning);
                    }
                }

                finishWithResult();

                break;

            case R.id.btn_add:
                sMeaningArrList.add(getString(R.string.new_item_text));
                adapter.notifyDataSetChanged();
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

                    tEXT = txtEditMeaning.getText().toString();

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

                        finishWithResult();
                    }
                });
                adb2.show();

                break;

            case R.id.btn_favorite:

                if (dbManager.insertIntoFavorite(sWord) != -1) {
                    Toast t = Toast.makeText(view_details_activity.this,
                            getString(R.string.msg_added_to_favorite_list), Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    Toast t = Toast.makeText(view_details_activity.this,
                            getString(R.string.msg_already_in_favorite_list), Toast.LENGTH_LONG);
                    t.show();
                }

                break;

            case R.id.btn_close_options:
                closeEditView();
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
            meaning = getString(R.string.no_meaning_text);

        if (dbManager.update(sWord, sPos, meaning) != 0) {
            isDBchanged = true;
        }

        sMeaning = meaning;

    }

    void closeEditView() {
        final Animation animationFade =
                AnimationUtils.loadAnimation(view_details_activity.this, android.R.anim.fade_out);
        OptionView.clearAnimation();
        OptionView.startAnimation(animationFade);
        OptionView.setVisibility(View.GONE);
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























