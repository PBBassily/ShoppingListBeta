package paula.mobdev.shoppingmania.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import paula.mobdev.shoppingmania.R;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView appTitlePartOne = (TextView)findViewById(R.id.app_title_part1);
        TextView appTitlePartTwo = (TextView)findViewById(R.id.app_title_part2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Nunito-ExtraBold.ttf");

        appTitlePartOne.setTypeface(custom_font);
        appTitlePartTwo.setTypeface(custom_font);

            /* New Handler to start the ListActivity
             * and close this Splash-Screen after 1 second.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the ListActivity. */
                    Intent mainIntent = new Intent(Splash.this,ListActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
    }
}
