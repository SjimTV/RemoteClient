package com.sjimtv.remoteclient.utils;

public class TimeConverter {
    public static String doubleToHourMinSec(double duration){
        int hours = (int) duration / 3600;
        int remainder = (int) duration - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;


        if (hours == 0) return addZeroIfNeeded(mins) + ":" + addZeroIfNeeded(secs);
        else return addZeroIfNeeded(hours) + ":" + addZeroIfNeeded(mins) + ":" + addZeroIfNeeded(secs);

    }

    private static String addZeroIfNeeded(int input){
        if (input >= 10) return Integer.toString(input);
        else if (input >= 0) return  "0" + input;
        else return "00";
    }
}
