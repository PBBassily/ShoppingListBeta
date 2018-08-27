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
        TextView tx1 = (TextView)findViewById(R.id.app_title_part1);
        TextView tx2 = (TextView)findViewById(R.id.app_title_part2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Nunito-ExtraBold.ttf");

        tx1.setTypeface(custom_font);
        tx2.setTypeface(custom_font);

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this,ListActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
    }
}
