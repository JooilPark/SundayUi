package com.sunday.ui.ui.gallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class BrainConnectivityView extends View {
    private final String TAG = "[BrainConnectivityView]";
    public double[][] BrainConnectivity = new double[48][48];
    private float widthMeasureSpec;
    private float heightMeasureSpec;
    private Bitmap GridientBitmap;
    private float RectWidth = 0;


    public BrainConnectivityView(Context context) {
        super(context);
        init();
    }

    public BrainConnectivityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrainConnectivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BrainConnectivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        GridientBitmap = Bitmap.createBitmap(100, 10, Bitmap.Config.ARGB_8888);
        setGrident(new Canvas(GridientBitmap));
        Log.i(TAG, "init");
        // 예제 채우기
        double start = -1.0;
        for (int a = 0; a < 48; a++) {
            for (int b = 0; b < 48; b++) {
                // -1.00 ~ 1.00 까지
                BrainConnectivity[a][b] = (((int) (((float) (a * b) / 2209) * 100f)) / 100f * 2) - 0.5;
                //Log.i(TAG, String.format("BrainConnectivity %.2f" ,   BrainConnectivity[a][b]));

            }
        }

        invalidate();
    }

    private void setGrident(Canvas c) {
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setShader(new LinearGradient(0, 0, 100, 0,
                new int[]{
                        Color.parseColor("#356bf1"),
                        Color.parseColor("#00cccc"),
                        Color.parseColor("#fcff02"),
                        Color.parseColor("#ff3399")


                }
                , null, Shader.TileMode.CLAMP));
        fillPaint.setFilterBitmap(true);
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        c.drawRect(0, 0, 100, 100, fillPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.widthMeasureSpec = right - left;
        this.heightMeasureSpec = bottom - top;
        RectWidth = (int)(widthMeasureSpec / 48.0f);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Log.i(TAG, "onDraw");
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setColor(Color.GRAY);
        // canvas.drawRect(0, 0, 200, 200, p);

        DrawRects(canvas);
        // canvas.drawBitmap(GridientBitmap, 0, 0, null);
    }

    private void DrawRects(Canvas c) {
        float widthpadding  = ( (widthMeasureSpec - (RectWidth * 48)))/2;
        float y = heightMeasureSpec - (heightMeasureSpec - RectWidth * 48);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);

        for (int a = 0; a < 48; a++) {
            float  x = widthpadding;
            for (int b = 0; b < 48; b++) {

                if (BrainConnectivity[a][b] < 0.1) {
                    p.setColor(Color.BLACK);
                } else if (BrainConnectivity[a][b] >= 1) {
                    p.setColor(GridientBitmap.getPixel(99, 0));
                } else {
                    Log.i(TAG, "Color[" + a + "][" + b + "]=[" + BrainConnectivity[a][b]);
                    p.setColor(GridientBitmap.getPixel((int) ((BrainConnectivity[a][b] + 1) / 2.0 * 100.0), 0));

                }


                RectF RF = new RectF(x, y, x + RectWidth, y + RectWidth);
                c.drawRect(RF, p);


                x += RectWidth;
            }
            y -= RectWidth;
        }
    }

    // -1.00 ~ 1.00 응 백분율로 변환
    public void setMap(double[][] map) {

        BrainConnectivity = map;
        invalidate();
    }
}
