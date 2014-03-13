package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WinActivity extends Activity {

    public static int COLOR = 0;

    Button btnExit;
    Button btnRetry;

    LinearLayout mLayout;

    MediaPlayer mediaPlayer;

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
        tvWin.setText(PrefsManager.getFieldSize() + "X" + PrefsManager.getFieldSize() + "\n" + getResources().getString(R.string.win) + " " + moves);
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        COLOR = PrefsManager.getMainColor();
        mLayout.setBackgroundColor(COLOR);
        btnExit.setBackgroundColor(COLOR);
        btnRetry.setBackgroundColor(COLOR);

        if (PrefsManager.isPlayingSounds()) {
            mediaPlayer = MediaPlayer.create(this, R.raw.win);
            mediaPlayer.start();
        }
    }

    public void exit() {
        Intent intent = new Intent(WinActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
