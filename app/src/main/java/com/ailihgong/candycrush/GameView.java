package com.ailihgong.candycrush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameView extends View {

    private static Paint paintBlock = null;

    private static Random random = new Random();

    private boolean swapState = false;

    private boolean loadAnimalState = false;

    private int lastTouchC;
    private int lastTouchR;
    private int newTouchC;
    private int newTouchR;

    private int rows;
    private int cols;
    private int randomUpperBound;
    private int[][] matrix;

    private int size;
    private int width;
    private int height;

    public GameView(Context context, AttributeSet attr) {
        super(context, attr);

        if (paintBlock == null) {
            paintBlock = new Paint();
        }
        rows = 12;
        cols = 8;
        randomUpperBound = 6;
        this.matrix = generateRandomMatrix(cols, rows, randomUpperBound);
    }

    private int[][] generateRandomMatrix(int cols, int rows, int randomUpperBound) {
        int[][] matrix = new int[cols][rows];

//        int[] dx = new int[] {-1, 0, 0, 1};
//        int[] dy = new int[] {0, -1, 1, 0};

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int r = random.nextInt(randomUpperBound);

                while ((i - 2 >= 0 && matrix[i - 1][j] == r && matrix[i - 2][j] == r) || (j - 2 >= 0 &&matrix[i][j - 1] == r && matrix[i][j - 2] == r) ||
                        (i - 1 >= 0 && j - 1 >= 0 && matrix[i - 1][j] == r && matrix[i - 1][j - 1] == r) || (i - 1 >= 0 && j - 1 >= 0 && matrix[i][j - 1] == r && matrix[i - 1][j - 1] == r) ||
                        (i - 1 >= 0 && j + 1 < rows && matrix[i - 1][j + 1] == r && matrix[i - 1][j] == r) || (i - 1 >= 0 && j - 1 >= 0 && matrix[i - 1][j] == r && matrix[i][j - 1] == r)) {
                    r = random.nextInt(randomUpperBound);
                }

                matrix[i][j] = r;
            }
        }
        return matrix;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        size = canvas.getWidth() / 8;
        width = canvas.getWidth();
        height = canvas.getHeight();

//        int cols = 8;
//        int rows = 12;
//        int randomUpperBound = 6;
//
//        int[][] matrix = generateRandomMatrix(cols, rows, randomUpperBound);

        // int[][] matrix = this.matrix;

        for (int i = 0; i < cols; i ++) {
            for (int j = 0; j < rows; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
                if (matrix[i][j] == 0) {
                    paintBlock.setColor(Color.argb(255, 255, 0, 0));
                }else if (matrix[i][j] == 1) {
                    paintBlock.setColor(Color.argb(255, 255, 255, 0));
                } else if (matrix[i][j] == 2) {
                    paintBlock.setColor(Color.argb(255, 255, 255, 255));
                } else if (matrix[i][j] == 3) {
                    paintBlock.setColor(Color.argb(255, 0, 255, 0));
                } else if (matrix[i][j] == 4) {
                    paintBlock.setColor(Color.argb(255, 0, 0, 255));
                } else if (matrix[i][j] == 5) {
                    paintBlock.setColor(Color.argb(255, 0, 255, 255));
                } else if (matrix[i][j] == -1) {
                    paintBlock.setColor(Color.argb(255, 0, 0, 0));
                }

                canvas.drawRoundRect(i * size, j * size, size + i * size, size + j * size, 1, 1, paintBlock);
            }

            System.out.println();
        }
    }

    private void swap(int c, int r, int nc, int nr) {
        System.out.println("IN SWAP");
        int temp = matrix[c][r];
        matrix[c][r] = matrix[nc][nr];
        matrix[nc][nr] = temp;

        Set<Pair<Integer, Integer>> set1 = getShouldRemoveCells(this.matrix, c, r);
        Set<Pair<Integer, Integer>> set2 = getShouldRemoveCells(this.matrix, nc, nr);
        if (set1.isEmpty() && set2.isEmpty()) {
            temp = matrix[c][r];
            matrix[c][r] = matrix[nc][nr];
            matrix[nc][nr] = temp;
            return;
        }

        removeCells(this.matrix, set1);
        removeCells(this.matrix, set2);

        invalidate();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveRemovedCellsUp(matrix);

                invalidate();
            }
        }, 600);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //final int action = MotionEventCompat.getActionMasked(ev);
        final int action = ev.getAction();

        if (swapState) {
            return false;
        }

        if (loadAnimalState) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                // Remember where we started (for dragging)
                lastTouchC = (int) (ev.getX() / size);
                lastTouchR = (int) (ev.getY() / size);
                System.out.println(lastTouchC + " " + lastTouchR);
                System.out.println("x:" + ev.getX() + " y:" + ev.getY());

                System.out.println("ACTION_DOWN " + lastTouchC + " " + lastTouchR);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                System.out.println("move");

                // Remember this touch position for the next move event
//                lastTouchC = ev.getX() / width;
//                lastTouchR = ev.getY() / height;

                break;
            }

            case MotionEvent.ACTION_UP: {
                System.out.println("ACTION_UP");

                newTouchC = (int) (ev.getX() / size);
                newTouchR = (int) (ev.getY() / size);
                System.out.println("x:" + ev.getX() + " y:" + ev.getY());
                System.out.println(newTouchC + " " + newTouchR);

                if ((Math.abs(newTouchC - lastTouchC) <= 1 && Math.abs(newTouchR - lastTouchR) == 0) ||
                        (Math.abs(newTouchC - lastTouchC) == 0 && Math.abs(newTouchR - lastTouchR) <= 1)) {
                    //System.out.println(newTouchC + " " +  lastTouchC+ " " +  newTouchR+ " " +  lastTouchR);
                    swap(lastTouchC, lastTouchR, newTouchC, newTouchR);
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                System.out.println("ACTION_POINTER_UP");

                break;
            }
        }
        return true;
    }

    public Set<Pair<Integer, Integer>> getShouldRemoveCells(int[][] matrix, int c, int r) {
        Set<Pair<Integer, Integer>> res = new HashSet<Pair<Integer, Integer>>();

        int countC = 0;
        int countR = 0;

        int tempC = c - 1;
        int tempR = r;
        int x = matrix[c][r];

        while (tempC >= 0 && matrix[tempC][tempR] == x) {
            countC ++;
            tempC --;
        }
        tempC = c + 1;
        while (tempC < matrix.length && matrix[tempC][tempR] == x) {
            countC ++;
            tempC ++;
        }
        tempC = c;
        if (countC >= 2) {
            while (tempC >= 0 && matrix[tempC][tempR] == x) {
                res.add(new Pair<Integer, Integer>(tempC, tempR));
                tempC--;
            }
            tempC = c + 1;
            while (tempC < matrix.length && matrix[tempC][tempR] == x) {
                res.add(new Pair<Integer, Integer>(tempC, tempR));
                tempC++;
            }
        }

        tempC = c;
        tempR = r - 1;

        while (tempR >= 0 && matrix[tempC][tempR] == x) {
            countR ++;
            tempR --;
        }
        tempR = r + 1;
        while (tempR < matrix[0].length && matrix[tempC][tempR] == x) {
            countR ++;
            tempR ++;
        }
        tempR = r;
        if (countR >= 2) {
            while (tempR >= 0 && matrix[tempC][tempR] == x) {
                res.add(new Pair<Integer, Integer>(tempC, tempR));
                tempR --;
            }
            tempR = r + 1;
            while (tempR < matrix[0].length && matrix[tempC][tempR] == x) {
                res.add(new Pair<Integer, Integer>(tempC, tempR));
                tempR ++;
            }
        }

        return res;
    }

    public void removeCells(int[][] matrix, Set<Pair<Integer, Integer>> shouldRemoveCells) {
        for (Pair<Integer, Integer> pair : shouldRemoveCells) {
            matrix[pair.first][pair.second] = -1;
        }
    }

    public void moveRemovedCellsUp(int[][] matrix) {
        System.out.println("In moving removed cells upwards");
//        for (int k = 0; k < matrix[0].length; k++) {
//            int i = matrix.length - 1;
//            int j = matrix.length - 1;
//            while (i >= 0 && j >= 0) {
//                while (matrix[j][k] == -1) {
//                    j--;
//                }
//                matrix[i][k] = matrix[j][k];
//                i--;
//                j--;
//            }
//            while (i >= 0) {
//                matrix[i][k] = -1;
//                i--;
//            }
//        }

        for (int k = 0; k < matrix.length; k++) {
            int i = matrix[0].length - 1;
            int j = matrix[0].length - 1;
            firstWhile:
            while (i >= 0 && j >= 0) {
                while (matrix[k][j] == -1) {
                    j--;
                    if (j < 0) {
                        break firstWhile;
                    }
                }
                matrix[k][i] = matrix[k][j];
                i--;
                j--;
            }
            while (i >= 0) {
                matrix[k][i] = -1;
                i--;
            }
        }
    }





}
