package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.HashMap;

/**
 *
 * Created by Shohag on 15 Aug 15.
 *
 */
public class so_tools {

    static String removeSymbolFromText(String str) {
        return str
                .replace(" ", "")
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
                .replace(";", "")
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
    }

    public static void sendData(String info, String word, String pos, String meaning, String synonyms) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();

        //?arg1=val1&arg2=val2
        hashMap.put("info", info);
        hashMap.put("word", so_tools.removeSymbolFromText(word));
        hashMap.put("pron", word);
        hashMap.put("pos", pos);
        hashMap.put("meaning", meaning);
        hashMap.put("synonyms", synonyms);

        // Gets the URL from the UI's text field.
        ConnectivityManager connMgr = (ConnectivityManager)
                appContextHelper.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new sendDataToServer(hashMap, appContextHelper.getAppContext().getString(R.string.server_post_url));

            Toast t = Toast.makeText(appContextHelper.getAppContext(),
                    appContextHelper.getAppContext().getString(R.string.txt_entry_sent_to_server), Toast.LENGTH_SHORT);
            t.show();
        }

    }

}

