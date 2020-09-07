package com.kn160642.cats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kn160642.cats.helpers.Globals;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        SharedPreferences sharedpreferences = getSharedPreferences(Globals.sharedPreferencesName,
                Context.MODE_PRIVATE);


        if (sharedpreferences.contains(Globals.botPlays) == false) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Globals.botPlays, true);
            editor.commit();
        }

        if (sharedpreferences.contains(Globals.musicOn) == false) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Globals.musicOn, true);
            editor.commit();
        }

    }
}
