package ua.bringoff.developer.fifteenpuzzle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by Bringoff on 12.03.14.
 */
public class PrefsManager {
    public static final String APP_PREFERENCES = "fifteen_puzzle_settings";

    public static final String KEY_FIELD_SIZE = "FIELD_SIZE";
    public static final String KEY_SOUNDS = "PLAYING_SOUNDS";
    public static final String KEY_MAIN_COLOR = "GAME_COLOR";

    private static int mFieldSize;
    private static boolean mPlayingSounds;
    private static int mMainColor;

    private static SharedPreferences mSharedPreferences;

    private static Context mContext;

    public static void setContext(Context context) {
        PrefsManager.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFERENCES, mContext.MODE_PRIVATE);
        loadPrefs();
    }

    public static int getFieldSize() {
        return mFieldSize;
    }

    public static void setFieldSize(int fieldSize) {
        mFieldSize = fieldSize;
    }

    public static boolean isPlayingSounds() {
        return mPlayingSounds;
    }

    public static void setPlayingSounds(boolean playingSounds) {
        mPlayingSounds = playingSounds;
    }

    public static int getMainColor() {
        return mMainColor;
    }

    public static void setMainColor(int mainColor) {
        mMainColor = mainColor;
    }

    private static void loadPrefs() {
        setFieldSize(mSharedPreferences.getInt(KEY_FIELD_SIZE, 4));
        setPlayingSounds(mSharedPreferences.getBoolean(KEY_SOUNDS, true));
        setMainColor(mSharedPreferences.getInt(KEY_MAIN_COLOR, mContext.getResources().getColor(R.color.orange_cool)));
        //savePrefs();
    }
    public static void savePrefs() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_FIELD_SIZE, getFieldSize());
        editor.putBoolean(KEY_SOUNDS, isPlayingSounds());
        editor.putInt(KEY_MAIN_COLOR, getMainColor());
        editor.commit();
    }
}
