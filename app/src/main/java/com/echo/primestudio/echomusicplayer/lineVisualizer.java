package com.echo.primestudio.echomusicplayer;

/**
 * Created by Rishabh Mishra on 12/26/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class lineVisualizer extends View {

    private byte[] visualizerValues ;
    double[] frequencyMagnitude ;
    int[] frequency ;
    double[] radiusCollector ;
    double[] angleCollector ;
    int[] plottingIndices = new int[20] ;
    private Rect container = new Rect();
    private Paint visualizerPaint  = new Paint () ;
    int maximumRadius = 5 , innerRadius = 50 , centerX = 0 , centerY = 0  , pointX = 0 , pointY = 0  ;
    double max = 0 , radius = 0 , angle = 0 ;

    public lineVisualizer(Context context) {
        super(context);
        init();
    }

    public lineVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public lineVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        visualizerValues = null;

        visualizerPaint.setStrokeWidth(20f);
        visualizerPaint.setAntiAlias(true);
        visualizerPaint.setColor(getResources().getColor(R.color.pink));

        innerRadius = Main.albumArtNowPlayingCurtainWidth / 2 ;

    }

    public void updateVisualizer(byte[] bytes) {
        visualizerValues = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Initialisation
        max = 0;

        container.set(0 , getHeight() , getWidth() , 0 );

        maximumRadius = container.width() /2 ;
        innerRadius = container.width() / 4 ;

        centerY = container.centerX() ;
        centerX = container.centerY() ;


        super.onDraw(canvas);
        if (visualizerValues == null) {
            return;
        }

        frequencyMagnitude = new double[visualizerValues.length / 2] ;
        frequency = new int[visualizerValues.length / 2] ;

        for (int i= 0 ; i <= ( visualizerValues.length / 2 ) -1 ; i++){
            int re = visualizerValues[2*i];
            int im = visualizerValues[2*i+1];
            frequencyMagnitude[i] = Math.sqrt(re*re+im*im);
            frequency[i] = i * ( Main.songVisualizer.getSamplingRate() / 1000 ) / visualizerValues.length ;
            if (frequencyMagnitude[i] > max)
                max = frequencyMagnitude[i] ;
        }

        for (int i = 0 ; i < frequency.length ; i++){
            Log.d("FREQUENCY ANALYSIS" , "Frequency : " + frequency[i] + "Frequency Magnitude : " + frequencyMagnitude[i] ) ;
        }


        radiusCollector = new double[ ( frequencyMagnitude.length / 4 ) - 1 ] ;
        angleCollector = new double[ ( frequencyMagnitude.length / 4 ) - 1 ] ;

        for (int i = 0 ; i < ( frequencyMagnitude.length / 8 ) - 1 ; i++){


            if ( frequencyMagnitude[i] > ( max / 5 ) ) {

                //Angle initiation
                angle = 105 + (i * 5.15625) ;

                //Radius Initiation
                radius = frequencyMagnitude[i];
                radius = ( ( maximumRadius - innerRadius ) * ( radius / max ) ) + innerRadius ;

                Log.d("ECHO VISUALIZATION" , "Radius : " + radius + "Inner Radius : " + innerRadius ) ;

                radiusCollector [i] = radius ;
                angleCollector [i] = angle ;


                int minimumRadiusIndexInPlottingIndices = 0 ;

                for (int j = 1 ; j < 20 ; j++){
                    if (radiusCollector[plottingIndices[j]] < radiusCollector[plottingIndices[minimumRadiusIndexInPlottingIndices]]) {
                        minimumRadiusIndexInPlottingIndices = j ;
                    }
                }

                if (radiusCollector[plottingIndices[minimumRadiusIndexInPlottingIndices]] < radius){
                    plottingIndices[minimumRadiusIndexInPlottingIndices] = i ;
                }


            }

        }


        Arrays.sort(plottingIndices);


        for (int i = 0 ; i < 20 ; i++){


            radius = radiusCollector[plottingIndices[i]] ;
            angle = angleCollector[plottingIndices[i]] ;

            Log.d("Echo Visualization" , "Index Plotted : " + plottingIndices[i] + "AT : " + i ) ;

            //Points
            pointX = centerX + (int) (radius * Math.cos(Math.toRadians(angle)) / 1.2);
            pointY = centerY + (int) (radius * Math.sin(Math.toRadians(angle)) / 1.2);

            int innerX = centerX + (int) (innerRadius * Math.cos(Math.toRadians(angle)));
            int innerY = centerY + (int) (innerRadius * Math.sin(Math.toRadians(angle)));

            canvas.drawLine(innerX, innerY, pointX, pointY, visualizerPaint);



        }





    }


}
