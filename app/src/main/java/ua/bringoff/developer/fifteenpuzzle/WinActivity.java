package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class WinActivity extends Activity {

    public static int COLOR = 0;

    Button btnExit;
    Button btnRetry;
    Button btnShare;

    TextView tvWin;

    LinearLayout mLayout;
    LinearLayout llResult;

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
        llResult = (LinearLayout) findViewById(R.id.result_layout);

        int moves = getIntent().getIntExtra(GameBoardActivity.MOVES_EXTRA, 0);

        tvWin = (TextView) findViewById(R.id.win_text);
        tvWin.setText(PrefsManager.getFieldSize() + "X" + PrefsManager.getFieldSize() + "\n" + getResources().getString(R.string.win) + " " + moves);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");
        tvWin.setTypeface(typeface);

        btnShare = (Button) findViewById(R.id.share_button);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap imageResult = Bitmap.createBitmap(llResult.getWidth(), llResult.getHeight(), Bitmap.Config.ARGB_8888);
                llResult.draw(new Canvas(imageResult));

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/png");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageResult.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                File file = null;
                try {
                    file = File.createTempFile("sharedImage", ".png", getExternalCacheDir());
                    file.createNewFile();
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });

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
        if (mediaPlayer != null) {
           if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        COLOR = PrefsManager.getMainColor();
        mLayout.setBackgroundColor(COLOR);
        btnExit.setBackgroundColor(COLOR);
        btnRetry.setBackgroundColor(COLOR);
        llResult.setBackgroundColor(COLOR);

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
