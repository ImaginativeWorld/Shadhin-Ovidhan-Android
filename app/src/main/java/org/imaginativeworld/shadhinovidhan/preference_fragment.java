package org.imaginativeworld.shadhinovidhan;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Shohag on 16 Aug 15.
 */
public class preference_fragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);


    }

}
