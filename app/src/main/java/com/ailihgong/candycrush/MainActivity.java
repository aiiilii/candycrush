package com.ailihgong.candycrush;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gameView = (GameView) this.findViewById(R.id.gameview);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected  void onResume() {
        super.onResume();
    }

}
