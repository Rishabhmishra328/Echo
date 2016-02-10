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

public class waveVisualizer extends View {

    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();
    public static Path visualizerPath = new Path() ;

    public waveVisualizer(Context context) {
        super(context);
        init();
    }

    public waveVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public waveVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(1f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(getResources().getColor(R.color.pink));
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
        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        visualizerPath.reset();

        mRect.set(0, 0, getWidth(), getHeight());
        visualizerPath.moveTo(0, mRect.height());

        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * 4 + 1] = mRect.height() / 2
                    + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;

            mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * 4 + 3] = mRect.height() / 2
                    + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
                    / 128;
            visualizerPath.lineTo(mPoints[i * 4 + 2],mPoints[i * 4 + 3]);
        }

        visualizerPath.lineTo(mRect.width(),mRect.height());

//        visualizerPath.moveTo(0,0);

        visualizerPath.close();

        canvas.drawPath(visualizerPath,mForePaint);
//        canvas.drawLines(mPoints, mForePaint);
    }



}
