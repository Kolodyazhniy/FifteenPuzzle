package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class PrefsActivity extends Activity {
    RadioGroup mPuzzleSizeRadioGroup;
    RadioButton m3x3, m4x4, m5x5, m6x6;
    RadioButton[] mSizeRadioButtons = {m3x3, m4x4, m5x5, m6x6};
    CheckBox mSoundsCheckBox;
    LinearLayout mPrefsLayout;
    Button mBadOrangeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        mPrefsLayout = (LinearLayout) findViewById(R.id.main_prefs_layout);
        mPrefsLayout.setBackgroundColor(PrefsManager.getMainColor());

        mPuzzleSizeRadioGroup = (RadioGroup) findViewById(R.id.puzzle_size_radiogroup);
        mPuzzleSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) mPuzzleSizeRadioGroup
                        .findViewById(checkedId);
                int checkedIndex = mPuzzleSizeRadioGroup.indexOfChild(checkedRadioButton);
                PrefsManager.setFieldSize(checkedIndex + 3);
                PrefsManager.savePrefs();
            }
        });

        mSoundsCheckBox = (CheckBox) findViewById(R.id.sounds_checkbox);
        mSoundsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PrefsManager.setPlayingSounds(b);
                PrefsManager.savePrefs();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPuzzleSizeRadioGroup.check(mSizeRadioButtons[0].getId() + PrefsManager.getFieldSize() - 3);
        mSoundsCheckBox.setChecked(PrefsManager.isPlayingSounds());
        mBadOrangeButton = (Button) findViewById(R.id.button_orange);
        mBadOrangeButton.setBackgroundColor(getResources().getColor(R.color.orange_cool));
    }

    public void chooseColor(View view) {
        Button button  = (Button) findViewById(view.getId());
        ColorDrawable color = (ColorDrawable) button.getBackground();

        PrefsManager.setMainColor(color.getColor());
        mPrefsLayout.setBackgroundColor(PrefsManager.getMainColor());
        PrefsManager.savePrefs();
    }

}
