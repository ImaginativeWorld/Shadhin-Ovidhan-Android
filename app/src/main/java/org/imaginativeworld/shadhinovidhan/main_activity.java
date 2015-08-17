package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    View searchBar;

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

        //Load default preference for first time only. Must make the last argument is false.
        //Or every time the application reset it to default value.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //============================================================

        welcomeLayout = (View) findViewById(R.id.layout_welcome);
        welcomeLayout.setVisibility(View.VISIBLE);

        searchBar = findViewById(R.id.search_bar);

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

            case R.id.prefs:

                Intent prefs_intent = new Intent(this, preference_activity.class);
                startActivity(prefs_intent);

                break;

            case R.id.about:

                Intent intent = new Intent(this, about_activity.class);
                startActivity(intent);

                break;

            case R.id.exit:

                this.finishAffinity();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void showSearchBar() {

        searchBar.setVisibility(View.VISIBLE);
        final Animation animationFade =
                AnimationUtils.loadAnimation(main_activity.this, android.R.anim.slide_in_left);
        searchBar.clearAnimation();
        searchBar.startAnimation(animationFade);
    }

    void hideSearchBar() {
        final Animation animationFade =
                AnimationUtils.loadAnimation(main_activity.this, android.R.anim.slide_out_right);
        searchBar.clearAnimation();
        searchBar.startAnimation(animationFade);
        searchBar.setVisibility(View.GONE);
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




























