package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Shohag on 26 Jul 15.
 */
public class add_new_entry extends Activity implements OnClickListener {

    private Button addTodoBtn;

    private EditText wordEditText;
    private EditText posEditText;
    private EditText meaningEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Entry");

        setContentView(R.layout.add_entry_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        wordEditText = (EditText) findViewById(R.id.txt_edit_word);
        posEditText = (EditText) findViewById(R.id.txt_edit_pos);
        meaningEditText = (EditText) findViewById(R.id.txt_edit_meaning);

        addTodoBtn = (Button) findViewById(R.id.btn_add);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(add_new_entry.this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add:

                final String word = wordEditText.getText().toString();
                final String pos = posEditText.getText().toString();
                final String meaning = meaningEditText.getText().toString();

                dbManager.insert(word, pos, meaning);

                Intent main = new Intent(add_new_entry.this, main_activity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }

    }

    public void btnClose(View view) {
        finish();
    }
}



























