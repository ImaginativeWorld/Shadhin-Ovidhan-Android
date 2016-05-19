/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * <p/>
 * Bangla (Bangladesh) Calendar
 * <p/>
 * Thinking & Coding:
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@yahoo.com
 * <p/>
 * Give me feedback and bug report:
 * imaginativeshohag@yahoo.com
 * <p/>
 * Version: 1.0
 * Last Update: 18 May 2015
 * <p/>
 * Bangla (Bangladesh) Calendar
 * <p/>
 * Thinking & Coding:
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@yahoo.com
 * <p/>
 * Give me feedback and bug report:
 * imaginativeshohag@yahoo.com
 * <p/>
 * Version: 1.0
 * Last Update: 18 May 2015
 */

/**
 * Bangla (Bangladesh) Calendar
 *
 * Thinking & Coding:
 *   Md. Mahmudul Hasan Shohag
 *   imaginativeshohag@yahoo.com
 *
 *   Give me feedback and bug report:
 *   imaginativeshohag@yahoo.com
 *
 *   Version: 1.0
 *   Last Update: 18 May 2015
 */
package org.imaginativeworld.shadhinovidhan;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class iw_bangla_calendar {

    String StrDay;
    String StrMonth;
    String StrYear;

    int LeapYear;

    int intMonth;

    GregorianCalendar calendar = new GregorianCalendar();

    public iw_bangla_calendar(GregorianCalendar cal) {

        calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        int dayOfTheYear;

        if (calendar.isLeapYear(calendar.get(Calendar.YEAR))) {
            LeapYear = 0;
        } else {
            LeapYear = 1;
        }

        dayOfTheYear = calendar.get(Calendar.DAY_OF_YEAR);

//        Log.d("shadhinovidhan", String.valueOf(calendar.get(Calendar.DATE)));
//        Log.d("shadhinovidhan", String.valueOf(calendar.get(Calendar.MONTH)));
//        Log.d("shadhinovidhan", String.valueOf(calendar.get(Calendar.YEAR)));
//        Log.d("shadhinovidhan", String.valueOf(calendar.get(Calendar.DAY_OF_YEAR)));
//        Log.d("shadhinovidhan", String.valueOf(calendar.isLeapYear(Calendar.YEAR)));

        /**
         * Processing the Month
         */
        if (isBetween(dayOfTheYear, 105 - LeapYear, 135 - LeapYear)) { //31
            StrMonth = "বৈশাখ";
            intMonth = 1;
        } else if (isBetween(dayOfTheYear, 136 - LeapYear, 166 - LeapYear)) { //31
            StrMonth = "জ্যৈষ্ঠ";
            intMonth = 2;
        } else if (isBetween(dayOfTheYear, 167 - LeapYear, 197 - LeapYear)) { //31
            StrMonth = "আষাঢ়";
            intMonth = 3;
        } else if (isBetween(dayOfTheYear, 198 - LeapYear, 228 - LeapYear)) { //31
            StrMonth = "শ্রাবণ";
            intMonth = 4;
        } else if (isBetween(dayOfTheYear, 229 - LeapYear, 259 - LeapYear)) { //31
            StrMonth = "ভাদ্র";
            intMonth = 5;
        } else if (isBetween(dayOfTheYear, 260 - LeapYear, 289 - LeapYear)) { //30
            StrMonth = "আশ্বিন";
            intMonth = 6;
        } else if (isBetween(dayOfTheYear, 290 - LeapYear, 319 - LeapYear)) { //30
            StrMonth = "কার্তিক";
            intMonth = 7;
        } else if (isBetween(dayOfTheYear, 320 - LeapYear, 349 - LeapYear)) { //30
            StrMonth = "অগ্রহায়ণ";
            intMonth = 8;
        } else if (isBetween(dayOfTheYear, 350 - LeapYear, 366 - LeapYear)) { //17
            StrMonth = "পৌষ";
            intMonth = 9;
        } else if (isBetween(dayOfTheYear, 1, 13)) { //13
            StrMonth = "পৌষ";
            intMonth = 9;
        } else if (isBetween(dayOfTheYear, 14, 43)) { //30
            StrMonth = "মাঘ";
            intMonth = 10;
        } else if (isBetween(dayOfTheYear, 44, 74 - LeapYear)) { //31 (30/31)
            StrMonth = "ফাল্গুন";
            intMonth = 11;
        } else if (isBetween(dayOfTheYear, 75 - LeapYear, 104 - LeapYear)) { //30
            StrMonth = "চৈত্র";
            intMonth = 12;
        }

        /**
         * Processing the Date
         */

        if (isBetween(dayOfTheYear, 1, 13)) {
            StrDay = String.valueOf(dayOfTheYear + 17);
        } else if (isBetween(dayOfTheYear, 14, 31)) {
            StrDay = String.valueOf(dayOfTheYear - 13);
        } else if (isBetween(dayOfTheYear, 32, 43)) {
            StrDay = String.valueOf(dayOfTheYear - 13);
        } else if (isBetween(dayOfTheYear, 44, 60 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - 43);
        } else if (isBetween(dayOfTheYear, 61 - LeapYear, 74 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - 43);
        } else if (isBetween(dayOfTheYear, 75 - LeapYear, 91 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (74 - LeapYear));
        } else if (isBetween(dayOfTheYear, 92 - LeapYear, 104 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (74 - LeapYear));
        } else if (isBetween(dayOfTheYear, 105 - LeapYear, 121 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (104 - LeapYear));
        } else if (isBetween(dayOfTheYear, 122 - LeapYear, 135 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (104 - LeapYear));
        } else if (isBetween(dayOfTheYear, 136 - LeapYear, 152 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (135 - LeapYear));
        } else if (isBetween(dayOfTheYear, 153 - LeapYear, 166 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (135 - LeapYear));
        } else if (isBetween(dayOfTheYear, 167 - LeapYear, 182 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (166 - LeapYear));
        } else if (isBetween(dayOfTheYear, 183 - LeapYear, 197 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (166 - LeapYear));
        } else if (isBetween(dayOfTheYear, 198 - LeapYear, 213 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (197 - LeapYear));
        } else if (isBetween(dayOfTheYear, 214 - LeapYear, 228 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (197 - LeapYear));
        } else if (isBetween(dayOfTheYear, 229 - LeapYear, 244 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (228 - LeapYear));
        } else if (isBetween(dayOfTheYear, 245 - LeapYear, 259 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (228 - LeapYear));
        } else if (isBetween(dayOfTheYear, 260 - LeapYear, 274 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (259 - LeapYear));
        } else if (isBetween(dayOfTheYear, 275 - LeapYear, 289 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (259 - LeapYear));
        } else if (isBetween(dayOfTheYear, 290 - LeapYear, 305 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (289 - LeapYear));
        } else if (isBetween(dayOfTheYear, 306 - LeapYear, 319 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (289 - LeapYear));
        } else if (isBetween(dayOfTheYear, 320 - LeapYear, 335 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (319 - LeapYear));
        } else if (isBetween(dayOfTheYear, 336 - LeapYear, 349 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (319 - LeapYear));
        } else if (isBetween(dayOfTheYear, 350 - LeapYear, 366 - LeapYear)) {
            StrDay = String.valueOf(dayOfTheYear - (349 - LeapYear));
        }

        /**
         * Processing the Year
         */

        int year = calendar.get(Calendar.YEAR);

        if (isBetween(dayOfTheYear, 1, 104 - LeapYear)) {
            StrYear = String.valueOf(year - 594);
        } else if (isBetween(dayOfTheYear, 105 - LeapYear, 365)) {
            StrYear = String.valueOf(year - 593);
        }

    }

    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public String getFullDate() {

        return convertToBanglaNumeric(StrDay) + " " + StrMonth + " " + convertToBanglaNumeric(StrYear);

    }

    public String getYear() {

        return convertToBanglaNumeric(StrYear);
    }

    public String getMonth() {

        return StrMonth;
    }

    public String getDate() {

        return convertToBanglaNumeric(StrDay);
    }

    public int getDateInt() {

        return Integer.parseInt(StrDay);
    }

    public int getDayOfTheWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getMonthNumber() {
        return intMonth;
    }

    public boolean isLeapYear() {
        return (LeapYear == 0);
    }

    String convertToBanglaNumeric(String string) {
        return string.replace("1", "১").replace("2", "২").replace("3", "৩")
                .replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭")
                .replace("8", "৮").replace("9", "৯").replace("0", "০");
    }
}
