package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Shohag on 16 Aug 15.
 */
public class preference_activity extends PreferenceActivity {

    public static String bnAdvSearchType = "bnAdvSearchType",
            enAdvSearchType = "enAdvSearchType",
            pref_key_send_to_server = "pref_key_send_to_server",
            pref_key_auto_update_check = "pref_key_auto_update_check",
            pref_feedback_show_counter = "pref_feedback_show_counter",
            pref_language = "pref_lang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new preference_fragment())
                .commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Bundle conData = new Bundle();
        conData.putBoolean("results", true);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);

    }

}
