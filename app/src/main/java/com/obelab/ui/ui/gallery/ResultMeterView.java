package com.obelab.ui.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.obelab.ui.R;

public class ResultMeterView extends View {


    private final String TAG = "[ResultMeterView]";
    private final float TickArc = 140f / 100f;

    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private float mStrockwidth = 15f;
    private int mStrockColor = Color.parseColor("#c4cbdb");
    private int mGridientStartColor = Color.parseColor("#364969");
    private int mGridientCenterColor = Color.parseColor("#51648c");
    private int mGridientEndColor = Color.parseColor("#bdd1da");
    private Paint mArcPaint;
    private Paint mBackBitmapPatin = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private Bitmap mArcBitmap;
    private int margin = 10;
    private Bitmap Niddle;
    private Bitmap NiddleBack;
    private String[] Labels = new String[]{"위험", "주의", "정상"};
    private float textSize = 10;
    private Typeface mfont;
    public ResultMeterView(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ResultMeterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }


    public ResultMeterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ResultMeterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);


    }



    /**
     * 뷰 초기화
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mArcPaint = new Paint();
        mArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mArcPaint.setStrokeWidth(mStrockwidth);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.GRAY);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        textSize = DptoPX(context , 14.8f);
        try {
            Typeface.create("nanumsquaretoundeb.ttf" , Typeface.BOLD);
        }catch (Exception e){
            mfont =  Typeface.create(Typeface.DEFAULT, Typeface.BOLD);

        }




    }
    //출처: https://baramziny.tistory.com/entry/안드로이드-텍스트-그리기 [양군&우자]
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "left[" + left + "]top[" + top + "]right[" + right + "]bottom[" + bottom + "]");
        this.widthMeasureSpec = right - left;
        this.heightMeasureSpec = bottom - top;
        //

        Niddle = Bitmap.createBitmap(20, widthMeasureSpec / 2, Bitmap.Config.ARGB_8888);
        setNiddle(new Canvas(Niddle));

        //
        mBitmap = Bitmap.createBitmap(widthMeasureSpec, heightMeasureSpec, Bitmap.Config.ARGB_8888);
        mArcBitmap = Bitmap.createBitmap(widthMeasureSpec, heightMeasureSpec, Bitmap.Config.ARGB_8888);
        NiddleBack = Bitmap.createBitmap(widthMeasureSpec, heightMeasureSpec, Bitmap.Config.ARGB_8888);
        DrawBack(new Canvas(mArcBitmap));
        DrawNiddle(new Canvas(NiddleBack), 0);

    }

    private void setNiddle(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //p.setColor(Color.parseColor("#aa00FF00"));
        //   canvas.drawRect(0,0,widthMeasureSpec,mArcBitmap.getHeight() , p);
        // canvas.drawRect(0, 0, Niddle.getWidth(), Niddle.getHeight(), p);


        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#494d56"));
        Log.i(TAG, "setNiddle[" + Niddle.getWidth());

        canvas.drawCircle(Niddle.getWidth() / 2, Niddle.getHeight() - 10, 10, p);


        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(Niddle.getWidth() / 2, 0);
        path.lineTo(0, Niddle.getHeight() - 10);

        path.lineTo(Niddle.getWidth(), Niddle.getHeight() - 10);

        path.moveTo(Niddle.getWidth() / 2, 0);

        path.close();
        canvas.drawPath(path, p);
    }

    public void DrawNiddle(Canvas canvas, int i) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


        float Arc = TickArc * (float) i;
        Log.i(TAG, "DrawNiddle[" + Arc);
        int cx = (widthMeasureSpec / 2);
        int cy = NiddleBack.getHeight() - 10;

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.parseColor("#aa00FF00"));
        canvas.rotate(0);


        canvas.rotate(Arc - 70, cx, cy);

        canvas.drawBitmap(Niddle, cx - (Niddle.getWidth() / 2), cy - Niddle.getHeight() + 9, null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    //https://gtothe1.tistory.com/entry/%EC%85%B0%EC%9D%B4%EB%8D%94-%EC%A7%81%EC%84%A0-%EA%B7%B8%EB%9E%98%EB%94%94%EC%96%B8%ED%8A%B8-LinearGradient
    //https://stackoverflow.com/questions/17623947/android-fill-partial-arc
    private void DrawBack(Canvas canvas) {
// 패인터 설정
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);


        //Create Paint Object
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setShader(new LinearGradient(100, 0, widthMeasureSpec - 100, 0,
                new int[]{Color.parseColor("#ff3399"), Color.parseColor("#fcff02"), Color.parseColor("#00cccc")}
                , null, Shader.TileMode.CLAMP));
        fillPaint.setFilterBitmap(true);
        fillPaint.setAntiAlias(true);
        // 두깨
        fillPaint.setStrokeWidth(DptoPX(getContext(),100));
        fillPaint.setStyle(Paint.Style.STROKE);

        float width = (float) widthMeasureSpec;
        float height = (float) heightMeasureSpec * 2;

        float radius;

        //Get radius from the bigger size
        if (width > height) {
            radius = height / 2;
        } else {
            radius = width / 2;
        }
        //Create Contour Object
        Path path = new Path();

        float center_x, center_y;
        center_x = width / 2;
        center_y = height / 2;
        center_y = center_y - 10;
        //Configure rect for the outer ring
        final RectF oval = new RectF();
        final int outterRing = (int)DptoPX(getContext(),0.3f);
        oval.set(center_x - radius + outterRing,
                center_y - radius + outterRing,
                center_x + radius - outterRing,
                center_y + radius - outterRing);
        //Add outer arc
        path.addArc(oval, -20, -140);

        //Configure rect for the inner ring
        final int InnerRing = (int)DptoPX(getContext(),100);
        oval.set(center_x - radius + InnerRing,
                center_y - radius + InnerRing,
                center_x + radius - InnerRing,
                center_y + radius - InnerRing);

        //Add inner arc to the path but draw counterclockwise
        path.arcTo(oval, -160, 140);

        //close path
        path.close();

        //Create Contour Object
        Path fillPath = new Path();
        //Configure rect for the fill ring
        final int ArcInner = (int)DptoPX(getContext(),50);
        oval.set(center_x - radius + ArcInner,
                center_y - radius + ArcInner,
                center_x + radius - ArcInner,
                center_y + radius - ArcInner);

        //Add fill arc
        fillPath.addArc(oval, -20, -140);


        //draw fill path
        canvas.drawPath(fillPath, fillPaint);
        //draw outer path
        canvas.drawPath(path, paint);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint p = new Paint();
        p.setColor(Color.LTGRAY);
        // canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        canvas.drawBitmap(mArcBitmap, 0, 0, p);

        DrawLabels(canvas, Labels);


        canvas.drawBitmap(NiddleBack, 0, 0, p);
        //DrawNiddle(new Canvas(NiddleBack), value);
       /* DrawNiddle(new Canvas(NiddleBack), 0);
        DrawNiddle(new Canvas(NiddleBack), 10);
        DrawNiddle(new Canvas(NiddleBack), 50);

        DrawNiddle(new Canvas(NiddleBack), 90);
        DrawNiddle(new Canvas(NiddleBack), 100);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, widthMeasureSpec / 2, heightMeasureSpec, p);*/

    }

    public void setValue(int value) {
        DrawNiddle(new Canvas(NiddleBack), value);
        invalidate();
    }

    public void setLabels(Context context, String[] Labels, float dp) {
        this.Labels = Labels;
        textSize = DptoPX(context , 14.8f);
        invalidate();
    }

    private void DrawLabels(Canvas c, String[] Text) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        p.setTypeface(mfont);
        drawText(c, Text[0], widthMeasureSpec / 5, (int) (heightMeasureSpec - (heightMeasureSpec / 2.5)), p);
        drawText(c, Text[1], widthMeasureSpec / 2, heightMeasureSpec / 3, p );
        drawText(c, Text[2], widthMeasureSpec - (widthMeasureSpec / 6), (int) (heightMeasureSpec - (heightMeasureSpec / 2.5)), p);
    }

    // 좌표를 중심으로 정가운데 그린다 .
    private void drawText(Canvas c, String Text, int x, int y , Paint pp) {
        Paint Fillp = new Paint(Paint.ANTI_ALIAS_FLAG);

        Fillp.setAntiAlias(true);
        Fillp.setTextAlign(Paint.Align.CENTER);
        Fillp.setTextSize(textSize);
        Fillp.setTypeface(mfont);
        Fillp.setColor(Color.GRAY);
        Fillp.setStrokeWidth(8);
        Fillp.setStrokeCap(Paint.Cap.ROUND);
        Fillp.setStyle(Paint.Style.STROKE);
        c.drawText(Text , x , y, Fillp);


        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        //p.setTypeface(mfont);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        c.drawText(Text , x , y, p);




    }

    private float DptoPX(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
