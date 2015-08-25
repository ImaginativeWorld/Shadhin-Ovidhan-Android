package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Shohag on 18 Jul 15.
 */
public class main_activity extends ActionBarActivity implements View.OnClickListener {

    //for DB ovidhan
    final String[] from = new String[]{
            DatabaseHelper.SO_PRON,
            DatabaseHelper.SO_POS,
            DatabaseHelper.SO_MEANING
    };
    final int[] to = new int[]{
            R.id.txt_pron,
            R.id.txt_pos,
            R.id.txt_meaning
    };

    Toolbar toolbar;

    EditText editTextSearch;

    View searchBar;//, toolBar;

    ImageButton btnClearSearch;

    View welcomeLayout;

    SharedPreferences sharedPref;
    String BnSearchType, EnSearchType;

    //private AutoCompleteTextView SrcTxtView;
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mainlayout); // select default layout

        //============================================================

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setVisibility(View.VISIBLE);

        //============================================================

        //Load default preference for first time only. Must make the last argument is false.
        //Or every time the application reset it to default value.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //============================================================

        welcomeLayout = findViewById(R.id.layout_welcome);
        welcomeLayout.setVisibility(View.VISIBLE);

        //============================================================

        searchBar = findViewById(R.id.search_bar);
        searchBar.setY(-getPixels(56));

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setHint(R.string.search_hint);

        //============================================================

        btnClearSearch = (ImageButton) findViewById(R.id.searchClear);
        btnClearSearch.setOnClickListener(this);

        //============================================================

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        setSettings();

        //============================================================

        dbManager = new DBManager(this);
        dbManager.open();

        //============================================================

        Cursor cursor = null;

        listView = (ListView) findViewById(R.id.list_view);

        listView.setEmptyView(findViewById(R.id.empty)); // for empty view

        adapter = new SimpleCursorAdapter(this, R.layout.result_list_layout, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idTextView = (TextView) view.findViewById(R.id.txt_pron);
                TextView titleTextView = (TextView) view.findViewById(R.id.txt_pos);
                TextView descTextView = (TextView) view.findViewById(R.id.txt_meaning);

                String s_word = idTextView.getText().toString();
                String s_pos = titleTextView.getText().toString();
                String s_meaning = descTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), view_details_activity.class);
                modify_intent.putExtra("word", s_word);
                modify_intent.putExtra("pos", s_pos);
                modify_intent.putExtra("meaning", s_meaning);

                startActivityForResult(modify_intent, 100);

            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals("")) {

                    welcomeLayout.setVisibility(View.GONE);

                    Cursor cursor;
                    String str = s.toString();

                    //Just change the adapter cursor to change the data view.. :)
                    if (s.toString().charAt(0) < 128) {
                        do {
                            cursor = dbManager.searchEN(str, EnSearchType);

                            if (str.length() >= 3)
                                str = str.substring(0, str.length() - 1);

                            if (str.equals(""))
                                break;

                        } while (cursor.getCount() == 0);
                    } else {
                        do {
                            cursor = dbManager.searchBN(str, BnSearchType);

                            if (str.length() >= 3)
                                str = str.substring(0, str.length() - 1);

                            if (str.equals(""))
                                break;

                        } while (cursor.getCount() == 0);
                    }

                    if (cursor.getCount() != 0)
                        adapter.changeCursor(cursor);

                } else {
                    adapter.changeCursor(null);
                    welcomeLayout.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
            case 200:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    Boolean result = res.getBoolean("results");
                    if (result) {
                        editTextSearch.setText("");
                    }
                }
                break;

            case 300:

                setSettings();

                break;

            case 400:

                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String result = res.getString("results");
//                    if (!result.equals("")) {
                    editTextSearch.setText(result);
//                    }
                }

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Must Add the intent/activity class name into Manifest.xml
         */

        switch (item.getItemId()) {
            case R.id.add_record:

                Intent add_entry_intent = new Intent(this, add_new_entry.class);
                startActivityForResult(add_entry_intent, 200);

                break;

            case R.id.searchNow:

                showSearchBar();

                break;

            case R.id.prefs:

                Intent prefs_intent = new Intent(this, preference_activity.class);
                startActivityForResult(prefs_intent, 300);

                break;

            case R.id.about:

                Intent intent = new Intent(this, about_activity.class);
                startActivity(intent);

                break;

            case R.id.favorite:

                Intent fav_intent = new Intent(this, favorite_list_activity.class);
                startActivityForResult(fav_intent, 400);

                break;

            case R.id.exit:

                this.finishAffinity();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void showSearchBar() {

        editTextSearch.setEnabled(true);
        editTextSearch.requestFocus();

        toolbar.animate()
                .translationY(-toolbar.getBottom())
                .setInterpolator(new AccelerateInterpolator())
                .start();

        searchBar.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();

    }

    void hideSearchBar() {

        toolbar.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        searchBar.animate()
                .translationY(-searchBar.getBottom())
                .setInterpolator(new AccelerateInterpolator())
                .start();


        editTextSearch.setEnabled(false);

        //editTextSearch.setFocusable(false);
        //editTextSearch.setClickable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchClear:
                if (editTextSearch.getText().toString().equals(""))
                    hideSearchBar();
                else
                    editTextSearch.setText("");
                break;


        }
    }

    private void setSettings() {
        BnSearchType = sharedPref.getString(preference_activity.bnAdvSearchType, "2");

        EnSearchType = sharedPref.getString(preference_activity.enAdvSearchType, "1");
    }

    private int getPixels(int dipValue) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}


//    public void showPopup(View v) {
//
//        PopupMenu popup = new PopupMenu(this, v);
//
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.main, popup.getMenu());
//        popup.show();
//    }

//Just making a toast u need to write below those 5 line of code.. :'(
//        Context context = getApplicationContext();
//        Toast toast = Toast.makeText(context, "Hello toast!",  Toast.LENGTH_LONG);
//        toast.show();




























