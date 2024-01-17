package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class CustomClockView extends View {

    private Paint mPaint;
    private int mCenterX, mCenterY;
    private int mRadius;

    public CustomClockView(Context context) {
        super(context);
        init();
    }

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = Math.min(w, h) / 2 - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制时钟外观
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

        // 绘制时钟刻度
        mPaint.setStrokeWidth(3);
        for (int i = 0; i < 12; i++) {
            double cos = Math.cos(Math.toRadians(i * 30));
            double sin = Math.sin(Math.toRadians(i * 30));
            float startX = mCenterX + (float) cos * (mRadius - 30);
            float startY = mCenterY + (float) sin * (mRadius - 30);
            float endX = mCenterX + (float) cos * mRadius;
            float endY = mCenterY + (float) sin * mRadius;
            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }

        // 获取当前的时间
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        // 绘制时针
        float hourAngle = (hours * 30) + (minutes * 0.5f);
        float hourHandLength = mRadius * 0.5f;
        float hourHandX = mCenterX + (float) Math.cos(Math.toRadians(hourAngle - 90)) * hourHandLength;
        float hourHandY = mCenterY + (float) Math.sin(Math.toRadians(hourAngle - 90)) * hourHandLength;
        mPaint.setStrokeWidth(10);
        canvas.drawLine(mCenterX, mCenterY, hourHandX, hourHandY, mPaint);

        // 绘制分针
        float minuteAngle = minutes * 6;
        float minuteHandLength = mRadius * 0.7f;
        float minuteHandX = mCenterX + (float) Math.cos(Math.toRadians(minuteAngle - 90)) * minuteHandLength;
        float minuteHandY = mCenterY + (float) Math.sin(Math.toRadians(minuteAngle - 90)) * minuteHandLength;
        mPaint.setStrokeWidth(5);
        canvas.drawLine(mCenterX, mCenterY, minuteHandX, minuteHandY, mPaint);

        // 绘制秒针
        float secondAngle = seconds * 6;
        float secondHandLength = mRadius * 0.9f;
        float secondHandX = mCenterX + (float) Math.cos(Math.toRadians(secondAngle - 90)) * secondHandLength;
        float secondHandY = mCenterY + (float) Math.sin(Math.toRadians(secondAngle - 90)) * secondHandLength;
        mPaint.setStrokeWidth(2);
        canvas.drawLine(mCenterX, mCenterY, secondHandX, secondHandY, mPaint);
    }
}
