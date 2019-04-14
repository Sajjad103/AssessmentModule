package com.test.sajjad.assessmentandroid.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String API_ID = "8b406537bc28ce0f26065be8882f18d8";
    public static Location current_location = null;

    public static final String API_KEY = "AIzaSyA2H-Bp_bG3PaiUPfrXfHmokbyc58m1tjA";//"AIzaSyAKBVnPyXNsz-ttcAaCvKeaqmZy4wLL5zw";

    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";



    public static String convertUnixToDate(int dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE:dd:yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }
    public static String convertUnixToDay(int dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String formatted = sdf.format(date);
        return formatted;
    }
}
