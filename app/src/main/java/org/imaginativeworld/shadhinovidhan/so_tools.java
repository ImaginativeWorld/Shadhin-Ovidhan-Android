package org.imaginativeworld.shadhinovidhan;

/**
 * Created by Shohag on 15 Aug 15.
 */
public class so_tools {

    static String removeSymbolFromText(String str) {
        return str
                .replace(" ", "")
                .replace("_", "")
                .replace(".", "")
                .replace("'", "")
                .replace("[", "")
                .replace("]", "")
                .replace(";", "")
                .replace(",", "")
                .replace("?", "")
                .replace("!", "")
                .replace("\\", "")
                .replace("/", "")
                .replace("&", "")
                .replace("*", "")
                .replace("#", "")
                .replace("%", "");
    }


}
