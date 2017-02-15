package com.rinno.apumanque.canvas;

import android.animation.Animator;
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
import android.widget.Toast;

import com.rinno.apumanque.models.Nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simaski on 31-01-17.
 */

public class DrawingView extends View {
    Path path;
    Paint paint;
    Paint paint2;
    float length;
    ObjectAnimator animator;

    public ArrayList<Float> coordx = new ArrayList<Float>();
    public ArrayList<Float> coordy = new ArrayList<Float>();
    public ArrayList<Float> coordz = new ArrayList<Float>();
    ArrayList arreglotemporal = new ArrayList();
    ArrayList arreglosegmentado = new ArrayList();
    ArrayList arreglorecorrido = new ArrayList();
    int cont;
    boolean bandera = true;


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


    public void init(List<Nodes> puntos,  ArrayList arregloRuta, ArrayList arregloStair)
    {
        arreglosegmentado.clear();
        coordx = new ArrayList<>();
        coordy = new ArrayList<>();

        animator = ObjectAnimator.ofFloat(this, "phase", 1.0f, 0.0f);
        animator.setDuration(5000);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);

        path = new Path();

        for(int i =0; i < puntos.size(); i++){
            coordx.add((float) puntos.get(i).getLocationX());
            coordy.add((float) puntos.get(i).getLocationY());
        }

        segmentarRuta(arregloRuta,arregloStair);


//        for(int i =0; i < arregloRuta.size(); i++){
//            int j  = 0;
//            //Log.e("TAG", "RECIBIDO: " + arregloRuta.get(i));
//            arreglotemporal.add(arregloRuta.get(i));
//            if(j <= arregloStair.size()) {
//                if (i == (int) arregloStair.get(j) || i == arregloRuta.size() - 1) {
//                    arreglosegmentado.add(arreglotemporal);
//                    arreglotemporal = new ArrayList();
//                }
//            }
//            j++;
//        }

        Log.e("TAG","ArregloTemporal: "+arreglosegmentado);


        for (int i = 0; i < arregloRuta.size(); i++){
            for(int j = 0; j < arreglosegmentado.size(); j++){
               arreglorecorrido = (ArrayList) arreglosegmentado.get(j);
                if(bandera) {
                    path.moveTo(coordx.get(0), coordy.get(0));
                    for (int k = 1; k < arreglorecorrido.size(); k++) {
                        path.lineTo(coordx.get(k), coordy.get(k));
                        cont = k;
                    }
                }else{
                    path.moveTo(coordx.get(cont), coordy.get(cont));
                    for (int k = cont+1; k < arreglorecorrido.size(); k++) {
                        path.lineTo(coordx.get(k), coordy.get(k));
                        cont = k;
                    }
                }
            }

        }

        Log.e("TAG","K: "+cont);

//        path.moveTo(coordx.get(0), coordy.get(0));
//
//        for (int i = 1; i < coordx.size(); i++){
//            path.lineTo(coordx.get(i), coordy.get(i));
//
//        }

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(getContext(), "Final", Toast.LENGTH_SHORT).show();
//                String idGrupo = "correcto";
//                EventBus.getDefault().postSticky(new Message(idGrupo));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });




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

    public void segmentarRuta(ArrayList arregloRuta, ArrayList arregloStair)
    {
        for(int i =0; i < arregloRuta.size(); i++){
            int j  = 0;
            //Log.e("TAG", "RECIBIDO: " + arregloRuta.get(i));
            arreglotemporal.add(arregloRuta.get(i));
            if(j <= arregloStair.size()) {
                if (i == (int) arregloStair.get(j) || i == arregloRuta.size() - 1) {
                    arreglosegmentado.add(arreglotemporal);
                    arreglotemporal = new ArrayList();
                }
            }
            j++;
        }
    }

}