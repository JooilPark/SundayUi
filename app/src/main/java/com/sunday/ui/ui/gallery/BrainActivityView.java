package com.sunday.ui.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BrainActivityView extends View {
    private final String TAG = "[BrainActivityView]";
    public double[][] BrainActivity = new double[4][14];
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private Bitmap GridientBitmap;
    private float RectWidth = 0;

    public BrainActivityView(@NonNull Context context) {
        super(context);
        init();
    }

    public BrainActivityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrainActivityView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        GridientBitmap = Bitmap.createBitmap(100, 10, Bitmap.Config.ARGB_8888);
        setGrident(new Canvas(GridientBitmap));
        Log.i(TAG, "init");
        // 예제 채우기
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 14; b++) {
                BrainActivity[a][b] = ((float) (a + 1 * b + 1) / 16) * 100;
            }
        }

        invalidate();


    }

    private void setGrident(Canvas c) {
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setShader(new LinearGradient(0, 0, 100, 0,
                new int[]{Color.parseColor("#ff3399"), Color.parseColor("#fcff02"), Color.parseColor("#00cccc"), Color.parseColor("#356bf1")}
                , null, Shader.TileMode.CLAMP));
        fillPaint.setFilterBitmap(true);
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        c.drawRect(0, 0, 100, 100, fillPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout");
        this.widthMeasureSpec = right - left;
        this.heightMeasureSpec = bottom - top;
        RectWidth = widthMeasureSpec / 14;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure");
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
        int  y = 0;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);

        for (int a = 0; a < 4; a++) {
            float x =  ((widthMeasureSpec - (RectWidth * 14f)) /2);
            for (int b = 0; b < 14; b++) {
                if((a == 0 && ( b==0 || b == 1 || b== 13 || b== 12) ||
                        (a == 3 && ( b==0 || b == 1 || b== 13 || b== 12)
                        ))){

                }else{
                    if(BrainActivity[a][b] <= 0){
                        p.setColor(GridientBitmap.getPixel(0, 0));
                    }else if(BrainActivity[a][b] >= 100){
                        p.setColor(GridientBitmap.getPixel(99, 0));
                    }else{
                        Log.i(TAG, "Color[" + BrainActivity[a][b]);
                        p.setColor(GridientBitmap.getPixel((int) BrainActivity[a][b], 0));

                    }

                    c.drawRect(x, y, x + RectWidth, y + RectWidth, p);

                }



                x += RectWidth;
            }
            y += RectWidth;
        }
    }
    public void setMap(double[][] map){
        BrainActivity = map;
        invalidate();
    }

}
