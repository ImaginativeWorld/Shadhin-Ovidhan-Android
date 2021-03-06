/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class feedback_activity extends AppCompatActivity implements View.OnClickListener {

    Resources res;
    private String URL_market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feedback_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //==================================================
        ImageButton imgBtn_like = (ImageButton) findViewById(R.id.imgBtn_like);
        imgBtn_like.setOnClickListener(feedback_activity.this);

        ImageButton imgBtn_dislike = (ImageButton) findViewById(R.id.imgBtn_dislike);
        imgBtn_dislike.setOnClickListener(feedback_activity.this);

        Button imgBtn_close = (Button) findViewById(R.id.btn_close);
        imgBtn_close.setOnClickListener(feedback_activity.this);

        //==================================================
        //res = getResources();

        URL_market = getString(R.string.url_market);
        //URL_feedback = getString(R.string.url_feedback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_like:
                Toast t1 = Toast.makeText(feedback_activity.this,
                        getString(R.string.feedback_give_us_five_stars),
                        Toast.LENGTH_LONG);
                t1.show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL_market));
                startActivity(intent);

                finish();

                break;

            case R.id.imgBtn_dislike:
                Toast t2 = Toast.makeText(feedback_activity.this,
                        getString(R.string.feedback_tell_us_what_you_need),
                        Toast.LENGTH_LONG);
                t2.show();

                Intent suggestion_intent = new Intent(feedback_activity.this, SuggestionActivity.class);
                startActivity(suggestion_intent);

                finish();

                break;

            case R.id.btn_close:

                finish();

                break;
        }
    }
}
