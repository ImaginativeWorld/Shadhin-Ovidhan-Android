package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shohag on 02 Aug 15.
 */
public class about_activity extends Activity implements OnClickListener {

    ImageButton btnClose, btnSend;

    private String URL_NEW_PREDICTION = "http://10.0.2.2/so/new_predict.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //=========================================================
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(about_activity.this);

        btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(about_activity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                finish();

                break;

            case R.id.btn_send:

                new AddNewPrediction().execute("shohag", "n", "সোহাগ; মাহমুদুল; হাসান");

                break;
        }

    }

    private class AddNewPrediction extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub
            String goalNo = arg[0];
            String cardNo = arg[1];
            String posDiff = arg[2];

            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("goalNo", goalNo));
            params.add(new BasicNameValuePair("cardNo", cardNo));
            params.add(new BasicNameValuePair("posDiff", posDiff));

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_NEW_PREDICTION,
                    ServiceHandler.POST, params);

            Log.d("Create P Request:", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("P added successfully ",
                                "> " + jsonObj.getString("message"));
                    } else {
                        Log.e("Add Prediction Error: ",
                                "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "JSON data error!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
