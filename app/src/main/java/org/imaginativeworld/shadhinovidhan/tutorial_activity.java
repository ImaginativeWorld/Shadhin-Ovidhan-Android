/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.Locale;

public class tutorial_activity extends AppCompatActivity implements View.OnClickListener {

    WebView webView;
    Button btnClose, btnIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

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

        setContentView(R.layout.tutorial_layout);

        //============================================================

        webView = (WebView) findViewById(R.id.webView);

        /**
         * Fix: FileUriExposedException
         */
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        };
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("file:///android_asset/tutorials/index.html");

        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(tutorial_activity.this);

        btnIndex = (Button) findViewById(R.id.btn_index);
        btnIndex.setOnClickListener(tutorial_activity.this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;

            case R.id.btn_index:
                webView.loadUrl("file:///android_asset/tutorials/index.html");
                break;
        }

    }
}
