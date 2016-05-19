/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.Application;
import android.content.Context;

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
