/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textViewSuggestion;
    private Button btnSend;
    private View suggestion_view;
    private TextView textSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Set Dialog Theme
         */
        String UI_theme = sharedPref.getString(preference_activity.pref_ui_theme, "light_green");
        so_tools.setDialogUItheme(UI_theme, SuggestionActivity.this);

        setContentView(R.layout.activity_suggestion);

        textViewSuggestion = (EditText) findViewById(R.id.suggestion);

        Button btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        suggestion_view = findViewById(R.id.suggestion_form);
        textSuccess = (TextView) findViewById(R.id.textSuccess);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:

                finish();

                break;

            case R.id.btn_send:

                String strEmail = ((EditText) findViewById(R.id.email)).getText().toString();
                String strSuggestion = ((EditText) findViewById(R.id.suggestion)).getText().toString();

                if(TextUtils.isEmpty(strEmail))
                    strEmail = getString(R.string.const_unknown);

                if (TextUtils.isEmpty(strSuggestion)) {
                    textViewSuggestion.setError(getString(R.string.err_enter_suggestion));
                } else {
                    /**
                     * Check internet connection
                     */
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {

                        int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

                        so_tools.sendData(getString(R.string.server_txt_suggestion), strEmail, strSuggestion, null);

                        btnSend.animate().setDuration(shortAnimTime).alpha(
                                0).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                btnSend.setVisibility(View.GONE);
                            }
                        });

                        suggestion_view.animate().setDuration(shortAnimTime).alpha(
                                0).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                suggestion_view.setVisibility(View.GONE);
                            }
                        });

                        textSuccess.setVisibility(View.VISIBLE);
                        textSuccess.animate().setDuration(shortAnimTime).alpha(
                                1).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }
                        });

                    } else {
                        Toast.makeText(SuggestionActivity.this,
                                getString(R.string.internet_not_connected),
                                Toast.LENGTH_LONG).show();
                    }


                }

                break;
        }
    }
}
