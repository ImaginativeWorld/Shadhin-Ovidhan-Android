package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Shohag on 02 Aug 15.
 */
public class about_activity extends Activity implements View.OnClickListener {

    ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        //Make window fill full width
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //=========================================================
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(about_activity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:

                finish();

                break;
        }

    }
}
