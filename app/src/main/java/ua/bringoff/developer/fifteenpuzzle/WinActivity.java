package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WinActivity extends Activity {

    public static int COLOR = 0;

    Button btnExit;
    Button btnRetry;

    LinearLayout mLayout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        exit();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        mLayout = (LinearLayout) findViewById(R.id.win_layout);
        mLayout.setBackgroundColor(COLOR);

        int moves = getIntent().getIntExtra(GameBoardActivity.MOVES_EXTRA, 0);

        TextView tvWin = (TextView) findViewById(R.id.win_text);
        tvWin.setText(getResources().getString(R.string.win) + " " + moves);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");
        tvWin.setTypeface(typeface);

        btnExit = (Button) findViewById(R.id.exit_button);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        btnRetry = (Button) findViewById(R.id.retry_button);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, GameBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(PrefsActivity.APP_PREFERENCES, MODE_PRIVATE);
        int color = sharedPreferences.getInt(PrefsActivity.KEY_GAME_COLOR, getResources().getColor(R.color.orange_cool));
        if (COLOR != color) {
            COLOR = color;
        }
        mLayout.setBackgroundColor(COLOR);
        btnExit.setBackgroundColor(COLOR);
        btnRetry.setBackgroundColor(COLOR);
    }

    public void exit() {
        Intent intent = new Intent(WinActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

}
