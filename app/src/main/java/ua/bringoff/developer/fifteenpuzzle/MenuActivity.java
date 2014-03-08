package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/DROID.TTF");

        Button btnNewGame = (Button) findViewById(R.id.button_new_game);
        btnNewGame.setWidth((int)Math.round(screenSize.x / 1.5));
        btnNewGame.setTypeface(fontFace);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GameBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnAbout = (Button) findViewById(R.id.button_about);
        btnAbout.setWidth((int)Math.round(screenSize.x / 1.5));
        btnAbout.setTypeface(fontFace);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }


}
