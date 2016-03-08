package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 *
 * Created by Shohag on 23 Oct 15.
 *
 */
public class feedback_activity extends Activity implements View.OnClickListener {

    Resources res;
    private String URL_market, URL_feedback;

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
        res = getResources();

        URL_market = getString(R.string.url_market);
        URL_feedback = getString(R.string.url_feedback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_like:
                Toast t1 = Toast.makeText(feedback_activity.this,
                        getString(R.string.feedback_give_us_five_star),
                        Toast.LENGTH_LONG);
                t1.show();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL_market));
                startActivity(intent);
                break;

            case R.id.imgBtn_dislike:
                Toast t2 = Toast.makeText(feedback_activity.this,
                        getString(R.string.feedback_tell_us_what_you_need),
                        Toast.LENGTH_LONG);
                t2.show();

                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(URL_feedback));
                startActivity(browserIntent2);
                break;

            case R.id.btn_close:

                finish();

                break;
        }
    }
}
