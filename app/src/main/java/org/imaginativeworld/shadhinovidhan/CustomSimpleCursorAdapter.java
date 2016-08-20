/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomSimpleCursorAdapter extends SimpleCursorAdapter {

    /**
     * Code here learned from:
     * http://alvinalexander.com/java/jwarehouse/android/core/java/android/widget/SimpleCursorAdapter.java.shtml
     */

    private int[] mFrom;

    private int[] mTo;

    private ViewBinder mViewBinder;


    public CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        mFrom = new int[]{
                1,
                2,
                3
        };
        mTo = to;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewBinder binder = mViewBinder;
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    String text2 = text;

                    // Separate and format the meaning
                    if (i == 1) { // meaning view
                        text = so_tools.meaning_htmlfy(text);
                    }

                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {

                        if (i == 1) { // meaning view

                            ((TextView) v).setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                            /**
                             * The text of the TextView is compiled in html
                             * so we save the string in TAG properties.. :)
                             */
                            v.setTag(text2);

                        } else {
                            setViewText((TextView) v, text);
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this CSCA");
                    }
                }
            }
        }
    }
}
