package org.imaginativeworld.shadhinovidhan;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shohag on 27 Aug 15.
 */

/**
 * Cheating to get "Context" everywhere.. :D
 */
public class appContextHelper extends Application {

    private static Context context;

    public static Context getAppContext() {
        return appContextHelper.context;
    }

    public void onCreate() {
        super.onCreate();
        appContextHelper.context = getApplicationContext();
    }


}
