/**
* Basado en https://github.com/GautamChibde/android-audio-visualizer/wiki/Circle-Bar-Visualizer
*/
package com.spreadyourmusic.spreadyourmusic.soundvisualizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.audiofx.Visualizer
import android.util.AttributeSet
import android.view.View

class CircleSoundVisualizer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs) {

    private var bytes: ByteArray? = null
    private val paint: Paint
    private val circlePaint: Paint
    private var visualizer: Visualizer? = null
    private var color: Int
    private var points: FloatArray? = null
    private var radius: Float
    private var centerX: Float
    private var centerY: Float

    init {
        bytes = null
        paint = Paint()
        visualizer = null
        color = Color.WHITE
        points = null
        circlePaint = Paint()
        radius = -1f
        centerX = -1f
        centerY = -1f
        paint.style = Paint.Style.STROKE
    }

    /**
     * Set color to visualizer with color resource id.
     */
    fun setColor(color: Int) {
        this.color = color
        this.paint.color = this.color
    }

    /**
     * Set player to visualizer
     */
    fun setPlayer(audioSessionId: Int) {
        visualizer = Visualizer(audioSessionId)
        visualizer!!.captureSize = Visualizer.getCaptureSizeRange()[1]

        visualizer!!.setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
            override fun onWaveFormDataCapture(visualizer: Visualizer, bytes: ByteArray,
                                               samplingRate: Int) {
                this@CircleSoundVisualizer.bytes = bytes
                invalidate()
            }

            override fun onFftDataCapture(visualizer: Visualizer, bytes: ByteArray,
                                          samplingRate: Int) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false)

        visualizer!!.enabled = true
    }

    /**
     * Set release player from visualizer
     */
    fun releasePlayer() {
        if (visualizer != null)
            visualizer!!.release()
    }

    override fun onDraw(canvas: Canvas) {
        if (radius == -1f) {
            val availableWidth = (width - paddingLeft - paddingRight).toDouble()
            val availableHeight = (height - paddingTop - paddingBottom).toDouble()

            val totalSideLength = Math.min(availableWidth, availableHeight)
            val internalSideLength = totalSideLength * 0.65

            val left = paddingLeft.toDouble() + (availableWidth - totalSideLength) / 2.0 + (totalSideLength - internalSideLength) / 2.0
            val top = paddingTop.toDouble() + (availableHeight - totalSideLength) / 2.0 + (totalSideLength - internalSideLength) / 2.0

            val localRadius = internalSideLength / 2.0
            radius = localRadius.toFloat()
            val circumference = 2.0 * Math.PI * localRadius

            paint.strokeWidth = (circumference / 120.0).toFloat()
            circlePaint.style = Paint.Style.STROKE
            circlePaint.strokeWidth = 4f

            centerX = (left + internalSideLength / 2.0).toFloat()
            centerY = (top + internalSideLength / 2.0).toFloat()

        }
        circlePaint.color = color

        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        if (bytes != null) {
            if (points == null || points!!.size < bytes!!.size * 4) {
                points = FloatArray(bytes!!.size * 4)
            }
            var angle = 0.0

            var i = 0
            while (i < 120) {
                val x = Math.ceil(i * 8.5).toInt()
                val t = (-Math.abs(bytes!![x].toInt()) + 128).toByte() * (canvas.height / 4) / 128

                points!![i * 4] = (centerX + radius * Math.cos(Math.toRadians(angle))).toFloat()

                points!![i * 4 + 1] = (centerY + radius * Math.sin(Math.toRadians(angle))).toFloat()

                points!![i * 4 + 2] = (centerX + (radius + t) * Math.cos(Math.toRadians(angle))).toFloat()

                points!![i * 4 + 3] = (centerY + (radius + t) * Math.sin(Math.toRadians(angle))).toFloat()
                i++
                angle += 3.0
            }

            canvas.drawLines(points!!, paint)
        }
        super.onDraw(canvas)
    }

}
