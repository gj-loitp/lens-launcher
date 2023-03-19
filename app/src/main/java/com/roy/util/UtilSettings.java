package com.roy.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

public class UtilSettings {

    public static final float DEFAULT_ICON_SIZE = 18.0f;
    public static final float DEFAULT_DISTORTION_FACTOR = 2.5f;
    public static final float DEFAULT_SCALE_FACTOR = 1.0f;
    public static final long DEFAULT_ANIMATION_TIME = 200;
    public static final boolean DEFAULT_VIBRATE_APP_HOVER = false;
    public static final boolean DEFAULT_VIBRATE_APP_LAUNCH = true;
    public static final boolean DEFAULT_SHOW_NAME_APP_HOVER = true;
    public static final boolean DEFAULT_SHOW_TOUCH_SELECTION = false;
    public static final boolean DEFAULT_SHOW_NEW_APP_TAG = true;
    public static final String DEFAULT_BACKGROUND = "Wallpaper";
    public static final String DEFAULT_BACKGROUND_COLOR = "#FFF8BBD0";
    public static final String DEFAULT_HIGHLIGHT_COLOR = "#FFF50057";
    public static final String DEFAULT_ICON_PACK_LABEL_NAME = "Default Icon Pack";
    public static final int DEFAULT_SORT_TYPE = 0;
    public static final int DEFAULT_NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO;

    // These values are for the progress bars, their real values = (MAX_VALUE / INTERVAL (eg. 2)) + MIN_VALUE
    public static final int MAX_ICON_SIZE = 45;
    public static final int MAX_DISTORTION_FACTOR = 9;
    public static final int MAX_SCALE_FACTOR = 5;
    public static final int MAX_ANIMATION_TIME = 600;

    public static final int SHOW_NEW_APP_TAG_DURATION = 12 * 60 * 60 * 1000; /* An app has the new tag for twelve hours. If openCount >= 1, the new tag is not drawn. */

    public static final float MIN_ICON_SIZE = 10.0f;
    public static final float MIN_DISTORTION_FACTOR = 0.5f;
    public static final float MIN_SCALE_FACTOR = 1.0f;
    public static final long MIN_ANIMATION_TIME = 100;

    public static final float DEFAULT_FLOAT = Float.MIN_VALUE;
    public static final long DEFAULT_LONG = Long.MIN_VALUE;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final String DEFAULT_STRING = "";

    public static final String KEY_ICON_SIZE = "min_icon_size";
    public static final String KEY_DISTORTION_FACTOR = "distortion_factor";
    public static final String KEY_SCALE_FACTOR = "scale_factor";
    public static final String KEY_ANIMATION_TIME = "animation_time";
    public static final String KEY_VIBRATE_APP_HOVER = "vibrate_app_hover";
    public static final String KEY_VIBRATE_APP_LAUNCH = "vibrate_app_launch";
    public static final String KEY_SHOW_NAME_APP_HOVER = "show_name_app_hover";
    public static final String KEY_SHOW_TOUCH_SELECTION = "show_touch_selection";
    public static final String KEY_SHOW_NEW_APP_TAG = "show_new_tag_app";
    public static final String KEY_BACKGROUND = "background";
    public static final String KEY_BACKGROUND_COLOR = "background_color";
    public static final String KEY_HIGHLIGHT_COLOR = "show_touch_selection_color";
    public static final String KEY_ICON_PACK_LABEL_NAME = "icon_pack_label_name";
    public static final String KEY_SORT_TYPE = "sort_type";
    public static final String KEY_NIGHT_MODE = "night_mode";

    private final Context mContext;
    private SharedPreferences mPrefs;

    public UtilSettings(Context context) {
        mContext = context;
    }

    private SharedPreferences sharedPreferences() {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mPrefs;
    }

    public void save(String name, int value) {
//        sharedPreferences().edit().putInt(name, value).commit();
        sharedPreferences().edit().putInt(name, value).apply();
    }

    public @AppCompatDelegate.NightMode int getNightMode() {
        switch (sharedPreferences().getInt(KEY_NIGHT_MODE, DEFAULT_NIGHT_MODE)) {
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                return AppCompatDelegate.MODE_NIGHT_AUTO;
            case AppCompatDelegate.MODE_NIGHT_NO:
                return AppCompatDelegate.MODE_NIGHT_NO;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }

    public void save(String name, float value) {
//        sharedPreferences().edit().putFloat(name, value).commit();
        sharedPreferences().edit().putFloat(name, value).apply();
    }

    public float getFloat(String name) {
        switch (name) {
            case KEY_ICON_SIZE:
                if (sharedPreferences().getFloat(name, DEFAULT_ICON_SIZE) < MIN_ICON_SIZE) {
                    save(name, MIN_ICON_SIZE);
                } else if (sharedPreferences().getFloat(name, DEFAULT_ICON_SIZE) > getMaxFloatValue(name)) {
                    save(name, getMaxFloatValue(name));
                }
                return sharedPreferences().getFloat(name, DEFAULT_ICON_SIZE);
            case KEY_DISTORTION_FACTOR:
                if (sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR) < MIN_DISTORTION_FACTOR) {
                    save(name, MIN_DISTORTION_FACTOR);
                } else if (sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR) > getMaxFloatValue(name)) {
                    save(name, getMaxFloatValue(name));
                }
                return sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR);
            case KEY_SCALE_FACTOR:
                if (sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR) < MIN_SCALE_FACTOR) {
                    save(name, MIN_SCALE_FACTOR);
                } else if (sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR) > getMaxFloatValue(name)) {
                    save(name, getMaxFloatValue(name));
                }
                return sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR);
            default:
                return sharedPreferences().getFloat(name, DEFAULT_FLOAT);
        }
    }

    public void save(String name, long value) {
//        sharedPreferences().edit().putLong(name, value).commit();
        sharedPreferences().edit().putLong(name, value).apply();
    }

    public long getLong(String name) {
        if (name.equals(KEY_ANIMATION_TIME)) {
            if (sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME) < MIN_ANIMATION_TIME) {
                save(name, MIN_ANIMATION_TIME);
            } else if (sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME) > getMaxLongValue(name)) {
                save(name, getMaxLongValue(name));
            }
            return sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME);
        } else {
            return sharedPreferences().getLong(name, DEFAULT_LONG);
        }
    }

    public void save(String name, String value) {
//        sharedPreferences().edit().putString(name, value).commit();
        sharedPreferences().edit().putString(name, value).apply();
    }

    public String getString(String name) {
        switch (name) {
            case KEY_BACKGROUND:
                return sharedPreferences().getString(name, DEFAULT_BACKGROUND);
            case KEY_BACKGROUND_COLOR:
                return sharedPreferences().getString(name, DEFAULT_BACKGROUND_COLOR);
            case KEY_HIGHLIGHT_COLOR:
                return sharedPreferences().getString(name, DEFAULT_HIGHLIGHT_COLOR);
            case KEY_ICON_PACK_LABEL_NAME:
                return sharedPreferences().getString(name, DEFAULT_ICON_PACK_LABEL_NAME);
            default:
                return sharedPreferences().getString(name, DEFAULT_STRING);
        }
    }


    public void save(String name, boolean value) {
//        sharedPreferences().edit().putBoolean(name, value).commit();
        sharedPreferences().edit().putBoolean(name, value).apply();
    }

    public boolean getBoolean(String name) {
        switch (name) {
            case KEY_VIBRATE_APP_HOVER:
                return sharedPreferences().getBoolean(name, DEFAULT_VIBRATE_APP_HOVER);
            case KEY_VIBRATE_APP_LAUNCH:
                return sharedPreferences().getBoolean(name, DEFAULT_VIBRATE_APP_LAUNCH);
            case KEY_SHOW_NAME_APP_HOVER:
                return sharedPreferences().getBoolean(name, DEFAULT_SHOW_NAME_APP_HOVER);
            case KEY_SHOW_TOUCH_SELECTION:
                return sharedPreferences().getBoolean(name, DEFAULT_SHOW_TOUCH_SELECTION);
            case KEY_SHOW_NEW_APP_TAG:
                return sharedPreferences().getBoolean(name, DEFAULT_SHOW_NEW_APP_TAG);
            default:
                return sharedPreferences().getBoolean(name, DEFAULT_BOOLEAN);
        }
    }

    public void save(UtilAppSorter.SortType value) {
        save(KEY_SORT_TYPE, value.ordinal());
    }

    public UtilAppSorter.SortType getSortType() {
        int ordinal = sharedPreferences().getInt(KEY_SORT_TYPE, DEFAULT_SORT_TYPE);
        return UtilAppSorter.SortType.values()[ordinal];
    }

    public float getMaxFloatValue(String name) {
        switch (name) {
            case KEY_ICON_SIZE:
                return (float) MAX_ICON_SIZE + MIN_ICON_SIZE;
            case KEY_DISTORTION_FACTOR:
                return (float) MAX_DISTORTION_FACTOR / 2 + MIN_DISTORTION_FACTOR;
            case KEY_SCALE_FACTOR:
                return (float) MAX_SCALE_FACTOR / 2 + MIN_SCALE_FACTOR;
            default:
                return DEFAULT_FLOAT;
        }
    }

    public long getMaxLongValue(String name) {
        if (name.equals(KEY_ANIMATION_TIME)) {
            return (long) MAX_ANIMATION_TIME / 2 + MIN_ANIMATION_TIME;
        } else {
            return DEFAULT_LONG;
        }
    }
}