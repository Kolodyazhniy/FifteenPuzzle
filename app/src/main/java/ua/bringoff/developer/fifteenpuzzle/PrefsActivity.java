package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class PrefsActivity extends Activity {

    public static final String APP_PREFERENCES = "fifteen_puzzle_settings";
    public static final String KEY_RADIOBUTTON_INDEX = "SIZE_RADIO_BUTTON_INDEX";
    public static final String KEY_CHECKBOX_SOUND_CHECKED = "KEY_CHECKBOX_SOUND_CHECKED";

    RadioGroup mPuzzleSizeRadioGroup;
    RadioButton m3x3, m4x4, m5x5, m6x6;
    RadioButton[] mSizeRadioButtons = {m3x3, m4x4, m5x5, m6x6};
    CheckBox mSoundsCheckBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        mPuzzleSizeRadioGroup = (RadioGroup) findViewById(R.id.puzzle_size_radiogroup);
        RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton) mPuzzleSizeRadioGroup
                        .findViewById(checkedId);
                int checkedIndex = mPuzzleSizeRadioGroup.indexOfChild(checkedRadioButton);

                savePreferencesSize(KEY_RADIOBUTTON_INDEX, checkedIndex);
            }
        };
        mPuzzleSizeRadioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        mSoundsCheckBox = (CheckBox) findViewById(R.id.sounds_checkbox);
        mSoundsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                savePreferencesSound(KEY_CHECKBOX_SOUND_CHECKED, b);
            }
        });
        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        TextView sizeTextView = (TextView) findViewById(R.id.puzzle_size_textview);
        sizeTextView.setTypeface(fontFace);

        int count = 0x0;

        for (int i = 0; i < mSizeRadioButtons.length; i++) {
            mSizeRadioButtons[i] = (RadioButton) findViewById(R.id.rb3x3 + count);
            mSizeRadioButtons[i].setTypeface(fontFace);
            count += 0x1;
        }

        mSoundsCheckBox = (CheckBox) findViewById(R.id.sounds_checkbox);
        mSoundsCheckBox.setTypeface(fontFace);

        loadPreferencesSize();
        loadPreferencesSound();
    }

    private void savePreferencesSize(String keySize, int valueSize) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(keySize, valueSize);
        editor.apply();
    }

    private void savePreferencesSound(String keySound, boolean valueSound) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keySound, valueSound);
        editor.apply();
    }


    private void loadPreferencesSize() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        int sizeRadioIndex = sharedPreferences.getInt(
                KEY_RADIOBUTTON_INDEX, 1);
        RadioButton sizeCheckedRadioButton = (RadioButton) mPuzzleSizeRadioGroup
                .getChildAt(sizeRadioIndex);
        sizeCheckedRadioButton.setChecked(true);
    }

    private void loadPreferencesSound() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        boolean sizeRadioIndex = sharedPreferences.getBoolean(
                KEY_CHECKBOX_SOUND_CHECKED, true);

        mSoundsCheckBox.setChecked(sizeRadioIndex);
    }
}
