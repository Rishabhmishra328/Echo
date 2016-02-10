package com.echo.primestudio.echomusicplayer;

/**
 * Created by Rishabh Mishra on 12/26/2015.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class echoVisualizer extends View {

    private byte[] visualizerValues ;
    double[] frequencyMagnitude ;
    int[] frequency ;
    double[] radiusCollector ;
    double[] angleCollector ;
    int[] plottingIndices = new int[40] ;
    private Rect container = new Rect();
    public Path visualizerPath = new Path() ;
    private Paint visualizerPaint  = new Paint () ;
    int mean =0 , min = 0 , maximumRadius = 5 , innerRadius = 50 , centerX = 0 , centerY = 0  , pointX = 0 , pointY = 0 , maxIndex = 0 , pathCalculator = 0 ;
    double max = 0 , radius = 0 , angle = 0 , referenceHeight = 0 , maximumAngleDeviation = 0 ;

    public echoVisualizer(Context context) {
        super(context);
        init();
    }

    public echoVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public echoVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        visualizerValues = null;

        visualizerPaint.setStrokeWidth(2f);
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
        mean = 0;
        max = 0;
        min = 0;

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
        }


        //Initializing Path
        visualizerPath.moveTo( centerX , centerY );

        radiusCollector = new double[ ( frequencyMagnitude.length / 2 ) - 1 ] ;
        angleCollector = new double[ ( frequencyMagnitude.length / 2 ) - 1 ] ;

        for (int i = 0 ; i < ( frequencyMagnitude.length / 8 ) - 1 ; i++){


            if ( frequencyMagnitude[i] > ( max / 5 ) ) {

                //Angle initiation
                angle = i < 49 ? 105 + (i * 5.2) : (i-49) * 5.2;

                //Radius Initiation
                radius = frequencyMagnitude[i];
                radius = ( ( maximumRadius - innerRadius ) * ( radius / max ) ) + innerRadius ;


                radiusCollector [i] = radius ;
                angleCollector [i] = angle ;


                int minimumRadiusIndexInPlottingIndices = 0 ;

                if (i < 40){
                    plottingIndices[i] = i ;
                }

                for (int j = 0 ; j < 40 ; j++){

                    if (radiusCollector[plottingIndices[j]] < radiusCollector[plottingIndices[minimumRadiusIndexInPlottingIndices]]) {
                        minimumRadiusIndexInPlottingIndices = j ;
                    }

                }

                if (i >= 40 && radiusCollector[minimumRadiusIndexInPlottingIndices] < radiusCollector[i]){
                    plottingIndices[minimumRadiusIndexInPlottingIndices] = i ;
                }

            }

        }


        Arrays.sort(plottingIndices);

        //Proximity Test
        int plottingIndexStartingPosition = 0 ;
        for ( int i = 1 ; i < 40 ; i+=2 ){

            if ( plottingIndices[i -1] > ( plottingIndices[i] - 10 ) ) {
                plottingIndices[i-1] = 0 ;
                plottingIndexStartingPosition++ ;
            }

        }

        Arrays.sort(plottingIndices);

        visualizerPath.moveTo(centerX + (int) ( innerRadius * Math.cos(105) ) , centerY + (int) ( innerRadius * Math.sin(105) ) ) ;

        for (int i = plottingIndexStartingPosition + 1 ; i < 40 ; i++){

            float controllerOneX , controllerOneY , controllerTwoX , controllerTwoY ;

            radius = radiusCollector[plottingIndices[i]] ;
            angle = angleCollector[plottingIndices[i]] ;

            //Calculating Controllers
            maximumAngleDeviation = 15 ;
            referenceHeight = ( 0.6 * ( (maximumRadius - innerRadius) * (radius / maximumRadius) ) ) ;
            double referenceAngle =  ( radius / maximumRadius ) * maximumAngleDeviation ;

            double midRadius = innerRadius ;
            float midPrevX , midPrevY , midNextX , midNextY;

            //Points
            pointX = centerX + (int) (radius * Math.cos(Math.toRadians(angle)));
            pointY = centerY + (int) (radius * Math.sin(Math.toRadians(angle)));

            //Mid point from previous
            midPrevX = (float) ( centerX + ( midRadius * Math.cos(Math.toRadians(angle - referenceAngle)) ) ) ;
            midPrevY = (float) ( centerY + ( midRadius * Math.sin(Math.toRadians(angle - referenceAngle)) ) ) ;

            midNextX = (float) ( centerX + ( midRadius * Math.cos(Math.toRadians(angle + referenceAngle)) ) ) ;
            midNextY = (float) ( centerY + ( midRadius * Math.sin(Math.toRadians(angle + referenceAngle)) ) ) ;

            controllerOneX = (float) ( midPrevX + ( referenceHeight * Math.cos(Math.toRadians(angle + (referenceAngle / 2))) ) ) ;
            controllerOneY = (float) ( midPrevY + ( referenceHeight * Math.sin(Math.toRadians(angle + (referenceAngle / 2))) ) ) ;
            controllerTwoX = (float) ( midNextX + ( referenceHeight * Math.cos(Math.toRadians(angle - (referenceAngle / 2))) ) );
            controllerTwoY = (float) ( midNextY + ( referenceHeight * Math.sin(Math.toRadians(angle - (referenceAngle / 2)) ) ) ) ;

            //Path Construction
            visualizerPath.moveTo(midPrevX, midPrevY);
            visualizerPath.cubicTo(controllerOneX, controllerOneY, controllerTwoX, controllerTwoY, midNextX, midNextY);
            visualizerPath.lineTo(midPrevX, midPrevY);

        }



        //Closing Path
        visualizerPath.moveTo(centerX, centerY);

        visualizerPath.close() ;


        Path standByPath ;
        standByPath = visualizerPath ;
        canvas.drawPath(standByPath , visualizerPaint) ;

        for (int i = plottingIndexStartingPosition + 1 ; i < 40 ; i++){

            radius = radiusCollector[plottingIndices[i]] ;
            angle = angleCollector[plottingIndices[i]] ;

            //Calculating Controllers
            maximumAngleDeviation = 40 ;
            referenceHeight = ( 1.25 * ( (maximumRadius - innerRadius) * (radius / maximumRadius) ) ) ;

            //Points
            pointX = centerX + (int) (innerRadius * Math.cos(Math.toRadians(angle)));
            pointY = centerY + (int) (innerRadius * Math.sin(Math.toRadians(angle)));

            //Path Construction
            visualizerPath.moveTo(pointX, pointY);

        }

        visualizerPath.reset();

    }


}
