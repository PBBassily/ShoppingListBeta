package paula.mobdev.shoppingmania.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import paula.mobdev.shoppingmania.R;

public class MainActivity extends AppCompatActivity {

    // this class is the Entry point to the project
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start the Splash screen
        startActivity(new Intent(this,Splash.class));
        finish();
    }
}
