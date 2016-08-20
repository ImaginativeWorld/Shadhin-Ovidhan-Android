/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class main_activity extends Activity
        implements View.OnClickListener, View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {

    //for DB ovidhan
    final String[] from = new String[]{
            DatabaseHelper.SO_PRON,
            DatabaseHelper.SO_MEANING,
            DatabaseHelper.SO_SYNONYMS
    };
    final int[] to = new int[]{
            R.id.txt_pron,
            R.id.txt_meaning,
            R.id.txt_synonyms
    };

    String _searchTerm = "";

    EditText editTextSearch;
    TextView txtView_welcome, txtView_result_count;

    View searchBar;

    ImageButton btnClearSearch, btnDrawerMenu, btn_search_web;

    View welcomeLayout;

    SharedPreferences sharedPref;
    String BnSearchType, EnSearchType;
    int pref_feedback_show_counter;
    ListView listView, listViewHistory;
    Button btn_clr_history;
    MenuItem nav_bn_calendar;
    /**
     * History
     */
    ArrayList<String> listItemsHistory = new ArrayList<>();
    ArrayAdapter<String> adapterHistory;
    /**
     * Update Info
     */

    String namedversion;
    String changelogurl;
    String downloadurl;
    String productpageurl;
    String releasedate;

    String Lang, UI_theme;
    String strTemp;

    HashMap<String, String> hashMap;


    int versionmajor;
    int versionminor;
    int versionrevision;

    XmlPullParser parser;
    AlertDialog alert;

    boolean isBackPressed = false;
    private DBManager dbManager;
    private CustomSimpleCursorAdapter adapter;

    private DrawerLayout mDrawerLayout;

    private View RightDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Lang = sharedPref.getString(preference_activity.pref_language, "bn");


        UI_theme = sharedPref.getString(preference_activity.pref_ui_theme, "light_green");

        so_tools.setUItheme(UI_theme, main_activity.this);


        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(preference_activity.pref_language)) {
                    RestartActivity();
                }
            }
        });


        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale locale = new Locale(Lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //============================================================

        setContentView(R.layout.mainlayout); // select default layout

        //===========================================================

        // All About Drawer init.

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view_left);
        navigationViewLeft.setNavigationItemSelectedListener(this);

        // get menu from navigationView
        Menu menu = navigationViewLeft.getMenu();

        // find MenuItem you want to change
        nav_bn_calendar = menu.findItem(R.id.textBanglaCalendar);

        RightDrawer = findViewById(R.id.right_drawer);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                showKeyboard();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        //============================================================

        //Load default preference for first time only. Must make the last argument is false.
        //Or every time the application reset it to default value.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //============================================================

        welcomeLayout = findViewById(R.id.layout_welcome);
        welcomeLayout.setVisibility(View.VISIBLE);
        welcomeLayout.setOnClickListener(this);

        //============================================================

        searchBar = findViewById(R.id.search_bar);

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.getBackground().setColorFilter(getResources()
                .getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        //============================================================

        btnClearSearch = (ImageButton) findViewById(R.id.searchClear);
        btnClearSearch.setOnClickListener(this);
        btnClearSearch.setOnLongClickListener(this);

        btn_search_web = (ImageButton) findViewById(R.id.searchWeb);
        btn_search_web.setOnClickListener(main_activity.this);
        btn_search_web.setOnLongClickListener(this);

        txtView_welcome = (TextView) findViewById(R.id.txt_welcome);
        txtView_result_count = (TextView) findViewById(R.id.textView_result_count);
        txtView_result_count.setVisibility(View.GONE);

        btnDrawerMenu = (ImageButton) findViewById(R.id.btnDrawerMenu);
        btnDrawerMenu.setOnClickListener(this);

        btn_clr_history = (Button) findViewById(R.id.btn_clr_history);
        btn_clr_history.setOnClickListener(this);

        //============================================================

        listViewHistory = (ListView) findViewById(R.id.list_view_history);

        listViewHistory.setEmptyView(findViewById(R.id.emptyHistory)); // for empty view

        listViewHistory.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView text = (TextView) view.findViewById(android.R.id.text1);

                editTextSearch.setText(text.getText());

                mDrawerLayout.closeDrawer(RightDrawer);

            }
        });

        //============================================================

        hashMap = new HashMap<>();

        //============================================================

        setSettings();

        //==================

        if (pref_feedback_show_counter == 15) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(preference_activity.pref_feedback_show_counter, ++pref_feedback_show_counter);
            editor.apply();

            Intent intt_feedback = new Intent(main_activity.this, feedback_activity.class);
            startActivity(intt_feedback);
        } else if (pref_feedback_show_counter < 15) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(preference_activity.pref_feedback_show_counter, ++pref_feedback_show_counter);
            editor.apply();
        }

        //============================================================

        dbManager = new DBManager(this);

        dbManager.open();

        //============================================================

        Cursor cursor = null;

        listView = (ListView) findViewById(R.id.list_view);

        listView.setEmptyView(findViewById(R.id.empty)); // for empty view

        adapter = new CustomSimpleCursorAdapter(this, R.layout.result_list_layout, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView idTextView = (TextView) view.findViewById(R.id.txt_pron);
                TextView descTextView = (TextView) view.findViewById(R.id.txt_meaning);
                TextView synoTextView = (TextView) view.findViewById(R.id.txt_synonyms);

                String s_word = idTextView.getText().toString();

                /**
                 * The text of the TextView is compiled in html
                 * so we save the string in TAG properties.. :)
                 */
                String s_meaning = descTextView.getTag().toString();
                String s_syno = synoTextView.getText().toString();

                /**
                 * Add in History
                 */
                listItemsHistory.add(s_word);
                adapterHistory.notifyDataSetChanged();

                Intent modify_intent = new Intent(getApplicationContext(), view_details_activity.class);
                modify_intent.putExtra("word", s_word);
                modify_intent.putExtra("meaning", so_tools.meaning_htmlfy_revert(s_meaning));
                modify_intent.putExtra("synonyms", s_syno);

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

                searchNow(s.toString());

            }
        });

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    /**
                     * Resetting @_searchTerm will force the searchNow() function to Query
                     */
                    _searchTerm = "";

                    searchNow(v.getText().toString());

                    return true;
                }
                return false;
            }
        });

        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    showKeyboard();
                } else {

                    hideKeyboard();
                }

            }
        });

        //Show keyboard
        editTextSearch.postDelayed(new Runnable() {
            @Override
            public void run() {

                showKeyboard();

            }
        }, 200);


        //=================================================================

        if (sharedPref.getBoolean(preference_activity.pref_key_auto_update_check, true)) {
            /**
             * Check Update (Maybe only for beta release.. :D )
             */
            // Gets the URL from the UI's text field.
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

                new GetDataTask().execute(getString(R.string.update_check_url));

            }
        }

        //======================================================
        adapterHistory = new ArrayAdapter<>(this,
                R.layout.simple_list_item,
                listItemsHistory);
        listViewHistory.setAdapter(adapterHistory);

        //======================================================

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }

        iw_bangla_calendar bnCalendar =
                new iw_bangla_calendar((GregorianCalendar) GregorianCalendar.getInstance());

        String str = bnCalendar.getFullDate();

        // set new title to the MenuItem
        nav_bn_calendar.setTitle(str);

    }

    void searchNow(String queryString) {
        if (!queryString.equals("")) {

            welcomeLayout.setVisibility(View.GONE);

            /**
             * If long text are delete using BackSpace
             * then a Cursor Exception occurred.
             * So, every time text changed, text saved into @_searchTerm variable.
             * Here we check the text length.
             * - If length decreased means text being deleted. so nothing will happened.
             */
            if (queryString.length() > _searchTerm.length()) {
                Cursor cursor;
                //Just change the adapter cursor to change the data view.. :)
                if (queryString.charAt(0) < 128) {
                    do {
                        cursor = dbManager.searchEN(queryString, EnSearchType);

                        if (queryString.length() >= 2)
                            queryString = queryString.substring(0, queryString.length() - 1);

                        if (queryString.equals(""))
                            break;

                    } while (cursor.getCount() == 0); // get the least match result

                } else {
                    do {
                        cursor = dbManager.searchBN(queryString, BnSearchType);

                        if (queryString.length() >= 2)
                            queryString = queryString.substring(0, queryString.length() - 1);

                        if (queryString.equals(""))
                            break;

                    } while (cursor.getCount() == 0);
                }

                int total_count = cursor.getCount();

                if (total_count != 0) {
                    adapter.changeCursor(cursor);
                    txtView_result_count.setVisibility(View.VISIBLE);
                    if (total_count > 1)
                        if (total_count <= 100)
                            txtView_result_count.setText(String.format(getString(R.string.total_result), total_count));
                        else
                            txtView_result_count.setText(
                                    String.format(getString(R.string.total_hundred_plus_result), 100));
                    else
                        txtView_result_count.setText(
                                String.format(getString(R.string.total_one_result), 1));
                }

                //Change to Clear Icon
                btnClearSearch.setImageResource(R.drawable.ic_close_black_24dp);

            }

            _searchTerm = queryString;


        } else {
            adapter.changeCursor(null);
            txtView_result_count.setVisibility(View.GONE);
            welcomeLayout.setVisibility(View.VISIBLE);

            //Change to Speak Icon
            btnClearSearch.setImageResource(R.drawable.ic_mic_black_24dp);

            _searchTerm = "";
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null && sharedText.length() <= 50) {
            editTextSearch.setText(sharedText);
            editTextSearch.setSelection(editTextSearch.getText().length());
        } else if (sharedText != null) {
            editTextSearch.setText(getString(R.string.invalid));
            editTextSearch.setSelection(editTextSearch.getText().length());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        editTextSearch.postDelayed(new Runnable() {
            @Override
            public void run() {

                mDrawerLayout.closeDrawers();
                showKeyboard();

            }
        }, 200);

        resetThings();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100: // View details
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    Boolean result = res.getBoolean("results");
                    String str = res.getString("synonym");
                    if (str != null) {
                        editTextSearch.setText(str);
                    } else if (result) {
                        editTextSearch.setText("");
                    }
                }
                break;
            case 200: // Add new Record
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
                    editTextSearch.setText(result);

                }

                break;

            case 500: // RESULT_SPEECH

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    editTextSearch.setText(text.get(0));
                }

                break;

            case 600: // Color Picker

                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String colorName = res.getString("ColorName");

                    // Save the theme name into the preference
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(preference_activity.pref_ui_theme, colorName);
                    editor.apply();

                    // restart activity to apply the theme
                    finish();
                    startActivity(getIntent());

                }


                break;
        }
    }

    //======================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchClear:
                if (editTextSearch.getText().toString().equals("")) {

                    Intent intent = new Intent(
                            RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                    try {
                        startActivityForResult(intent, 500);

                    } catch (ActivityNotFoundException a) {
                        Toast t = Toast.makeText(getApplicationContext(),
                                "Opps! Your device doesn't support Speech to Text!",
                                Toast.LENGTH_SHORT);
                        t.show();
                    }

                } else {
                    editTextSearch.setText("");
                    editTextSearch.requestFocus();
                    btnClearSearch.setImageResource(R.drawable.ic_mic_black_24dp);
                    showKeyboard();

                }
                break;

            case R.id.layout_welcome:

                showKeyboard();

                break;

            case R.id.btnDrawerMenu:

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

                break;


            case R.id.btn_clr_history:

                /**
                 * Clear History
                 */
                listItemsHistory.clear();
                adapterHistory.notifyDataSetChanged();

                mDrawerLayout.closeDrawer(RightDrawer);

                break;

            case R.id.searchWeb:

                strTemp = editTextSearch.getText().toString();

                if (!editTextSearch.getText().toString().equals("")) {

                    Intent iSearch = new Intent(Intent.ACTION_WEB_SEARCH);
                    String term;
                    Boolean isEn;
                    if (strTemp.charAt(0) < 128) {
                        term = "define " + strTemp;
                        isEn = true;
                    } else {
                        term = "translate " + strTemp;
                        isEn = false;
                    }

                    if(isEn)
                    {
                        sendData(getString(R.string.server_txt_sent_by_web_button_en), strTemp, "-");

                    }else{

                        sendData(getString(R.string.server_txt_sent_by_web_button_bn), strTemp, "-");
                    }



                    iSearch.putExtra(SearchManager.QUERY, term);
                    startActivity(iSearch);
                } else {
                    Toast.makeText(main_activity.this,
                            getString(R.string.enter_any_search_term_first), Toast.LENGTH_LONG).show();
                }

                break;

        }
    }


    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.searchWeb:

                Toast.makeText(main_activity.this,
                        getString(R.string.desc_web_search), Toast.LENGTH_LONG).show();

                break;

            case R.id.searchClear:

                if (editTextSearch.getText().toString().equals("")) {

                    Toast.makeText(main_activity.this,
                            getString(R.string.desc_speech_to_text), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(main_activity.this,
                            getString(R.string.desc_clear_search), Toast.LENGTH_LONG).show();
                }

                break;
        }

        return true;
    }

    private void setSettings() {
        BnSearchType = sharedPref.getString(preference_activity.bnAdvSearchType, "2");

        EnSearchType = sharedPref.getString(preference_activity.enAdvSearchType, "1");

        pref_feedback_show_counter = sharedPref.getInt(preference_activity.pref_feedback_show_counter, 0);

    }

    private void RestartActivity() {

        main_activity.this.recreate();

    }

    void showKeyboard() {
        //Show Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    void hideKeyboard() {
        //Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
    }

    public String GetData(String requestURL) throws IOException {

        java.net.URL url;
        String response = "";
        List ls = new ArrayList();

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream in = conn.getInputStream();


            try {
                parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag(); //versioninfo
                parser.nextTag(); //namedversion

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        parser.next();
                        ls.add(parser.getText());
                    }

                    eventType = parser.next();

                }

                namedversion = (String) ls.get(0);
                try {
                    versionmajor = Integer.parseInt(ls.get(1).toString());
                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

                try {
                    versionminor = Integer.parseInt(ls.get(2).toString());
                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

                try {
                    versionrevision = Integer.parseInt(ls.get(3).toString());
                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

                changelogurl = (String) ls.get(4);
                downloadurl = (String) ls.get(5);
                productpageurl = (String) ls.get(6);
                releasedate = (String) ls.get(7);


            } finally {
                in.close();
            }

        } catch (Exception e) {
            //e.printStackTrace();
            response = "e";
        }

        return response;
    }

    void UpdateFound() {
        AlertDialog.Builder adb = new AlertDialog.Builder(main_activity.this);

        adb.setTitle(
                Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.update_available) + "</font>"));

        String s =
                String.format(getString(R.string.new_update_info)
                        , versionmajor, versionminor, versionrevision) + "\n" +
                        String.format(getString(R.string.update_release_date) + "\n\n" +
                                getString(R.string.do_you_want_to_download_now), releasedate);

        adb.setMessage(s);

        adb.setNegativeButton(getString(R.string.no), null);
        adb.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String URL_market = getString(R.string.url_market);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(URL_market));
                startActivity(browserIntent);

                alert.cancel();
            }
        });


        alert = adb.create();
        alert.show();
    }

    void sendData(String info, String word, String meaning) {
        hashMap.clear();

        //?arg1=val1&arg2=val2
        hashMap.put("info", info);
        hashMap.put("word", so_tools.removeSymbolFromText(word));
        hashMap.put("pron", word);
        hashMap.put("meaning", meaning);

        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new sendDataToServer(hashMap, getString(R.string.server_post_url));

        }

    }

    @Override
    public void onBackPressed() {

        if (isBackPressed) {
            super.onBackPressed();
            return;
        }

        isBackPressed = true;
        Toast.makeText(main_activity.this, "Press once again to exit", Toast.LENGTH_LONG)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                isBackPressed = false;
            }

        }, 2000);

    }

    void resetThings() {

        iw_bangla_calendar bnCalendar =
                new iw_bangla_calendar((GregorianCalendar) GregorianCalendar.getInstance());

        String str = bnCalendar.getFullDate();

        nav_bn_calendar.setTitle(str);

        isBackPressed = false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.textBanglaCalendar) {

            Toast
                    .makeText(main_activity.this,
                            "Bangla Calendar - Bangladesh version", Toast.LENGTH_LONG)
                    .show();

            Intent bnCalendar_intent = new Intent(main_activity.this, bn_calendar_activity.class);
            startActivity(bnCalendar_intent);

        } else if (id == R.id.favorite) {

            Intent fav_intent = new Intent(main_activity.this, favorite_list_activity.class);
            startActivityForResult(fav_intent, 400);


        } else if (id == R.id.history) {

            mDrawerLayout.openDrawer(RightDrawer);

        } else if (id == R.id.greek_alp) {

            Intent greek_alp_intent = new Intent(main_activity.this, greek_alphabet_activity.class);
            startActivity(greek_alp_intent);

            /**
             * Almost no one use this feature.
             */

//        } else if (id == R.id.add_record) {

//            Intent add_entry_intent = new Intent(main_activity.this, add_new_entry.class);
//            startActivityForResult(add_entry_intent, 200);

//            Toast.makeText(main_activity.this, "Under construction. Coming soon...", Toast.LENGTH_LONG).show();

        } else if (id == R.id.menu_suggestion) {

            Intent suggestion_intent = new Intent(main_activity.this, SuggestionActivity.class);
            startActivity(suggestion_intent);

        } else if (id == R.id.prefs) {

            Intent prefs_intent = new Intent(main_activity.this, preference_activity.class);
            startActivityForResult(prefs_intent, 300);

        } else if (id == R.id.tutorial) {

            Intent tutorial_intent = new Intent(main_activity.this, tutorial_activity.class);
            startActivity(tutorial_intent);

        } else if (id == R.id.about) {

            Intent about_intent = new Intent(main_activity.this, about_activity.class);
            startActivity(about_intent);

        } else if (id == R.id.giveRating) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getResources().getString(R.string.url_market)));
            startActivity(intent);

        } else if (id == R.id.exit) {

            //API >= 16
            main_activity.this.finishAffinity();

        } else if (id == R.id.change_theme_color) {

            Intent color_intent = new Intent(main_activity.this, ColorPickerActivity.class);
            color_intent.putExtra(getString(R.string.ColorName), UI_theme);
            startActivityForResult(color_intent, 600);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class GetDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return GetData(urls[0]);
            } catch (IOException e) {
                return e.toString();
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (result.equals("")) {

                String version = getString(R.string.app_version);
                String[] v;
                v = version.split("\\.");

                try {

                    if (Integer.parseInt(v[0]) < versionmajor) {
                        UpdateFound();
                    } else if (Integer.parseInt(v[0]) == versionmajor) {
                        if (Integer.parseInt(v[1]) < versionminor) {
                            UpdateFound();
                        } else if (Integer.parseInt(v[1]) == versionminor) {
                            if (Integer.parseInt(v[2]) < versionrevision) {
                                UpdateFound();
                            }
                        }
                    }

                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

            }

        }

    }
}














