package ua.bringoff.developer.fifteenpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvAbout = (TextView) findViewById(R.id.about_text_view);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        tvAbout.setTypeface(typeface);

        TextView tvEmail = (TextView) findViewById(R.id.email_textview);
        tvEmail.setTypeface(typeface);
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                        getResources().getString(R.string.my_email), null));
                intentEmail.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                startActivity(Intent.createChooser(intentEmail, getString(R.string.send_email_chooser_title)));            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.about_layout);
        layout.setBackgroundColor(PrefsManager.getMainColor());
    }

}
