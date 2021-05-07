package com.obelab.ui.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.Nullable;

import com.obelab.ui.R;

public class stdGraph extends View {
    private final String TAG = "[stdGraph]";
    public float[] Orbxy = new float[]{};
    public float[] Rectxy = new float[]{};
    public float[] Starxy = new float[]{};
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private RectF BitmapDst;
    private Bitmap BackGraphSrc;
    private Bitmap BackGraph;
    private Paint BitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float MarginLeft = 20;
    private float MarginTop = 10;
    private float MarginBottom = 10;
    private float MarginRight = 5;
    private Rect bSRC = new Rect();
    private RectF bDST = new RectF();
    private float MaxXValue = 0.40f;
    private int MaxYCount = 9;
    private float MaxTValue = 3;
    private Context mContext;
    private Typeface mfont;
    private float textSize = 8;
    private Makers makers;

    public stdGraph(Context context) {
        super(context);
        init(context);
    }

    public stdGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public stdGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public stdGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        BitmapDst = new RectF(0, 0, DptoPX(mContext, 323), DptoPX(mContext, 159));
        BitmapFactory.Options options = new BitmapFactory.Options();
        BackGraphSrc = BitmapFactory.decodeResource(getResources(), R.drawable.graph, options);


        textSize = DptoPX(mContext, 8);
        try {
            Typeface.create("nanumsquaretoundeb.ttf", Typeface.NORMAL);
        } catch (Exception e) {
            mfont = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

        }

        MarginLeft = DptoPX(mContext, 20);
        MarginBottom = DptoPX(mContext, 20);
        MarginTop = DptoPX(mContext, 20);
        MarginRight = DptoPX(mContext, 10);
        makers = new Makers(mContext);

        Orbxy = new float[]{-1.5f, 0.05f};
        Rectxy = new float[]{0, 0.10f};
        Starxy = new float[]{1.5f, 0.05f};
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.widthMeasureSpec = right - left;
        this.heightMeasureSpec = bottom - top;

        bSRC = new Rect(0, 0, BackGraphSrc.getWidth(), BackGraphSrc.getHeight());
        bDST = new RectF(MarginLeft, MarginTop, widthMeasureSpec - MarginRight, heightMeasureSpec - MarginBottom);

        BackGraph = Bitmap.createBitmap((int) bDST.width(), (int) bDST.height(), Bitmap.Config.ARGB_8888);
        invalidate();
    }

    private void drawbitmap(Canvas c, Bitmap b, float x, float y) {
        c.drawBitmap(b, x, y, null);
    }

    public void setDtd(float ba, float bc, float work) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //배경 그래프
        canvas.drawBitmap(BackGraphSrc, bSRC, bDST, BitmapPaint);
        //배경위에 추가요소
        DrawBack();
        canvas.drawBitmap(BackGraph, MarginLeft, MarginTop, BitmapPaint);

        //단위 표시
        DrawText(canvas);


    }

    private void DrawText(Canvas canvas) {
        drawText(canvas, "MAX", (int) MarginLeft / 2, (int) MarginTop / 2);


        float yGap = bDST.height() / 8;
        float y = 0.0f;
        for (int a = 0; a < 9; a++) {

            drawText(canvas, String.format("%.2f", y), (int) (MarginLeft / 2), (int) ((bDST.height() + MarginTop) - (yGap * a)));
            y += 0.05f;
        }
        // x 축 그리기
        float xGap = (bDST.width() / 2) / 3;
        int xText = 0;
        for (int a = 0; a < 4; a++) {
            drawText(canvas, "" + xText, (int) ((bDST.width() / 2 + (xGap * a)) + MarginLeft), (int) (bDST.height() + MarginTop + 20));
            xText++;
        }
        xText = -1;
        for (int a = 1; a < 4; a++) {
            drawText(canvas, "" + xText, (int) ((bDST.width() / 2 - (xGap * a)) + MarginLeft), (int) (bDST.height() + MarginTop + 20));
            xText--;
        }
    }

    // 좌표를 중심으로 정가운데 그린다 .
    private void drawText(Canvas c, String Text, int x, int y) {


        /*Paint Fillp = new Paint(Paint.ANTI_ALIAS_FLAG);

        Fillp.setAntiAlias(true);
        Fillp.setTextAlign(Paint.Align.CENTER);
        Fillp.setTextSize(textSize);
        Fillp.setTypeface(mfont);
        Fillp.setColor(Color.GRAY);
        Fillp.setStrokeWidth(8);
        Fillp.setStrokeCap(Paint.Cap.ROUND);
        Fillp.setStyle(Paint.Style.STROKE);*/


        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        p.setTypeface(mfont);
        p.setColor(Color.parseColor("#a5a9b4"));
        p.setStyle(Paint.Style.FILL);

        Rect Bound = new Rect();
        p.getTextBounds(Text, 0, Text.length(), Bound);

/*
        // 가이드
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        c.drawRect(x - (Bound.width()/2) , y-(Bound.height()/2) , x+ (Bound.width()/2), y+(Bound.height()/2) , p);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        // 가이드 끝
*/

        y = y + (Bound.height() / 2);
        //c.drawText(Text , x , y, Fillp);
        c.drawText(Text, x, y, p);


    }

    private void DrawBack() {
        Paint Line = new Paint(Paint.ANTI_ALIAS_FLAG);
        Canvas c2 = new Canvas(BackGraph);
        // 가이드
        Line.setStyle(Paint.Style.STROKE);
        Line.setStrokeWidth(2);
        /*Line.setColor(Color.parseColor("#aa0000"));
        c2.drawRect(0,0,BackGraph.getWidth() , BackGraph.getHeight() , Line);*/


        // 왼쪽 아래 외곽선
        Line.setStyle(Paint.Style.STROKE);
        Line.setStrokeWidth(3);
        Line.setColor(Color.parseColor("#90a3c1"));
        c2.drawLine(0, 0, 0, bDST.bottom, Line);
        c2.drawLine(0, bDST.bottom - 2, bDST.right, bDST.bottom - 2, Line);

        float Width = bDST.right - bDST.left;
        float Height = bDST.bottom - bDST.top;
        float WidthCLine = DptoPX(mContext, 5);

        // x 축 점찍기
        float MaxXCount = 8;
        float xGap = Height / MaxXCount;
        LOG("X-GAP[" + xGap);
        for (int a = 1; a < MaxYCount; a++) {
            c2.drawLine(0, Height - (xGap * a), WidthCLine, Height - (xGap * a), Line);
        }
        // y 축 점찍기
        float MaxYCount = 3;
        float yGap = (Width / 2) / MaxYCount;
        LOG("Y-GAP[" + xGap);
        Line.setColor(Color.parseColor("#FF0000"));
        for (int a = 0; a < MaxYCount + 1; a++) {
            c2.drawLine(Width / 2 + (yGap * a), Height - WidthCLine, Width / 2 + (yGap * a), Height, Line);
        }
        for (int a = 0; a < MaxYCount; a++) {
            c2.drawLine(Width / 2 - (yGap * a), Height - WidthCLine, Width / 2 - (yGap * a), Height, Line);
        }
        // 별그림
        float heightTick = Height / 40;
        float WidthTick = (Width / 2) / 30;

        makers.DrawOrb(c2, (int) (Width / 2) + (int) (Orbxy[0] * 10 * WidthTick), (int) Height - (int) (Orbxy[1] * 100 * heightTick));
        makers.DrawRect(c2, (int) (Width / 2) + (int) (Rectxy[0] * 10 * WidthTick), (int) Height - (int) (Rectxy[1] * 100 * heightTick));
        makers.DrawStar(c2, (int) (Width / 2) + (int) (Starxy[0] * 10 * WidthTick), (int) Height - (int) (Starxy[1] * 100 * heightTick));

    }

    private float DptoPX(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private void LOG(String text) {
        if (true) {
            Log.i(TAG, text);
        }


    }

    class Makers {
        private float WidthOrb = 10;
        private float WidthRect = 10;
        private float WidthStar = 14;
        private Bitmap bOrb;
        private Bitmap bRect;
        private Bitmap bStar;
        private BitmapFactory.Options options = new BitmapFactory.Options();
        private Rect RectOrb = new Rect();
        private Rect RectRect = new Rect();
        private Rect RectStar = new Rect();

        public Makers(Context mContext) {
            Bitmap Orb = BitmapFactory.decodeResource(getResources(), R.drawable.graph1, options);
            Bitmap Rect = BitmapFactory.decodeResource(getResources(), R.drawable.graph2, options);
            Bitmap Star = BitmapFactory.decodeResource(getResources(), R.drawable.graph3, options);


            WidthOrb = DptoPX(mContext, 15);
            WidthRect = DptoPX(mContext, 15);
            WidthStar = DptoPX(mContext, 20);
            bOrb = Bitmap.createBitmap((int) WidthOrb, (int) WidthOrb, Bitmap.Config.ARGB_8888);
            bRect = Bitmap.createBitmap((int) WidthRect, (int) WidthRect, Bitmap.Config.ARGB_8888);
            bStar = Bitmap.createBitmap((int) WidthStar, (int) WidthStar, Bitmap.Config.ARGB_8888);
            DrawMaker(bOrb, Orb);
            DrawMaker(bRect, Rect);
            DrawMaker(bStar, Star);


        }

        public void DrawMaker(Bitmap b, Bitmap m) {
            Canvas c = new Canvas(b);
            Rect DST = new Rect(0, 0, b.getWidth(), b.getHeight());
            Rect ost = new Rect(0, 0, m.getWidth(), m.getHeight());
            c.drawBitmap(m, ost, DST, null);


        }


        public void DrawOrb(Canvas c, int x, int y) {
            c.drawBitmap(bOrb, x - WidthOrb / 2, y - WidthOrb / 2, null);
        }

        public void DrawRect(Canvas c, int x, int y) {
            c.drawBitmap(bRect, x - WidthRect / 2, y - WidthRect / 2, null);
        }

        public void DrawStar(Canvas c, int x, int y) {
            c.drawBitmap(bStar, x - WidthStar / 2, y - WidthStar / 2, null);
        }
    }
}
