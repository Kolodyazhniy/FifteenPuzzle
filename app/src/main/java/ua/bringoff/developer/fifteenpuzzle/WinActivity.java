package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinActivity extends Activity {

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

        int moves = getIntent().getIntExtra(GameBoardActivity.MOVES_EXTRA, 0);

        TextView tvWin = (TextView) findViewById(R.id.win_text);
        tvWin.setText(getResources().getString(R.string.win) + " " + moves);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");
        tvWin.setTypeface(typeface);

        Button btnExit = (Button) findViewById(R.id.exit_button);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
        Button btnRetry = (Button) findViewById(R.id.retry_button);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, GameBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void exit() {
        Intent intent = new Intent(WinActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

}
