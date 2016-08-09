/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class about_activity extends AppCompatActivity implements OnClickListener {

    String namedversion,
            changelogurl, downloadurl, productpageurl,
            releasedate;
    int versionmajor, versionminor, versionrevision;
    XmlPullParser parser;
    Button btnClose;
    Button btnUpdate;
    View updateInfoView;
    TextView txtNewVersion, txtReleaseDate;
    Resources res;
    boolean ifUpdateAvailable = false;
    TextView dev_url;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //=========================================================
        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(about_activity.this);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(about_activity.this);

        updateInfoView = findViewById(R.id.view_update);
        updateInfoView.setVisibility(View.GONE);

        txtNewVersion = (TextView) findViewById(R.id.txtNewVersion);
        txtReleaseDate = (TextView) findViewById(R.id.txtReleaseDate);

        dev_url = (TextView) findViewById(R.id.dev_url);
        dev_url.setOnClickListener(about_activity.this);

        res = getResources();

        URL = getString(R.string.update_check_url);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                finish();

                break;

            case R.id.btn_update:

                if (ifUpdateAvailable) {
                    String URL_market = getString(R.string.url_market);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(URL_market));
                    startActivity(browserIntent);
                } else {

                    // Gets the URL from the UI's text field.
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        btnUpdate.setText(getString(R.string.wait));
                        btnUpdate.setEnabled(false);
                        new GetDataTask().execute(URL);
                    } else {
                        btnUpdate.setText(getString(R.string.update_error));
                        btnUpdate.setEnabled(true);

                        Toast t = Toast.makeText(about_activity.this,
                                getString(R.string.internet_not_connected),
                                Toast.LENGTH_LONG);
                        t.show();
                    }

                }

                break;

            case R.id.dev_url:

                //Go to the url
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.website)));
                startActivity(browserIntent);

                break;
        }

    }

    void UpdateFound() {
        btnUpdate.setText(getString(R.string.update_now));
        txtNewVersion.setText(String.format(res.getString(R.string.new_update_info),
                versionmajor, versionminor, versionrevision));

        txtReleaseDate.setText(String.format(res.getString(R.string.update_release_date),
                releasedate));

        updateInfoView.setVisibility(View.VISIBLE);

        ifUpdateAvailable = true;
        btnUpdate.setEnabled(true);

    }

    void UpdateNotFound() {
        btnUpdate.setText(getString(R.string.update_not_found));
        btnUpdate.setEnabled(false);
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
                    } else if (Integer.parseInt(v[0]) == versionmajor) {
                        if (Integer.parseInt(v[1]) < versionminor) {
                            UpdateFound();
                        } else if (Integer.parseInt(v[1]) == versionminor) {
                            if (Integer.parseInt(v[2]) < versionrevision) {
                                UpdateFound();
                            } else
                                UpdateNotFound();
                        } else
                            UpdateNotFound();
                    } else
                        UpdateNotFound();

                } catch (NumberFormatException nfe) {
                    //System.out.println("Could not parse " + nfe);
                }

            } else {
                btnUpdate.setText(getString(R.string.update_error));
                btnUpdate.setEnabled(true);
            }

        }
    }

}
