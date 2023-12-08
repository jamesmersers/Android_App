package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public GameLoopThread gameLoopThread;
    private double touchX = -1;
    private double touchY = -1;
    private int hitNumber = 0;
    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);
        getHolder().addCallback(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameLoopThread = new GameLoopThread();
        gameLoopThread.start();
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameLoopThread.end();
    }
    public void draw(Canvas canvas){
        super.draw(canvas);
    }
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.touchX = -1;
                this.touchY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                //触摸移动事件
                break;
            case MotionEvent.ACTION_UP:
                this.touchX = event.getRawX()/ this.getWidth();
                this.touchY = (event.getRawY() - 220.0)/ this.getHeight();
                break;
        }
        return true;
    }
    public void resetGame() {
        hitNumber = 0;
        touchX = -1;
        touchY = -1;
    }
    public class GameLoopThread extends Thread {
        boolean isLive = true;
        @Override
        public void run() {
            super.run();
            ArrayList<GameSprinter> gameSprinters = new ArrayList<>();
            int iloop;
            for(iloop=0;iloop<3;iloop++){
                gameSprinters.add(new GameSprinter(Math.random(),Math.random(),R.drawable.book1));
                gameSprinters.add(new GameSprinter(Math.random(),Math.random(),R.drawable.book2));
                gameSprinters.add(new GameSprinter(Math.random(),Math.random(),R.drawable.book3));
            }
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(35);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            while(isLive){
                Canvas canvas = null;
                try{
                    canvas = GameView.this.getHolder().lockCanvas();
                    canvas.drawColor(Color.WHITE);
                    canvas.drawText("Your hit：" + hitNumber,100,100,paint);
                    for(GameSprinter gameSprinter : gameSprinters){
                        if(gameSprinter.detectCollision())
                            hitNumber++;
                        gameSprinter.move(canvas);
                    }
                    for(GameSprinter gameSprinter : gameSprinters){
                        gameSprinter.draw(canvas);
                    }
                }finally {
                    if(canvas != null){
                        GameView.this.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!isLive) {
                    break;
                }
            }
        }
        public void end() {
            isLive = false;
        }
    }

    private class GameSprinter {
        private double x;
        private double y;
        private double direction;
        private int imageId;
        private double _x;
        private double _y;
        public GameSprinter(double x, double y, int imageId) {
            this.x = x;
            this.y = y;
            this.imageId = imageId;
            this.direction = Math.random() * 2 * Math.PI;
            this._x = 50.0/GameView.this.getWidth();
            this._y= 50.0/GameView.this.getHeight();
        }
        public void draw(Canvas canvas) {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageId);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, true);
            canvas.drawBitmap(scaledBitmap,(int)(canvas.getWidth()*this.x),(int)(canvas.getHeight()*this.y),null);
        }

        public void move(Canvas canvas) {
            this.y += Math.sin(this.direction) * 0.05;
            this.x += Math.cos(this.direction) * 0.05;
            if(this.y > 1) this.y = 0;
            if(this.y < 0) this.y = 1;
            if(this.x > 1) this.x = 0;
            if(this.x < 0) this.x = 1;
            if(Math.random() < 0.05){
                this.direction = Math.random() * 2 * Math.PI;
            }
        }

        public boolean detectCollision() {
            double distanceX = Math.abs(this.x - GameView.this.touchX);
            double distanceY = Math.abs(this.y - GameView.this.touchY);
            if(distanceX < _x && distanceY < _y){
                return true;
            }
            return false;
        }
    }
}