package com.example.rpiplayer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class NubView : View {
    private val LOG = "NubView"
    private var paint: Paint? = null
    private var backgroundPaint: Paint? = null
    private var centerX = 0f
    private var centerY = 0f
    private val radius = 500f
    private val lineLength = 500f
    private val nubRadius = 100f
    private var touching = false
    private var nubX = 0f
    private var nubY = 0f
    private var velocityX = 0f
    private var velocityY = 0f
    private val springConstant = 0.01f
    private val dampingFactor = 0.95f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.color = Color.BLUE
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeWidth = 5f

        backgroundPaint = Paint()
        backgroundPaint!!.color = Color.GRAY
        backgroundPaint!!.style = Paint.Style.FILL
        backgroundPaint!!.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        nubX = centerX
        nubY = centerY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint!!);
        canvas.drawCircle(centerX, centerY, radius, paint!!);
        canvas.drawLine(centerX, centerY, nubX, nubY, paint!!)
        canvas.drawCircle(nubX, nubY, nubRadius, paint!!)

        if (!touching) {
            nubX = (nubX + centerX) / 2;
            nubY = (nubY + centerY) / 2;

            if (abs(nubX - centerX) < 1) {
                nubX = centerX
            }
            if (abs(nubY - centerY) < 1) {
                nubY = centerY
            }

            if (nubX != centerX || nubY != centerY) {
                invalidate()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val distance =
            sqrt(((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)).toDouble())
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
touching = true
            }
            MotionEvent.ACTION_UP -> {
                touching = false
            }
        }

        if (distance <= radius) {
            nubX = x
            nubY = y
        } else {
            val angle = atan2(y - centerY, x - centerX)
            nubX = centerX + radius * cos(angle)
            nubY = centerY + radius * sin(angle)
        }

        invalidate()
        return true
    }
}