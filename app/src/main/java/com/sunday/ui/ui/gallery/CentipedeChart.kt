package com.sunday.ui.ui.gallery

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.abs

/**
 * bitmap에 그리는 방식으로 변경
 */
class CentipedeChart : View {
    private var widthDp = 0.0 // 박스사이의 공간
    private var maxBoxs = 5// 박스는 몇개인가 ?
    private var barHeight = DptoPX(context, 12f) // 바의 높이
    private var oneBox = 0f // 박스의 크기
    private var oneTick = 0f // 1의 넓이
    private var centerVerticalLine = 0f // 중간 높이
    private val ovalSize = DptoPX(context, 30f)
    private var paddingVer = ovalSize / 2 // 원표시를 위한 공간
    private val oval: RectF = RectF(0f, 0f, ovalSize, ovalSize)
    private var positionIndex: Int = 75
    private var barColorArray: ArrayList<Int> = arrayListOf()
    private val sideInversion = Matrix().apply {
        setScale(-1f, 1f)
    }
    var isReverse = false
    private var paintHighlight = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#a8ea6e")
    }

    private var paintdefault = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#d5d5d5")
    }
    private var paintLog = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#000000")
        textSize = 40f
    }
    private val paintWhiteBar = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE

    }

    constructor(context: Context?) : super(context) {
        InitViewSetting(5, arrayListOf(Color.parseColor("#eb6060"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#10ce9c")), 6.0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        InitViewSetting(5, arrayListOf(Color.parseColor("#eb6060"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#10ce9c")), 6.0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        InitViewSetting(5, arrayListOf(Color.parseColor("#eb6060"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#fbd12b"),
                Color.parseColor("#10ce9c")), 6.0)
    }

    /**
     * start 시작값
     * max 끝값
     * cutValue
     * widthDp 컷바 위치
     * format 표시 포멧
     */
    fun InitViewSetting(maxBoxs: Int, barColors: ArrayList<Int>, widthDp: Double) {
        if (barColors.size != maxBoxs) {
            throw IndexOutOfBoundsException("maxBoxs 와 barColors 크기는 같아야 합니다 . ")
        }
        this.maxBoxs = maxBoxs
        this.barColorArray = barColors
        this.widthDp = widthDp
    }

    val backRect = RectF(0f, 0f, 0f, 0f)
    var backBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {


        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
                getDefaultSize(suggestedMinimumHeight, heightMeasureSpec))
        backRect.set(0f, 0f, getDefaultSize(suggestedMinimumWidth, widthMeasureSpec).toFloat(), getDefaultSize(suggestedMinimumHeight, heightMeasureSpec).toFloat())


        centerVerticalLine = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec).toFloat() / 2
        oneBox = (getDefaultSize(suggestedMinimumWidth, widthMeasureSpec).toFloat() - (paddingVer * 2)) / maxBoxs.toFloat()
        oval.top = centerVerticalLine - (ovalSize / 2)
        oval.bottom = centerVerticalLine + (ovalSize / 2)
        oneTick = (getDefaultSize(suggestedMinimumWidth, widthMeasureSpec).toFloat() - (paddingVer * 2)) / 100

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        backBitmap = Bitmap.createBitmap(backRect.width().toInt(), backRect.height().toInt(), Bitmap.Config.ARGB_8888)
        drawBoxs(Canvas(backBitmap), maxBoxs, positionIndex)
        if (isReverse) {
            canvas?.drawBitmap(Bitmap.createBitmap(backBitmap, 0, 0, backBitmap.width, backBitmap.height, sideInversion, false), 0f, 0f, null)
        } else {
            canvas?.drawBitmap(backBitmap, 0f, 0f, null)
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    fun setValue(position: Int) {
        this.positionIndex = position
        invalidate()
    }

    private fun Log(canvas: Canvas, text: String) {
        canvas.drawText(text, 0, text.length, 10f, 100f, paintLog)
    }

    private fun drawBoxs(canvas: Canvas, maxBoxs: Int, positionIndex: Int) {
        // 원위치 계산
        oval.left = abs(paddingVer - (oval.width() / 2)) + (positionIndex * oneTick)
        oval.right = oval.left + ovalSize
        // --------------------- 바탕 사각형
        var startBoxleft = paddingVer
        val startRect = RectF(startBoxleft, centerVerticalLine - (barHeight / 2), startBoxleft + oneBox, centerVerticalLine + (barHeight / 2))
        for (x in 0 until maxBoxs) {
            startRect.left = startBoxleft
            startRect.right = startBoxleft + oneBox
            //drawTickBox(canvas, startRect, if (highLightBoxNumber - 1 == x) paintHighlight else paintdefault)
            drawTickBox(canvas, startRect, if (crashOval(startRect, positionIndex, x)) paintHighlight else paintdefault)
            startBoxleft += oneBox

        }
        //----------------------- 사이에 흰색바
        startBoxleft = paddingVer
        val whiteBox = RectF(startBoxleft + oneBox, centerVerticalLine - (barHeight / 2), startBoxleft + oneBox + widthDp.toFloat(), centerVerticalLine + (barHeight / 2))
        for (x in 0 until maxBoxs - 1) {
            canvas.drawRect(whiteBox, paintWhiteBar)
            whiteBox.left += oneBox
            whiteBox.right = whiteBox.left + widthDp.toFloat()

        }
        // 원그리기
        canvas.drawOval(oval, paintHighlight)

    }

    /**
     * 충돌하면 바에 칠한다
     */
    private fun crashOval(rect: RectF, positionIndex: Int, x: Int): Boolean {
        val center = oval.left + oval.width() / 2
        if (positionIndex == 0 && x == 0) {
            paintHighlight.color = barColorArray[x]
            return true
        } else if (rect.left < center && rect.right >= center) {
            paintHighlight.color = barColorArray[x]
            return true
        }
        return false

    }

    private fun drawTickBox(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        drawTickBox(canvas, RectF(left, top, right, bottom), paint)
    }

    private fun drawTickBox(canvas: Canvas, rectf: RectF, paint: Paint) {
        canvas.drawRect(rectf, paint)
    }

    private fun DptoPX(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }


}