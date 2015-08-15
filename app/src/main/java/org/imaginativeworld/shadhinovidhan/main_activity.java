package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Shohag on 18 Jul 15.
 */
public class main_activity extends ActionBarActivity {

    private DBManager dbManager;

    private ListView listView;

    SearchView searchView;

    View welcomeLayout;

    //private AutoCompleteTextView SrcTxtView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.SO_PRON,
            DatabaseHelper.SO_POS,
            DatabaseHelper.SO_MEANING
    };

    final int[] to = new int[] {
            R.id.txt_pron,
            R.id.txt_pos,
            R.id.txt_meaning
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mainlayout); // select default layout

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //============================================================

        welcomeLayout = (View) findViewById(R.id.layout_welcome);
        welcomeLayout.setVisibility(View.VISIBLE);

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
                        searchView.setQuery("", true);
                    }
                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint(getString(R.string.search_hint));
        //searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {

                    welcomeLayout.setVisibility(View.GONE);

                    //Just change the adapter cursor to change the data view.. :)
                    if (s.charAt(0) < 128)
                        adapter.changeCursor(dbManager.searchEN(s));
                    else
                        adapter.changeCursor(dbManager.searchBN(s));

                } else {
                    adapter.changeCursor(null);
                    welcomeLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });



        return true;
    }

//    public void showPopup(View v) {
//
//        PopupMenu popup = new PopupMenu(this, v);
//
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.main, popup.getMenu());
//        popup.show();
//    }

    public void addEntry(MenuItem item) {

        /*
         * Must Add the intent/activity class name into Manifest.xml
         */

        Intent add_entry_intent = new Intent(this, add_new_entry.class);
        //startActivity(intent);
        startActivityForResult(add_entry_intent, 200);
    }

    public void showAbout(MenuItem item) {

        /*
         * Must Add the intent/activity class name into Manifest.xml
         */

        Intent intent = new Intent(this, about_activity.class);
        startActivity(intent);

    }

    public void exitApp(MenuItem item) {
        this.finishAffinity();
    }
}


//Just making a toast u need to write below those 5 line of code.. :'(
//        Context context = getApplicationContext();
//        Toast toast = Toast.makeText(context, "Hello toast!",  Toast.LENGTH_LONG);
//        toast.show();




























