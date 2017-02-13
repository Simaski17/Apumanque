package com.rinno.apumanque.canvas;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by simaski on 31-01-17.
 */

public class DrawingView extends View {
    Path path;
    Paint paint;
    Paint paint2;
    float length;
    ObjectAnimator animator;

    public String[] parts;
    public ArrayList<Float> coordx = new ArrayList<Float>();
    public ArrayList<Float> coordy = new ArrayList<Float>();
    public ArrayList<Float> coordz = new ArrayList<Float>();
    ArrayList arreglotemporal = new ArrayList();
    ArrayList arreglosegmentado = new ArrayList();
    ArrayList arreglorecorrido = new ArrayList();
    int cont;


    public DrawingView(Context context)
    {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    class Puntos{
        float x, y, z;
        Puntos(float _x, float _y, float _z){
            x = _x;
            y = _y;
            z = _z;
        }
    }

    /*Puntos[] myPath = { new Puntos(300, 100, 0), new Puntos(400, 200, 0), new Puntos(500, 100, 0), new Puntos(250, 350, 0), new Puntos(700, 200, 1), new Puntos(700, 100, 0),
            new Puntos(550, 350, 0), new Puntos(200, 200, 0)};*/

    Puntos[] myPath = { new Puntos(540, 100, 0), new Puntos(440, 200, 0), new Puntos(500, 300, 0), new Puntos(300, 400, 0), new Puntos(470, 450, 0), new Puntos(550, 450, 0),
            new Puntos(380, 500, 0), new Puntos(500, 530, 1),new Puntos(750, 550, 0),new Puntos(200, 650, 0),new Puntos(480, 680, 0),new Puntos(540, 650, 0),new Puntos(650, 650, 0),
            new Puntos(850, 680, 0), new Puntos(250, 720, 0),new Puntos(400, 700, 1),new Puntos(540, 720, 0),new Puntos(150, 830, 0),new Puntos(360, 800, 0),new Puntos(600, 870, 0),
            new Puntos(900, 800, 0), new Puntos(50, 930, 0),new Puntos(360, 980, 0),new Puntos(480, 950, 0), new Puntos(750, 940, 0),new Puntos(650, 1100, 0)};


    public void init(String partes)
    {

        //myPath = new float[][]{{300,100,0},{400, 200, 0},{500, 100, 0},{250, 350, 0},{700, 200, 1},{700, 100, 0},{550, 350, 0},{200, 200, 0}};
        animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f);
        animator.setDuration(4000);

        parts =partes.split("->"); // escape .

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        path = new Path();



        for(int i =0; i < parts.length; i++){
            Log.e("TAG","RECIBIDO: "+parts[i]);
            coordx.add(myPath[Integer.parseInt(parts[i])].x);
            coordy.add(myPath[Integer.parseInt(parts[i])].y);
            coordz.add(myPath[Integer.parseInt(parts[i])].z);
            arreglotemporal.add(parts[i]);
            if(coordz.get(i) == 1.0 || i == parts.length-1){
                arreglosegmentado.add(arreglotemporal);
                arreglotemporal = new ArrayList();
            }
        }

        Log.e("TAG","ARREGLO SEGMENTADO: "+cont);




        path.moveTo(coordx.get(0), coordy.get(0));

        for (int i = 1; i < coordx.size(); i++){
            path.lineTo(coordx.get(i), coordy.get(i));

        }

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        animator.start();

    }

    //is called by animtor object
    public void setPhase(float phase)
    {
        //Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw

    }

    private PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] {
                pathLength, pathLength
        },
                Math.max(phase * pathLength, offset));
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        c.drawPath(path, paint);
        c.drawCircle(coordx.get(0), coordy.get(0), 30, paint2);
    }

}