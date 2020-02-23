package com.ailihgong.candycrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

public class GameLayout extends SurfaceView implements Runnable {

    Thread thread = null;
    Bitmap background;
    private int screenHeight;
    private int screenWidth;

    private int row = 12;
    private int col = 8;

    public GameLayout(Context context) {
        super(context);

        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.cupcakebackground);

        int ave = 0;
        int size = (screenWidth - ave * 2) / row;

    }

    @Override
    public void run() {

    }

    public void pause() {

    }

    public void resume() {

    }



}
