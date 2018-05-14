package com.example.mathi.expense;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by MATHI on 2/2/2018.
 */

public class Utils {

    public static void storeNetworkMode(String mode, Activity activity){
        String NETWORK_MODE_PREFS_NAME = "network_details";
        SharedPreferences settings= activity.getSharedPreferences(NETWORK_MODE_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("network_mode", mode);
        editor.apply();
    }

    public static String getNetworkMode(Activity activity) {
        String NETWORK_MODE_PREFS_NAME = "network_details";
        SharedPreferences settings = activity.getSharedPreferences(NETWORK_MODE_PREFS_NAME, MODE_PRIVATE);
        return settings.getString("network_mode", "en");
    }
    public static void storeSelectedIcon(Activity activity, String selected_icon,String category){
        String SELECTED_ICON = "selected_icon";
        SharedPreferences settings= activity.getSharedPreferences(SELECTED_ICON, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("selected_icon"+category, selected_icon);
        editor.apply();
    }
    public static String getSelectedIcon(Activity activity,String category){
        String SELECTED_ICON = "selected_icon";
        SharedPreferences settings = activity.getSharedPreferences(SELECTED_ICON, MODE_PRIVATE);
        return settings.getString("selected_icon"+category,"");
    }
    public static void removeSelectedIcon(Activity activity, String category){
        String SELECTED_ICON = "selected_icon";
        SharedPreferences settings = activity.getSharedPreferences(SELECTED_ICON, MODE_PRIVATE);
        settings.edit().remove("selected_icon"+category).commit();
    }
}
