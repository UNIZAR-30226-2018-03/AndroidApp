package com.spreadyourmusic.spreadyourmusic.soundvisualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

public class CircleSoundVisualizer extends View {

    private byte[] bytes;
    private Paint paint, circlePaint;
    private Visualizer visualizer = null;
    private int color;
    private float[] points = null;
    private float radius, centerX, centerY;

    public CircleSoundVisualizer(Context context) {
        this(context, null, 0);
    }

    public CircleSoundVisualizer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleSoundVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        bytes = null;
        paint = new Paint();
        visualizer = null;
        color = Color.BLUE;
        points = null;
        circlePaint = new Paint();
        radius = -1;
        centerX = -1;
        centerY = -1;
        paint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Set color to visualizer with color resource id.
     *
     * @param color color resource id.
     */
    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(this.color);
    }

    public void setPlayer(int audioSessionId) {
        visualizer = new Visualizer(audioSessionId);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                CircleSoundVisualizer.this.bytes = bytes;
                invalidate();
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);

        visualizer.setEnabled(true);
    }

    public void releasePlayer() {
        if (visualizer != null)
            visualizer.release();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius == -1) {
            double availableWidth = (double) (getWidth() - getPaddingLeft() - getPaddingRight());
            double availableHeight = (double) (getHeight() - getPaddingTop() - getPaddingBottom());

            double totalSideLength = Math.min(availableWidth, availableHeight);
            double internalSideLength = totalSideLength * 0.65;

            double left = getPaddingLeft() + (availableWidth - totalSideLength) / 2.0 + (totalSideLength - internalSideLength) / 2.0;
            double top = getPaddingTop() + (availableHeight - totalSideLength) / 2.0 + (totalSideLength - internalSideLength) / 2.0;

            double localRadius = internalSideLength / 2.0;
            radius = (float) localRadius;
            double circumference = 2.0 * Math.PI * localRadius;

            paint.setStrokeWidth((float) (circumference / 120.0));
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeWidth(4);

            centerX = (float) (left + (internalSideLength / 2.0));
            centerY = (float) (top + (internalSideLength / 2.0));

        }
        circlePaint.setColor(color);

        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        if (bytes != null) {
            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }
            double angle = 0;

            for (int i = 0; i < 120; i++, angle += 3) {
                int x = (int) Math.ceil(i * 8.5);
                int t = ((byte) (-Math.abs(bytes[x]) + 128)) * (canvas.getHeight() / 4) / 128;

                points[i * 4] = (float) (centerX
                        + radius
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 1] = (float) (centerY
                        + radius
                        * Math.sin(Math.toRadians(angle)));

                points[i * 4 + 2] = (float) (centerX
                        + (radius + t)
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 3] = (float) (centerY
                        + (radius + t)
                        * Math.sin(Math.toRadians(angle)));
            }

            canvas.drawLines(points, paint);
        }
        super.onDraw(canvas);
    }

}
