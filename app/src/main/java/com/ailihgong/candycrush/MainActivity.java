package com.ailihgong.candycrush;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    GameView gameView;

    private EditText scoreText;

    private EditText scoreEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        scoreText = (EditText) this.findViewById(R.id.editText);
        scoreText.setText("0");
        scoreText.setEnabled(false);

        scoreEditText = (EditText) this.findViewById(R.id.scoreEditText);
        scoreEditText.setEnabled(false);
        gameView = (GameView) this.findViewById(R.id.gameview);
        gameView.setScoreText(scoreText);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected  void onResume() {
        super.onResume();
    }


    public void resetButtonClicked(View view) {
        this.gameView.resetButtonClicked();
    }
}
