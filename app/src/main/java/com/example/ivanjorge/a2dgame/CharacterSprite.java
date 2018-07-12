package com.example.ivanjorge.a2dgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by rushd on 7/5/2017.
 */

public class CharacterSprite {


    private Bitmap image;
    public int x, y;
    public int xVelocity = 10;
    public int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public CharacterSprite (Bitmap bmp) {
        image = bmp;
        x = 100;
        y = 100;
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);


    }
    public void update() {


//        x += xVelocity;
        y += yVelocity;
//        if ((x > screenWidth - image.getWidth()) || (x < 0)) {
//            xVelocity = xVelocity*-1;
//        }
//        if ((y > screenHeight - image.getHeight()) || (y < 0)) {
//            y=100;
////            yVelocity += yVelocity;
//        }

    }
}
