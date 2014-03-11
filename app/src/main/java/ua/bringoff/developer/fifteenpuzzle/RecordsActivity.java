package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


public class RecordsActivity extends Activity {
    public static int COLOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        SharedPreferences sharedPreferences = getSharedPreferences(PrefsActivity.APP_PREFERENCES, MODE_PRIVATE);
        COLOR = sharedPreferences.getInt(PrefsActivity.KEY_GAME_COLOR, getResources().getColor(R.color.orange_cool));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.about_layout);
        layout.setBackgroundColor(COLOR);
    }

}
