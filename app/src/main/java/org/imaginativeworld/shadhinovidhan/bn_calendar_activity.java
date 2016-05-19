/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.imaginativeworld.shadhinovidhan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class bn_calendar_activity extends Activity implements View.OnClickListener {

    TextView bnMonth, bnYear;

    TextView[] day = new TextView[31];
    TextView[] week = new TextView[7];

    Button btnExit;

    String[] strWeek = {  //Sunday: 1st day of week
            "র", "সো", "ম", "বু", "বৃ", "শু", "শ"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn_calendar_layout);

        //============
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


//        for(int i=0;i<31;i++)
//        {
//            day[i].setOnClickListener(bn_calendar_activity.this);
//        }

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

        //Boishkh to Vadro: 31 Days
        //Ashin to Choitro: 30 Days
        //Falgun: 31 Days if Gregorian Calendar has leap year
        if (intMonth >= 6 && intMonth <= 12) {
            if (!(bnCalendar.isLeapYear() && intMonth == 11)) {
                day[30].setVisibility(View.INVISIBLE);
            }
        }

        day[bnCalendar.getDateInt() - 1].setTextColor(getResources().getColor(R.color.red_700));

        int dayOfWeek = bnCalendar.getDayOfTheWeek();

        int startLoop = bnCalendar.getDateInt();
        if (startLoop > 7)
            startLoop %= 7;

        for (int i = 0; i < 7; i++) {
            week[startLoop - 1].setText(strWeek[dayOfWeek - 1]);
            startLoop++;
            dayOfWeek++;
            if (startLoop > 7)
                startLoop = 1;
            if (dayOfWeek > 7)
                dayOfWeek = 1;
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
