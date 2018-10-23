/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class bn_calendar_activity extends AppCompatActivity implements View.OnClickListener {

    final int TOTAL_DAY_TEXT = 42;

    TextView bnMonth, bnYear;

    TextView[] day = new TextView[TOTAL_DAY_TEXT];
    TextView[] week = new TextView[7];

    Button btnExit;

    /**
     * SUN = 1 (1)
     * MON = 2 (2)
     * TUE = 3 (3)
     * WED = 4 (4)
     * THU = 5 (5)
     * FRI = 6 (6)
     * SAT = 7 (0)
     */
    String[] strWeek = {
            "শ", "র", "সো", "ম", "বু", "বৃ", "শু"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /**
         * Set Dialog Theme
         */
        String UI_theme = sharedPref.getString(preference_activity.pref_ui_theme, "light_green");
        so_tools.setDialogUItheme(UI_theme, bn_calendar_activity.this);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bn_calendar_layout);

        bnMonth = (TextView) findViewById(R.id.bn_month);
        bnYear = (TextView) findViewById(R.id.bn_year);

        btnExit = (Button) findViewById(R.id.btn_close);
        btnExit.setOnClickListener(bn_calendar_activity.this);

        day[0] = (TextView) findViewById(R.id.cal_day_01);
        day[1] = (TextView) findViewById(R.id.cal_day_02);
        day[2] = (TextView) findViewById(R.id.cal_day_03);
        day[3] = (TextView) findViewById(R.id.cal_day_04);
        day[4] = (TextView) findViewById(R.id.cal_day_05);
        day[5] = (TextView) findViewById(R.id.cal_day_06);
        day[6] = (TextView) findViewById(R.id.cal_day_07);
        day[7] = (TextView) findViewById(R.id.cal_day_08);
        day[8] = (TextView) findViewById(R.id.cal_day_09);
        day[9] = (TextView) findViewById(R.id.cal_day_10);
        day[10] = (TextView) findViewById(R.id.cal_day_11);
        day[11] = (TextView) findViewById(R.id.cal_day_12);
        day[12] = (TextView) findViewById(R.id.cal_day_13);
        day[13] = (TextView) findViewById(R.id.cal_day_14);
        day[14] = (TextView) findViewById(R.id.cal_day_15);
        day[15] = (TextView) findViewById(R.id.cal_day_16);
        day[16] = (TextView) findViewById(R.id.cal_day_17);
        day[17] = (TextView) findViewById(R.id.cal_day_18);
        day[18] = (TextView) findViewById(R.id.cal_day_19);
        day[19] = (TextView) findViewById(R.id.cal_day_20);
        day[20] = (TextView) findViewById(R.id.cal_day_21);
        day[21] = (TextView) findViewById(R.id.cal_day_22);
        day[22] = (TextView) findViewById(R.id.cal_day_23);
        day[23] = (TextView) findViewById(R.id.cal_day_24);
        day[24] = (TextView) findViewById(R.id.cal_day_25);
        day[25] = (TextView) findViewById(R.id.cal_day_26);
        day[26] = (TextView) findViewById(R.id.cal_day_27);
        day[27] = (TextView) findViewById(R.id.cal_day_28);
        day[28] = (TextView) findViewById(R.id.cal_day_29);
        day[29] = (TextView) findViewById(R.id.cal_day_30);
        day[30] = (TextView) findViewById(R.id.cal_day_31);
        day[31] = (TextView) findViewById(R.id.cal_day_32);
        day[32] = (TextView) findViewById(R.id.cal_day_33);
        day[33] = (TextView) findViewById(R.id.cal_day_34);
        day[34] = (TextView) findViewById(R.id.cal_day_35);
        day[35] = (TextView) findViewById(R.id.cal_day_36);
        day[36] = (TextView) findViewById(R.id.cal_day_37);
        day[37] = (TextView) findViewById(R.id.cal_day_38);
        day[38] = (TextView) findViewById(R.id.cal_day_39);
        day[39] = (TextView) findViewById(R.id.cal_day_40);
        day[40] = (TextView) findViewById(R.id.cal_day_41);
        day[41] = (TextView) findViewById(R.id.cal_day_42);

        week[0] = (TextView) findViewById(R.id.cal_week_01);
        week[1] = (TextView) findViewById(R.id.cal_week_02);
        week[2] = (TextView) findViewById(R.id.cal_week_03);
        week[3] = (TextView) findViewById(R.id.cal_week_04);
        week[4] = (TextView) findViewById(R.id.cal_week_05);
        week[5] = (TextView) findViewById(R.id.cal_week_06);
        week[6] = (TextView) findViewById(R.id.cal_week_07);

        generateBnCalendar();

    }

    public void generateBnCalendar() {

        iw_bangla_calendar bnCalendar =
                new iw_bangla_calendar((GregorianCalendar) GregorianCalendar.getInstance());

        bnMonth.setText(bnCalendar.getMonth());
        bnYear.setText(bnCalendar.getYear());

        int intMonth = bnCalendar.getMonthNumber();
        int total_day = 31;

        /**
         * Boishkh to Vadro: 31 Days
         * Ashin to Choitro: 30 Days
         * Falgun: 31 Days if Gregorian Calendar has leap year
         */
        if (intMonth >= 6 && intMonth <= 12) {
            if (!(bnCalendar.isLeapYear() && intMonth == 11)) {
                total_day = 30;
            }
        }

        /**
         * Get primaryColor attribute
         */
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        /**
         * Set week
         */
        int i, j, BnDate;
        for (i = 0; i < 7; i++) {
            week[i].setText(strWeek[i]);
        }

        /**
         * SUN = 1 (1)
         * MON = 2 (2)
         * TUE = 3 (3)
         * WED = 4 (4)
         * THU = 5 (5)
         * FRI = 6 (6)
         * SAT = 7 (0)
         */
        int dayOfWeek = bnCalendar.getDayOfTheWeek();
        if (dayOfWeek == Calendar.SATURDAY)
            dayOfWeek = 0;

        int startLoop = bnCalendar.getDateInt()-1;

        if (startLoop >= 7)
            startLoop %= 7;

        int startDayInWeek = dayOfWeek;
        while(startLoop!=0)
        {
            startDayInWeek--;
            startLoop--;
            if(startDayInWeek<0)
                startDayInWeek=6;
        }

        BnDate = bnCalendar.getDateInt();

        for (i = startDayInWeek, j = 1; j <= total_day; i++, j++) {
            day[i].setText(bnCalendar.convertToBanglaNumeric(String.valueOf(j)));
            if (j == BnDate) {
                day[i].setTextColor(color);
                day[i].setTypeface(null, Typeface.BOLD);
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_close:

                finish();


                break;
        }
    }
}
