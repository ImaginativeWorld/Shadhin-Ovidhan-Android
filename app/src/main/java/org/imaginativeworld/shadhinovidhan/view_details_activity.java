package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
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


/**
 * Created by Shohag on 06 Aug 15.
 */
public class view_details_activity extends Activity implements OnClickListener {

    private TextView txtWord;
    private TextView txtpos;
    private TextView txtmeaning;

    private ListView meaningList;

    private ImageButton btnClose;
    private ImageButton btnEdit;

    private String sWord;
    private String sPos;
    private String sMeaning, tempSmeaning;

    private String[] sMeaningArray;

    ArrayAdapter<String> adapter;

    AlertDialog.Builder builder;
    DialogInterface.OnClickListener dialogClickListener;

    //private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Details View Record");

        setContentView(R.layout.details_view_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //================================================================

        //dbManager = new DBManager(this);
        //dbManager.open();

        txtWord = (TextView) findViewById(R.id.txt_word);
        txtpos = (TextView) findViewById(R.id.txt_pos);
        //txtmeaning = (TextView) findViewById(R.id.txt_meaning);

        Intent intent = getIntent();
        sWord = intent.getStringExtra("id");
        sPos = intent.getStringExtra("title");
        tempSmeaning = sMeaning = intent.getStringExtra("desc");

        txtWord.setText(sWord);
        txtpos.setText(sPos);
        //txtmeaning.setText(sMeaning);

        //Set Listener for Buttons
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);
//        btnEdit = (ImageButton) findViewById(R.id.btn_edit);
//        btnEdit.setOnClickListener(this);

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

        // Get ListView object from xml
        meaningList = (ListView) findViewById(R.id.meaning_list);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<String>(this,
                R.layout.meaning_list_layout, R.id.text_view, sMeaningArray);

        // Assign adapter to ListView
        meaningList.setAdapter(adapter);

        //=============================================================
        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        builder = new AlertDialog.Builder(view_details_activity.this);


        //================================================================

        meaningList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.text_view);

                String tEXT = text.getText().toString();

                //Copy to Clip Board (Only support API >=11)
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("SO_Meaning", tEXT);
                clipboard.setPrimaryClip(clip);

                Toast t = Toast.makeText(view_details_activity.this, "Text Copied to Clipboard.", Toast.LENGTH_LONG);
                t.show();


                //====================================================
                builder
                        .setMessage("Position: " + position)
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                ;

                builder.show();


            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();

                break;

//            case R.id.btn_edit:
//
//                Intent modify_intent = new Intent(getApplicationContext(), modify_entry_activity.class);
//                modify_intent.putExtra("word", sWord);
//                modify_intent.putExtra("pos", sPos);
//                modify_intent.putExtra("meaning", tempSmeaning);
//
//                startActivity(modify_intent);
//
//                this.finish();
//                break;

        }
    }


}























