/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class about_activity extends AppCompatActivity implements OnClickListener {


    Button btnClose;
    Button btnPrivacyPolicy;

    Resources res;

    TextView dev_url;
    TextView txt_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Set Dialog Theme
         */
        String UI_theme = sharedPref.getString(preference_activity.pref_ui_theme, "light_green");
        so_tools.setDialogUItheme(UI_theme, about_activity.this);

        /**
         * Change Language
         */
        String Lang = sharedPref.getString(preference_activity.pref_language, "bn");

        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale locale = new Locale(Lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //============================================================

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_layout);

        //============================================================

        btnClose =findViewById(R.id.btn_close);
        btnClose.setOnClickListener(about_activity.this);

        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy);
        btnPrivacyPolicy.setOnClickListener(about_activity.this);

        dev_url = findViewById(R.id.dev_url);
        dev_url.setOnClickListener(about_activity.this);

        res = getResources();

        txt_version = findViewById(R.id.txt_version);
        txt_version.setText(String.format("%s", BuildConfig.VERSION_NAME));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                finish();

                break;


            case R.id.btn_privacy_policy:

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://imaginativeworld.org/privacy-policy-shadhin-ovidhan/")));

                break;


            case R.id.dev_url:

                //Go to the url
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.website)));
                startActivity(browserIntent);

                break;
        }

    }


}
