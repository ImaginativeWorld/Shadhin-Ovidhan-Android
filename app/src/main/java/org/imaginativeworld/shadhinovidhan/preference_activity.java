/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class preference_activity extends PreferenceActivity {

    public final static String bnAdvSearchType = "bnAdvSearchType";
    public final static String enAdvSearchType = "enAdvSearchType";
    public final static String pref_search_on_type = "pref_search_on_type";
    public final static String pref_key_send_to_server = "pref_key_send_to_server";
    public final static String pref_key_auto_update_check = "pref_key_auto_update_check";
    public final static String pref_feedback_show_counter = "pref_feedback_show_counter";
    public final static String pref_language = "pref_lang";
    public final static String pref_ui_theme = "pref_ui_theme";

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
