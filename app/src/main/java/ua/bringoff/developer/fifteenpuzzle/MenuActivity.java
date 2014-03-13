package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuActivity extends Activity {
    Button btnNewGame;
    Button btnPreferences;
    Button btnAbout;

    LinearLayout layout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        PrefsManager.setContext(getApplicationContext());

        layout = (LinearLayout) findViewById(R.id.menu_layout);

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");

        btnNewGame = (Button) findViewById(R.id.button_new_game);
        btnNewGame.setWidth((int) Math.round(screenSize.x / 1.5));
        btnNewGame.setTypeface(fontFace);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnPreferences = (Button) findViewById(R.id.button_preferences);
        btnPreferences.setWidth((int) Math.round(screenSize.x / 1.5));
        btnPreferences.setTypeface(fontFace);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PrefsActivity.class);
                startActivity(intent);
            }
        });

        btnAbout = (Button) findViewById(R.id.button_about);
        btnAbout.setWidth((int) Math.round(screenSize.x / 1.5));
        btnAbout.setTypeface(fontFace);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        layout.setBackgroundColor(PrefsManager.getMainColor());
        btnNewGame.setBackgroundColor(PrefsManager.getMainColor());
        btnPreferences.setBackgroundColor(PrefsManager.getMainColor());
        btnAbout.setBackgroundColor(PrefsManager.getMainColor());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);

        btnNewGame.startAnimation(animation);
        btnPreferences.startAnimation(animation);
        btnAbout.startAnimation(animation);
    }
}
