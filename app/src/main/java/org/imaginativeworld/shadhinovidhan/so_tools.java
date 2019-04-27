/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Field;
import java.util.HashMap;

public class so_tools {

    static String removeSymbolFromText(String text, boolean isRemoveSpace, boolean isRemoveSemiColon) {

        if (isRemoveSpace) {
            text = text.replace(" ", "");
        }

        if (isRemoveSemiColon) {
            text = text.replace(";", "");
        }

        text = text
                .replace("_", "")
                .replace("-", "")
                .replace(".", "")
                .replace("'", "")
                .replace("[", "")
                .replace("]", "")
                .replace("{", "")
                .replace("}", "")
                .replace("(", "")
                .replace(")", "")
                .replace("<", "")
                .replace(">", "")
                .replace(",", "")
                .replace("?", "")
                .replace("!", "")
                .replace("@", "")
                .replace("$", "")
                .replace("^", "")
                .replace("+", "")
                .replace("=", "")
                .replace("|", "")
                .replace("\\", "")
                .replace("/", "")
                .replace("&", "")
                .replace("*", "")
                .replace("#", "")
                .replace("%", "")
                .replace("\"", "")
                .replace("~", "")
                .replace("`", "");

        return text.replaceAll(" +", " ");
    }

    public static void sendData(String info, String word, String meaning, String synonyms) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();

        //?arg1=val1&arg2=val2
        hashMap.put("info", info);
        hashMap.put("word", so_tools.removeSymbolFromText(word, true, true));
        hashMap.put("pron", word);
        hashMap.put("meaning", meaning);
        if (synonyms != null)
            hashMap.put("synonyms", synonyms);

        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                appContextHelper.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new sendDataToServer(hashMap, appContextHelper.getAppContext().getString(R.string.server_post_url));

//            Toast t = Toast.makeText(appContextHelper.getAppContext(),
//                    appContextHelper.getAppContext().getString(R.string.txt_entry_sent_to_server), Toast.LENGTH_SHORT);
//            t.show();
        }

    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String meaning_htmlfy(String text) {
        text = text
                .replace("|", "<br />")
                .replace("[", "<em><font color=\"#616161\">") // Grey_700
                .replace("]", "</font></em> ");

        return text;
    }

    public static String meaning_htmlfy_revert(String text) {
        text = text
                .replace("<br />", "|")
                .replace("<em>", "[")
                .replace("</em> ", "]");

        return text;
    }

    /**
     * Change activity theme.
     * This is specially for @main_activity
     * Because for some reason @main_activity is not AppCompat
     *
     * @param themeName Name of the Theme.
     * @param context   Applied context.
     */
    public static void setUItheme(String themeName, Context context) {
        switch (themeName) {
            case "red":
                context.setTheme(R.style.AppTheme_red);
                break;

            case "pink":
                context.setTheme(R.style.AppTheme_pink);
                break;

            case "purple":
                context.setTheme(R.style.AppTheme_purple);
                break;

            case "deep_purple":
                context.setTheme(R.style.AppTheme_deep_purple);
                break;

            case "indigo":
                context.setTheme(R.style.AppTheme_indigo);
                break;

            case "blue":
                context.setTheme(R.style.AppTheme_blue);
                break;

            case "light_blue":
                context.setTheme(R.style.AppTheme_light_blue);
                break;

            case "cyan":
                context.setTheme(R.style.AppTheme_cyan);
                break;

            case "teal":
                context.setTheme(R.style.AppTheme_teal);
                break;

            case "green":
                context.setTheme(R.style.AppTheme_green);
                break;

            case "light_green":
                context.setTheme(R.style.AppTheme_light_green);
                break;

            case "lime":
                context.setTheme(R.style.AppTheme_lime);
                break;

            case "yellow":
                context.setTheme(R.style.AppTheme_yellow);
                break;

            case "amber":
                context.setTheme(R.style.AppTheme_amber);
                break;

            case "orange":
                context.setTheme(R.style.AppTheme_orange);
                break;

            case "deep_orange":
                context.setTheme(R.style.AppTheme_deep_orange);
                break;

            case "brown":
                context.setTheme(R.style.AppTheme_brown);
                break;

            case "grey":
                context.setTheme(R.style.AppTheme_grey);
                break;

            case "blue_grey":
                context.setTheme(R.style.AppTheme_blue_grey);
                break;

            case "black":
                context.setTheme(R.style.AppTheme_black);
                break;

            default:
                context.setTheme(R.style.AppTheme_light_green); //light_green
                break;
        }

    }

    /**
     * Change activity theme.
     * This is for other dialog activity, whose are support AppCompat.
     *
     * @param themeName Name of the Theme.
     * @param context   Applied context.
     */
    public static void setDialogUItheme(String themeName, Context context) {
        switch (themeName) {
            case "red":
                context.setTheme(R.style.AppThemeDialog_red);
                break;

            case "pink":
                context.setTheme(R.style.AppThemeDialog_pink);
                break;

            case "purple":
                context.setTheme(R.style.AppThemeDialog_purple);
                break;

            case "deep_purple":
                context.setTheme(R.style.AppThemeDialog_deep_purple);
                break;

            case "indigo":
                context.setTheme(R.style.AppThemeDialog_indigo);
                break;

            case "blue":
                context.setTheme(R.style.AppThemeDialog_blue);
                break;

            case "light_blue":
                context.setTheme(R.style.AppThemeDialog_light_blue);
                break;

            case "cyan":
                context.setTheme(R.style.AppThemeDialog_cyan);
                break;

            case "teal":
                context.setTheme(R.style.AppThemeDialog_teal);
                break;

            case "green":
                context.setTheme(R.style.AppThemeDialog_green);
                break;

            case "light_green":
                context.setTheme(R.style.AppThemeDialog_light_green);
                break;

            case "lime":
                context.setTheme(R.style.AppThemeDialog_lime);
                break;

            case "yellow":
                context.setTheme(R.style.AppThemeDialog_yellow);
                break;

            case "amber":
                context.setTheme(R.style.AppThemeDialog_amber);
                break;

            case "orange":
                context.setTheme(R.style.AppThemeDialog_orange);
                break;

            case "deep_orange":
                context.setTheme(R.style.AppThemeDialog_deep_orange);
                break;

            case "brown":
                context.setTheme(R.style.AppThemeDialog_brown);
                break;

            case "grey":
                context.setTheme(R.style.AppThemeDialog_grey);
                break;

            case "blue_grey":
                context.setTheme(R.style.AppThemeDialog_blue_grey);
                break;

            case "black":
                context.setTheme(R.style.AppThemeDialog_black);
                break;

            default:
                context.setTheme(R.style.AppThemeDialog_light_green); //light_green
                break;
        }
    }
}

