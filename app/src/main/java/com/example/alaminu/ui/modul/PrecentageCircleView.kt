package com.example.alaminu.ui.modul

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PrecentageCircleView : View {
    private var percentage: Float = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.rgb(0,128,228) // Change color as needed
    }

    fun setPercentage(percentage: Float) {
        this.percentage = percentage
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2f - paint.strokeWidth / 2f // Adjust for stroke width

        val rectF = RectF(
            paint.strokeWidth / 2f,
            paint.strokeWidth / 2f,
            width - paint.strokeWidth / 2f,
            height - paint.strokeWidth / 2f
        )

        // Draw the circle with adjusted stroke width
        canvas.drawArc(rectF, -90f, 360f * (percentage / 100), false, paint)
    }
}
