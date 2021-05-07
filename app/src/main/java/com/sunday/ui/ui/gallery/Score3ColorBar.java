package com.sunday.ui.ui.gallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Score3ColorBar extends View {
    private final String TAG = "[" + Score3ColorBar.class.getSimpleName() + "]";
    private int BarMax = 10;
    private int[] Colors = new int[]{
            Color.parseColor("#FF0000"),
            Color.parseColor("#00FF00"),
            Color.parseColor("#0000FF")};
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private float barWidth = 0;
    private double Value = 100;
    private float Height = 15;
    private Paint P = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float textSize = 10;
    private Typeface mfont;
    private Context mContext;

    public Score3ColorBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Score3ColorBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Score3ColorBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.widthMeasureSpec = right - left;
        this.heightMeasureSpec = bottom - top;
        barWidth = (float) widthMeasureSpec / 100;
        Height = DptoPX(getContext(), Height);
        P.setStyle(Paint.Style.FILL_AND_STROKE);

        Log.i(TAG, "Width : " + widthMeasureSpec + "][" + barWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Value != 0) {


            for (int a = 0; a < Value; a++) {
                if (a < 40) {
                    DrawBar(canvas, Colors[0], a);
                } else if (a >= 40 && a < 60) {
                    DrawBar(canvas, Colors[1], a);
                } else {
                    DrawBar(canvas, Colors[2], a);
                }

            }
            int LinrCount = (int) (Value/10);
            for(int a = 0 ; a < LinrCount+1 ;a++){
                DrawBarLine(canvas, Color.parseColor("#FFFFFF"), a*10);
            }

            drawText(canvas , ""+Value , (int) (Value* barWidth + barWidth), heightMeasureSpec/2 );
        }

    }
    private void DrawBarLine(Canvas canvas, int color, float index) {
        RectF rect = new RectF(index * barWidth, 0, (index * barWidth) + 2, Height);
        P.setColor(color);
        canvas.drawRect(rect, P);
    }
    private void DrawBar(Canvas canvas, int color, float index) {
        RectF rect = new RectF(index * barWidth, 0, (index * barWidth) + barWidth, Height);
        P.setColor(color);
        canvas.drawRect(rect, P);
    }

    public void setValue(float Value) {
        this.Value = Value;
        invalidate();
    }

    public void setHeight(int Height) {
        this.Height = DptoPX(getContext(), Height);
    }

    private void init(Context context) {
        P.setAntiAlias(true);
        P.setStyle(Paint.Style.FILL_AND_STROKE);
        P.setStrokeWidth(2);
        textSize = DptoPX(context , 17f);

        try {
            Typeface.create("nanumsquaretoundeb.ttf" , Typeface.BOLD);
        }catch (Exception e){
            mfont =  Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

        }
        invalidate();
    }
    public void setTextSize(Context mContext , float textSize){
        this.textSize = DptoPX(mContext , textSize);
        invalidate();
    }
    private float DptoPX(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    private void drawText(Canvas c, String Text, int x, int y ) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(textSize);
        p.setColor(Color.parseColor("#888888"));
        p.setStyle(Paint.Style.FILL);
        Rect textBound = new Rect();
        p.getTextBounds(Text , 0 , Text.length(),textBound);

        if(x + textBound.width() + 5 > widthMeasureSpec){
            c.drawText(Text ,widthMeasureSpec - (textBound.width()+5)  , y + textBound.height()/2, p);
        }else{
            c.drawText(Text , x , y + textBound.height()/2, p);
        }



    }
    public void setColor(int[] Colors){
        this.Colors = Colors;
    }

}
