package org.imaginativeworld.shadhinovidhan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Locale;

public class ColorPickerActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    Button[] colorBtn = new Button[21];
    Button btnOK;

    String color, colorStr;
    int __id = 0;
    int __margin;

    LinearLayout.LayoutParams layoutParams;
    LinearLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Change Language
         */
        String Lang = sharedPref.getString(preference_activity.pref_language, "bn");

        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale locale = new Locale(Lang);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        //============================================================

        setContentView(R.layout.activity_color_picker);

        mContext = ColorPickerActivity.this;

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        btnOK = (Button) findViewById(R.id.btnOk);
        btnOK.setOnClickListener(ColorPickerActivity.this);
//        colorPreview = findViewById(R.id.colorPreview);


        // Get Display size in pixel
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int __width = size.x - (dpToPx(48) * 2 + 10 * 2);
        __margin = dpToPx(2);

        layoutParams = new LinearLayout.LayoutParams(__width / 5, __width / 5);
        layoutParams.setMargins(__margin, __margin, __margin, __margin);

        Class cls = R.id.class;

        for (int i = 1; i <= 20; i++) {
            colorBtn[i] = (Button) findViewById(getResId("color" + String.valueOf(i), cls));
            colorBtn[i].setOnClickListener(ColorPickerActivity.this);
            colorBtn[i].setLayoutParams(layoutParams);
        }

        // Preview Setting
//        LinearLayout.LayoutParams lpColorPreview = new LinearLayout.LayoutParams(__width + 10 * 2, __width / 2);
//        lpColorPreview.setMargins(__margin, __margin, __margin, __margin);

//        colorPreview.setLayoutParams(lpColorPreview);

        /**
         * Get Intent Extras
         */
        Intent intent = getIntent();
        colorStr = intent.getStringExtra(getString(R.string.ColorName));
        if (colorStr == null) {
            colorStr = "light_green";
        }
        color = getColorCodeAsString(colorStr);

        /**
         * Set defaults
         */
        setButton(getColorButtonNumber(colorStr));


    }

    String getColorCodeAsString(String colorName) {
        switch (colorName) {
            case "red":
                return "#f44336";
            case "pink":
                return "#e91e63";
            case "purple":
                return "#9c27b0";
            case "deep_purple":
                return "#673ab7";
            case "indigo":
                return "#3f51b5";
            case "blue":
                return "#2196f3";
            case "light_blue":
                return "#03a9f4";
            case "cyan":
                return "#00bcd4";
            case "teal":
                return "#009688";
            case "green":
                return "#4caf50";
            case "light_green":
                return "#8bc34a";
            case "lime":
                return "#cddc39";
            case "yellow":
                return "#ffeb3b";
            case "amber":
                return "#ffc107";
            case "orange":
                return "#ff9800";
            case "deep_orange":
                return "#ff5722";
            case "brown":
                return "#795548";
            case "grey":
                return "#9e9e9e";
            case "blue_grey":
                return "#607d8b";
            case "black":
                return "#000000";
        }

        return "#ffffff";
    }

    int getColorButtonNumber(String colorName) {
        switch (colorName) {
            case "red":
                return 1;
            case "pink":
                return 2;
            case "purple":
                return 3;
            case "deep_purple":
                return 4;
            case "indigo":
                return 5;
            case "blue":
                return 6;
            case "light_blue":
                return 7;
            case "cyan":
                return 8;
            case "teal":
                return 9;
            case "green":
                return 10;
            case "light_green":
                return 11;
            case "lime":
                return 12;
            case "yellow":
                return 13;
            case "amber":
                return 14;
            case "orange":
                return 15;
            case "deep_orange":
                return 16;
            case "brown":
                return 17;
            case "grey":
                return 18;
            case "blue_grey":
                return 19;
            case "black":
                return 20;
        }

        return 11;  // light_green
    }

    void setButton(int _id) {

        if (__id != 0) {

            colorBtn[__id].setText("");

        }

        __id = _id;

        colorBtn[__id].setText("✔");

//        colorPreview.setBackgroundColor(Color.parseColor(color));
//        ViewCompat.setBackgroundTintList(colorPreview, ColorStateList.valueOf(Color.parseColor(color)));

//        colorPreview.setText(colorStr.replace('_', ' '));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color1:

                color = "#f44336"; //Red
                colorStr = "red";
                setButton(1);

                break;
            case R.id.color2:
                color = "#e91e63"; //Pink
                colorStr = "pink";
                setButton(2);

                break;
            case R.id.color3:
                color = "#9c27b0"; //Purple
                colorStr = "purple";
                setButton(3);

                break;
            case R.id.color4:
                color = "#673ab7"; //Deep Purple
                colorStr = "deep_purple";
                setButton(4);

                break;
            case R.id.color5:
                color = "#3f51b5"; //Indigo
                colorStr = "indigo";
                setButton(5);

                break;
            case R.id.color6:
                color = "#2196f3"; //Blue
                colorStr = "blue";
                setButton(6);

                break;
            case R.id.color7:
                color = "#03a9f4"; //Light Blue
                colorStr = "light_blue";
                setButton(7);

                break;
            case R.id.color8:
                color = "#00bcd4"; //Cyan
                colorStr = "cyan";
                setButton(8);

                break;
            case R.id.color9:
                color = "#009688"; //Teal
                colorStr = "teal";
                setButton(9);

                break;
            case R.id.color10:
                color = "#4caf50"; //Green
                colorStr = "green";
                setButton(10);

                break;
            case R.id.color11:
                color = "#8bc34a"; //Light Green
                colorStr = "light_green";
                setButton(11);

                break;
            case R.id.color12:
                color = "#cddc39"; //Lime
                colorStr = "lime";
                setButton(12);

                break;
            case R.id.color13:
                color = "#ffeb3b"; //Yellow
                colorStr = "yellow";
                setButton(13);

                break;
            case R.id.color14:
                color = "#ffc107"; //Amber
                colorStr = "amber";
                setButton(14);

                break;
            case R.id.color15:
                color = "#ff9800"; //Orange
                colorStr = "orange";
                setButton(15);

                break;
            case R.id.color16:
                color = "#ff5722"; //Deep Orange
                colorStr = "deep_orange";
                setButton(16);

                break;
            case R.id.color17:
                color = "#795548"; //Brown
                colorStr = "brown";
                setButton(17);

                break;
            case R.id.color18:
                color = "#9e9e9e"; //Grey
                colorStr = "grey";
                setButton(18);

                break;
            case R.id.color19:
                color = "#607d8b"; //Blue Grey
                colorStr = "blue_grey";
                setButton(19);

                break;
            case R.id.color20:
                color = "#000000"; //Black
                colorStr = "black";
                setButton(20);

                break;

            case R.id.btnOk:
                finishWithResult(color, colorStr);
                break;
        }

    }

    private void finishWithResult(String color, String colorStr) {
        Bundle conData = new Bundle();

        conData.putString("color", color);
        conData.putString("ColorName", colorStr);

        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
