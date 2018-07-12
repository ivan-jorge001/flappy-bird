package com.example.ivanjorge.a2dgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.R.drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rushd on 7/5/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static int characterW = 200; //300
    public static int characterH = 150;//240
    public static int gapHeight = characterH * 2; //300;

    public static int velocity = 10;

    private MainThread thread;
    private CharacterSprite characterSprite;
    private PipeSprite pipe1;
    private PipeSprite pipe2;
    private PipeSprite pipe3;

    private int screenHeight =
            Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth =
            Resources.getSystem().getDisplayMetrics().widthPixels;

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        characterSprite.y = characterSprite.y - (characterSprite.yVelocity * 10);
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.flappy), characterW, characterH));

        Bitmap bmp;
        Bitmap bmp2;
        int y;
        int x;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.pipe_down), 200,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.pipe), 200,
                Resources.getSystem().getDisplayMetrics().heightPixels / 2);

        pipe1 = new PipeSprite(bmp, bmp2, 0, 100);
        pipe2 = new PipeSprite(bmp, bmp2, -500, 150);
        pipe3 = new PipeSprite(bmp, bmp2, 500, 150);

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        characterSprite.update();
        pipe1.update();
        pipe2.update();
        pipe3.update();
        logic();
    }

    @Override
    public void draw(Canvas canvas)
    {

        super.draw(canvas);
        if(canvas!=null) {
            characterSprite.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void logic() {

        List pipes = new ArrayList<PipeSprite>();
        pipes.add(pipe1);
        pipes.add(pipe2);
        pipes.add(pipe3);

        for (int i = 0; i < pipes.size(); i++) {
            PipeSprite pipe = ((PipeSprite)pipes.get(i));

            //Detect if the character is touching one of the pipes

            int characterPosY = characterSprite.y;
            int characterPosX = characterSprite.x;

            int pipePoisionInY = pipe.yY;
            int pipePoisionInX = pipe.xX;

            if (characterPosY < pipePoisionInY + (screenHeight / 2)
                    - (gapHeight / 2) && characterPosX + characterW > pipePoisionInX
                    && characterPosX < pipePoisionInX + 500) {
                resetLevel();
            } else if (characterPosY + characterH > (screenHeight / 2) +
                    (gapHeight / 2) + pipePoisionInY
                    && characterPosX + characterW > pipePoisionInX
                    && characterPosX < pipePoisionInX + 500) {
                resetLevel();
            }

            //Detect if the pipe has gone off the left of the 
            //screen and regenerate further ahead
            if (pipePoisionInX + 500 < 0) {
                Random r = new Random();
                int value1 = r.nextInt(500);
                int value2 = r.nextInt(500);
                pipePoisionInX = screenWidth + value1 + 1000;
                pipePoisionInY = value2 - 250;
            }
        }

        //Detect if the character has gone off the 
        //bottom or top of the screen
        if (characterSprite.y + characterH < 0) {
            resetLevel(); }
        if (characterSprite.y > screenHeight) {
            resetLevel(); }
    }

    public void resetLevel() {
        characterSprite.y = 100;
        pipe1.xX = 2000;
        pipe1.yY = 0;
        pipe2.xX = 4500;
        pipe2.yY = 200;
        pipe3.xX = 3200;
        pipe3.yY = 250;

    }
}
