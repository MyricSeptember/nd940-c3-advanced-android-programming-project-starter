package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var progressWidth = 0.0f
    private var progressSweepAngle = 0.0f


    private val buttonText get() = context.getString(buttonState.buttonText).toUpperCase(Locale.ROOT)


    private var valueAnimator = ValueAnimator()

    var value = 0.0f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            is ButtonState.Downloading -> {
                downloadAnim()
            }
            is ButtonState.Loading -> {
                downloadAnim()
            }

            is ButtonState.Completed -> {

            }
        }
    }

    private var backgroundButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimary)
    }

    private var backgroundIsDownloadingButtonPain = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorPrimaryDark)
    }

    private val buttonTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER
    }

    private var progressCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.colorAccent)
    }


    init {
        val array = context.theme.obtainStyledAttributes(attrs,
                R.styleable.LoadingButton,
                0, 0
        )
        buttonTextPaint.color = array.getColor(R.styleable.LoadingButton_btnTextColor, Color.WHITE)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val buttonWidth = measuredWidth.toFloat()
        val buttonHeight = measuredHeight.toFloat()


        canvas?.drawRect(0f, 0f, buttonWidth, buttonHeight, backgroundButtonPaint)





        if (buttonState == ButtonState.Loading || buttonState == ButtonState.Downloading) {
            canvas?.drawRect(0f, 0f, width * progressWidth / 100, heightSize.toFloat(), backgroundIsDownloadingButtonPain)


        }

        val textHeight: Float = buttonTextPaint.descent() - buttonTextPaint.ascent()
        val textOffset: Float = textHeight / 2 - buttonTextPaint.descent()
        canvas?.drawText(
                buttonText,
                widthSize.toFloat() / 2,
                heightSize.toFloat() / 2 + textOffset,
                buttonTextPaint
        )

        canvas?.drawArc(
                width - height * 0.75f,
                height * 0.25f,
                width - height * 0.25f,
                height * 0.75f,
                270f, 360 * progressWidth / 100, true, progressCirclePaint
        )
    }

    private fun downloadAnim() {
        valueAnimator = ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 3000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE

            addUpdateListener {
                progressWidth = it.animatedValue as Float
                invalidate()
            }
        }
        valueAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}