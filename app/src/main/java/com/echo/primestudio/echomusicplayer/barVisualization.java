package com.echo.primestudio.echomusicplayer;

/**
 * Created by Rishabh Mishra on 12/26/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class barVisualization extends View {

    private byte[] mBytes;
    private Rect mRect = new Rect();
    public static Paint barVizPaint = new Paint();
    public static Path visualizerPath = new Path();
    public static Path[] paths = new Path[1024];

    public barVisualization(Context context) {
        super(context);
        init();
    }

    public barVisualization(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public barVisualization(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBytes = null;
        barVizPaint.setAntiAlias(true);
        barVizPaint.setColor(getResources().getColor(R.color.pink));
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {
            return;
        }
//        if (mPoints == null || mPoints.length < mBytes.length * 4) {
//            mPoints = new float[mBytes.length * 4];
//        }

//        visualizerPath.reset();


        for (int i = 0; i < paths.length; i++) {
            if (paths[i] != null) {
                paths[i].reset();
            }
        }


        mRect.set(0, 0, getWidth(), getHeight());
//        visualizerPath.moveTo(0, mRect.height());

        int totalWidth = mRect.width();
        int totalHeight = mRect.height();
        float strokeWidth = (float) (totalWidth * 0.8) / 1024;
        barVizPaint.setStrokeWidth(strokeWidth);

        for (int i = 0; i < mBytes.length; i++) {
            mBytes[i] = (byte) (mBytes[i] + 256);
            mBytes[i] = (byte) ((byte) (mBytes[i] * totalHeight) / 512);
        }

        float emptyPathWidth = (float) (totalWidth * 0.2) / 1024;
        float pathWidthTracker = emptyPathWidth;


        for (int i = 0; i < mBytes.length; i++) {

            paths[i] = new Path();

            paths[i].moveTo(pathWidthTracker, 0);
            paths[i].lineTo(pathWidthTracker, mBytes[i]);

            pathWidthTracker = emptyPathWidth + strokeWidth;

        }

//        visualizerPath.lineTo(mRect.width(), mRect.height());

//        visualizerPath.moveTo(0,0);

//        visualizerPath.close();

        for (int i = 0; i < paths.length; i++) {
            canvas.drawPath(paths[i], barVizPaint);
        }
//        canvas.drawLines(mPoints, barVizPaint);
    }


}
