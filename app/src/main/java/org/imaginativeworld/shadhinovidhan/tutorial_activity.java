package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by Shohag on 08 Mar 16.
 */

public class tutorial_activity extends Activity implements View.OnClickListener {

    WebView webView;
    Button btnClose, btnIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tutorial_layout);


//        Intent intent = getIntent();
//        String goWhere = intent.getStringExtra("goWhere");

        webView = (WebView) findViewById(R.id.webView);

//        if(goWhere!=null)
//        {
//            if(goWhere.equals("backupNow"))
//                webView.loadUrl("file:///android_asset/tutorials/fav-backup-why.html");
//        }
//        else
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
