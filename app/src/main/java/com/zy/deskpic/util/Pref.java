package com.zy.deskpic.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zy.deskpic.ZyApplication;

public class Pref {
    public Context context;
    private volatile static Pref pref;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public synchronized static Pref getInstance() {
        if (pref == null) {
            synchronized (Pref.class) {
                if (pref == null) {
                    init();
                }
            }
        }
        return pref;
    }

    public static void init() {
        pref = new Pref();
        pref.context = ZyApplication.getInstance();
        pref.prefs = pref.context.getSharedPreferences(pref.context.getPackageName() + "_preference", Context.MODE_PRIVATE);
        pref.editor = pref.prefs.edit();
    }

    public void putString(String key, String value) {
        try {
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String defaultVal) {
        if (prefs == null || context == null) {
            return "";
        }
        return prefs.getString(key, defaultVal);
    }

    public void putInt(String key, int value) {
        try {
            editor.putInt(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getInt(String key, int defaultVal) {
        if (prefs == null || context == null) {
            return 0;
        }
        return prefs.getInt(key, defaultVal);
    }

}
