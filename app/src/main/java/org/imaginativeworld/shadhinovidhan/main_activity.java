package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Shohag on 18 Jul 15.
 */
public class main_activity extends ActionBarActivity {

    private DBManager dbManager;

    private ListView listView;

    //private AutoCompleteTextView SrcTxtView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper._WORD,
            DatabaseHelper.SO_POS,
            DatabaseHelper.SO_MEANING
    };

    final int[] to = new int[] {
            R.id.id,
            R.id.title,
            R.id.desc
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

        dbManager = new DBManager(this);
        dbManager.open();
        //Cursor cursor = dbManager.fetch();
        Cursor cursor = null;

        listView = (ListView) findViewById(R.id.list_view);

        listView.setEmptyView(findViewById(R.id.empty)); // for empty view

        adapter = new SimpleCursorAdapter(this, R.layout.result_list_layout, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

//        SrcTxtView = (AutoCompleteTextView) findViewById(R.id.SrcEditText);
//
//        SrcTxtView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().equals("")) {
//
//                    //Just change the adapter cursor to change the data view.. :)
//                    adapter.changeCursor(dbManager.searchEN(s.toString()));
//
//                } else
//                    adapter.changeCursor(null);
//
//            }
//        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView descTextView = (TextView) view.findViewById(R.id.desc);

                String sid = idTextView.getText().toString();
                String stitle = titleTextView.getText().toString();
                String sdesc = descTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), view_details_activity.class);
                modify_intent.putExtra("title", stitle);
                modify_intent.putExtra("desc", sdesc);
                modify_intent.putExtra("id", sid);

                startActivity(modify_intent);

//                Toast.makeText(getBaseContext(), tvCountry.getText().toString(), Toast.LENGTH_SHORT).show();



            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

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

                    //Just change the adapter cursor to change the data view.. :)
                    adapter.changeCursor(dbManager.searchEN(s));

                } else
                    adapter.changeCursor(null);
                return false;
            }
        });



        return true;
    }

    public void showPopup(View v) {

        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.show();
    }

    public void addEntry(MenuItem item) {

        /*
         * Must Add the intent/activity class name into Manifest.xml
         */

        Intent intent = new Intent(this, add_new_entry.class);
        startActivity(intent);
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




























