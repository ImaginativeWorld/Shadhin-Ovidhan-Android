package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Shohag on 07 Aug 15.
 */
public class modify_entry_activity extends Activity implements OnClickListener {

    private TextView txtWord;
    private TextView txtpos;

    private ListView meaningList;

    private ImageButton btnClose, btnAdd, btnSave;

    String sWord;
    String sPos;
    private String sMeaning;
    private ArrayList<String> aryListStr = new ArrayList<String>();
    private String[] sMeaningArray;

    ArrayAdapter<String> adapter;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.modify_entry_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dbManager = new DBManager(this);
        dbManager.open();

        //================================================================


        txtWord = (TextView) findViewById(R.id.txt_word);
        txtpos = (TextView) findViewById(R.id.txt_pos);
//        //txtmeaning = (TextView) findViewById(R.id.txt_meaning);

        Intent intent = getIntent();
        sWord = intent.getStringExtra("word");
        sPos = intent.getStringExtra("pos");
        sMeaning = intent.getStringExtra("meaning");

        txtWord.setText(sWord);
        txtpos.setText(sPos);
//        //txtmeaning.setText(sMeaning);

        //Add listener to Buttons
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

        btnSave = (ImageButton) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);


        //================================================================

        sMeaningArray = sMeaning.split(";");

        /*
         * Delete extra space from meaning lists
         */
        int len, i;
        len=sMeaningArray.length;
        for(i=0;i<len;i++)
        {
            if(sMeaningArray[i].startsWith(" "))
            {
                aryListStr.add(sMeaningArray[i].substring(1));
            }
            else
                aryListStr.add(sMeaningArray[i].substring(0));

        }

        // Get ListView object from xml
        meaningList = (ListView) findViewById(R.id.meaning_list);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<String>(this,
                R.layout.modify_meaning_list_layout, android.R.id.text1, aryListStr);
//
//        // Assign adapter to ListView
        meaningList.setAdapter(adapter);

        //================================================================

        meaningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (meaningList.getItemAtPosition(position)).toString();

                //Copy to Clip Board (Only support API >=11)
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("SO_Meaning", selectedFromList);
                clipboard.setPrimaryClip(clip);

                Toast t = Toast.makeText(modify_entry_activity.this, "Text Copied to Clipboard.", Toast.LENGTH_LONG);
                t.show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();

                break;
            case R.id.btn_add:

                adapter.add(getString(R.string.new_item_text));
                break;

            case R.id.btn_save:
                String meaning;
                int len, i;

                len = meaningList.getAdapter().getCount();
                meaning = meaningList.getItemAtPosition(0).toString();
                for(i=1;i<len;i++)
                {
                    if(!meaningList.getItemAtPosition(i).toString().equals("") ||
                            !meaningList.getItemAtPosition(i).toString().equals(getString(R.string.new_item_text)))
                        meaning += "; " + meaningList.getItemAtPosition(i).toString();
                }

                Toast t = Toast.makeText(this, meaning + "  " + len, Toast.LENGTH_LONG);
                t.show();

                dbManager.update(sWord, sPos, meaning);

                this.finish();

                break;
        }

    }

//    public void returnHome() {
//        Intent home_intent = new Intent(getApplicationContext(), main_activity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(home_intent);
//    }

}
