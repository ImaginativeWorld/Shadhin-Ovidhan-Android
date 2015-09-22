package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shohag on 18 Jul 15.
 */
public class main_activity extends Activity implements View.OnClickListener {

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

    //Toolbar toolbar;

    EditText editTextSearch;
    TextView txtView_welcome;

    View searchBar;

    ImageButton btnClearSearch, btnDrawerMenu;

    View welcomeLayout;

    SharedPreferences sharedPref;
    String BnSearchType, EnSearchType;
    ListView listView, listViewHistory;
    Button btn_add_record, btn_favorite, btn_history, btn_prefs, btn_about, btn_exit, btn_clr_history;
    /**
     * History
     */
    ArrayList<String> listItemsHistory = new ArrayList<String>();
    ArrayAdapter<String> adapterHistory;
    /**
     * Update Info
     */

    String namedversion,
            changelogurl, downloadurl, productpageurl,
            releasedate;

    //private ActionBarDrawerToggle mDrawerToggle;
    int versionmajor, versionminor, versionrevision;
    XmlPullParser parser;
    AlertDialog alert;
    //private AutoCompleteTextView SrcTxtView;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    //String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    //    ListView mDrawerList;
    private View LeftDrawer, RightDrawer;

//    void showSearchBar() {
//
//        editTextSearch.setEnabled(true);
//        editTextSearch.requestFocus();
//
////        toolbar.animate()
////                .translationY(-toolbar.getBottom())
////                .setInterpolator(new AccelerateInterpolator())
////                .start();
//
//        searchBar.animate()
//                .translationY(0)
//                .setInterpolator(new DecelerateInterpolator())
//                .start();
//
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
//
//        //txtView_welcome.setText(getString(R.string.main_window_welcome_text_search));
//
//    }

//    void hideSearchBar() {
//
//        toolbar.animate()
//                .translationY(0)
//                .setInterpolator(new DecelerateInterpolator())
//                .start();
//
//        searchBar.animate()
//                .translationY(-searchBar.getBottom())
//                .setInterpolator(new AccelerateInterpolator())
//                .start();
//
//        editTextSearch.setEnabled(false);
//
//        //txtView_welcome.setText(getString(R.string.main_window_welcome_text_hint));
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.mainlayout); // select default layout

        //============================================================

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        toolbar.setVisibility(View.VISIBLE);


        //===========================================================

        // All About Drawer init.

        //mPlanetTitles = getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.drawer_list_view);
        LeftDrawer = findViewById(R.id.left_drawer);
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

//        mDrawerToggle = new ActionBarDrawerToggle(main_activity.this, mDrawerLayout,
//                R.string.drawer_open, R.string.drawer_close) {
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                //Show Keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
//
//                Toast t = Toast.makeText(main_activity.this, "a", Toast.LENGTH_LONG);
//                t.show();
//            }
//
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                //hide keyboard
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
//
//                Toast t = Toast.makeText(main_activity.this, "b", Toast.LENGTH_LONG);
//                t.show();
//            }
//
//        };


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
        //searchBar.setY(-getPixels(56));


        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setHint(R.string.search_hint);
        editTextSearch.getBackground().setColorFilter(getResources()
                .getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        //============================================================

        btnClearSearch = (ImageButton) findViewById(R.id.searchClear);
        btnClearSearch.setOnClickListener(this);

        txtView_welcome = (TextView) findViewById(R.id.txt_welcome);

        btnDrawerMenu = (ImageButton) findViewById(R.id.btnDrawerMenu);
        btnDrawerMenu.setOnClickListener(this);

        btn_clr_history = (Button) findViewById(R.id.btn_clr_history);
        btn_clr_history.setOnClickListener(this);

        //============================================================

        btn_add_record = (Button) findViewById(R.id.add_record);
        btn_add_record.setOnClickListener(main_activity.this);

        btn_favorite = (Button) findViewById(R.id.favorite);
        btn_favorite.setOnClickListener(main_activity.this);

        btn_history = (Button) findViewById(R.id.history);
        btn_history.setOnClickListener(main_activity.this);

        btn_prefs = (Button) findViewById(R.id.prefs);
        btn_prefs.setOnClickListener(main_activity.this);

        btn_about = (Button) findViewById(R.id.about);
        btn_about.setOnClickListener(main_activity.this);

        btn_exit = (Button) findViewById(R.id.exit);
        btn_exit.setOnClickListener(main_activity.this);

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

                /**
                 * Add in History
                 */
                listItemsHistory.add(s_word);
                adapterHistory.notifyDataSetChanged();

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

                            if (str.length() >= 2)
                                str = str.substring(0, str.length() - 1);

                            if (str.equals(""))
                                break;

                        } while (cursor.getCount() == 0);

                    } else {
                        do {
                            cursor = dbManager.searchBN(str, BnSearchType);

                            if (str.length() >= 2)
                                str = str.substring(0, str.length() - 1);

                            if (str.equals(""))
                                break;

                        } while (cursor.getCount() == 0);
                    }

                    if (cursor.getCount() != 0)
                        adapter.changeCursor(cursor);

                    //Change to Clear Icon
                    btnClearSearch.setImageResource(R.drawable.ic_clear_white_48dp);

                } else {
                    adapter.changeCursor(null);
                    welcomeLayout.setVisibility(View.VISIBLE);

                    //Change to Speak Icon
                    btnClearSearch.setImageResource(android.R.drawable.ic_btn_speak_now);
                }

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
//                InputMethodManager keyboard = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.showSoftInput(editTextSearch, 0);

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
        adapterHistory = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItemsHistory);
        listViewHistory.setAdapter(adapterHistory);
    }

    @Override
    protected void onResume() {
        super.onResume();

        editTextSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
//                InputMethodManager keyboard = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.showSoftInput(editTextSearch, 0);

                showKeyboard();

            }
        }, 200);


    }

//    private int getPixels(int dipValue) {
//        Resources r = getResources();
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
//    }

    @Override
    protected void onStart() {
        super.onStart();


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

            case 500: // RESULT_SPEECH

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    editTextSearch.setText(text.get(0));
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
                    btnClearSearch.setImageResource(android.R.drawable.ic_btn_speak_now);

                }
                break;

            case R.id.layout_welcome:

                showKeyboard();

                break;

            case R.id.btnDrawerMenu:

                mDrawerLayout.openDrawer(LeftDrawer);

                break;

            //===================================

            case R.id.add_record:

                Intent add_entry_intent = new Intent(main_activity.this, add_new_entry.class);
                startActivityForResult(add_entry_intent, 200);


                mDrawerLayout.closeDrawer(LeftDrawer);

                break;

            case R.id.favorite:

                Intent fav_intent = new Intent(main_activity.this, favorite_list_activity.class);
                startActivityForResult(fav_intent, 400);


                mDrawerLayout.closeDrawer(LeftDrawer);

                break;

            case R.id.history:

                mDrawerLayout.closeDrawer(LeftDrawer);
                mDrawerLayout.openDrawer(RightDrawer);

                break;

            case R.id.prefs:

                Intent prefs_intent = new Intent(main_activity.this, preference_activity.class);
                startActivityForResult(prefs_intent, 300);


                mDrawerLayout.closeDrawer(LeftDrawer);

                break;

            case R.id.about:

                Intent intent = new Intent(main_activity.this, about_activity.class);
                startActivity(intent);


                mDrawerLayout.closeDrawer(LeftDrawer);

                break;

            case R.id.exit:

                //API >= 16
                main_activity.this.finishAffinity();


                mDrawerLayout.closeDrawer(LeftDrawer);

                break;

            case R.id.btn_clr_history:

                /**
                 * Clear History
                 */
                listItemsHistory.clear();
                adapterHistory.notifyDataSetChanged();

                mDrawerLayout.closeDrawer(RightDrawer);

                break;


        }
    }

    private void setSettings() {
        BnSearchType = sharedPref.getString(preference_activity.bnAdvSearchType, "2");

        EnSearchType = sharedPref.getString(preference_activity.enAdvSearchType, "1");
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

        adb.setTitle(getString(R.string.update_available));

        String s =
                String.format(getString(R.string.new_update_info)
                        , versionmajor, versionminor, versionrevision) + "\n" +
                        String.format(getString(R.string.update_release_date), releasedate);

        adb.setMessage(s);

        adb.setNegativeButton(getString(R.string.no), null);
        adb.setPositiveButton(getString(R.string.yes), new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(productpageurl));
                startActivity(browserIntent);
                alert.cancel();
            }
        });

        alert = adb.create();
        alert.show();
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

//                Toast tt = Toast.makeText(about_activity.this,
//                        versionmajor + "." + versionminor+ "." + versionrevision + " " + namedversion,Toast.LENGTH_LONG );
//                tt.show();

                try {

                    if (Integer.parseInt(v[0]) < versionmajor) {
                        UpdateFound();

                    } else if (Integer.parseInt(v[1]) < versionminor) {
                        UpdateFound();

                    } else if (Integer.parseInt(v[2]) < versionrevision) {
                        UpdateFound();

                    }
//                    else
//                        UpdateNotFound();

                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

            }
//            else {
//                btnUpdate.setText(getString(R.string.update_error));
//                btnUpdate.setEnabled(true);
//            }

        }
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




























