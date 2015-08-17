package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Shohag on 16 Aug 15.
 */
public class preference_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.preference_layout);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new preference_fragment())
                .commit();

    }
}
