package com.rinno.apumanque;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rinno.apumanque.algoritmo.Astar;
import com.rinno.apumanque.algoritmo.Graph;
import com.rinno.apumanque.canvas.DrawingPointView;
import com.rinno.apumanque.canvas.DrawingView;
import com.rinno.apumanque.models.Edges;
import com.rinno.apumanque.models.Message;
import com.rinno.apumanque.models.Nodes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapaActivity extends AppCompatActivity {


    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    @BindView(R.id.btEscalera1)
    ImageView btEscalera1;
    @BindView(R.id.btEscalera2)
    ImageView btEscalera2;
    @BindView(R.id.btEscalera3)
    ImageView btEscalera3;
    private ArrayList arregloA = new ArrayList();
    private ArrayList arregloB = new ArrayList();
    private ArrayList arregloCosto = new ArrayList();
    private ArrayList arregloIdRuta = new ArrayList();
    private ArrayList arregloLocationX = new ArrayList();
    private ArrayList arregloLocationY = new ArrayList();
    private ArrayList arregloStair = new ArrayList();
    private ArrayList arregloType = new ArrayList();
    private ArrayList arregloRutaFinal = new ArrayList();

    private List<String> stockList = new ArrayList<String>();
    List<Nodes> puntos = new ArrayList<Nodes>();
    private int start;
    private int end;
    int idCont;

    //custom drawing view
    private DrawingView drawView;
    private DrawingPointView drawViewPoint;

    //Button btPrueba;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        ButterKnife.bind(this);

        datos = this.getIntent().getExtras();
        idCont = 0;


        //ESCALERAS COORD XY
        btEscalera1.setEnabled(false);
        btEscalera2.setEnabled(false);
        btEscalera2.setEnabled(false);
        btEscalera1.setX(700);
        btEscalera1.setY(500);
        btEscalera2.setX(600);
        btEscalera2.setY(600);
        btEscalera3.setX(800);
        btEscalera3.setY(630);

        //get drawing view
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawViewPoint = (DrawingPointView) findViewById(R.id.drawingPoint);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i = 1; i <= dataSnapshot.child("Nodes").getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child("Nodes").child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId().trim());
                    arregloIdRuta.add(nod.getId());
                    arregloLocationX.add(Float.parseFloat(String.valueOf(nod.getLocationX())));
                    arregloLocationY.add(Float.parseFloat(String.valueOf(nod.getLocationY())));
                    //arregloLocationZ.add(Float.parseFloat(String.valueOf(nod.getLocationZ())));
                    arregloType.add(String.valueOf(nod.getType()));
                    vertices.add(a);
                }

                for (int i = 1; i <= dataSnapshot.child("Edges").getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child("Edges").child(String.valueOf(i)).getValue(Edges.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getInicio().trim());
                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getFin().trim());

                    for (int l = 0; l < vertices.size(); l++) {
                        if (vertices.get(l).equals(a)) {
                            arregloA.add(l);
                        }
                    }

                    for (int l = 0; l < vertices.size(); l++) {
                        if (vertices.get(l).equals(b)) {
                            arregloB.add(l);
                        }
                    }
                    arregloCosto.add(edg.getCosto());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG","ERROR: "+databaseError);
            }
        });

        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    idCont = 0;
                    drawView.setVisibility(View.GONE);
                    drawViewPoint.setVisibility(View.GONE);

                    start = datos.getInt("start");
                    end = datos.getInt("end");
                    calcularRuta(start, end);

                    if (puntos.size() > 0) {
                        drawView.setVisibility(View.VISIBLE);
                        drawViewPoint.setVisibility(View.VISIBLE);
                        //Log.e("TAG","RUTA FINAL: "+arregloRutaFinal);

                        drawView.init(puntos, arregloRutaFinal, arregloStair, idCont);
                        drawViewPoint.init(puntos, idCont);
                    }
            }
        }, 100);

    }

    @OnClick({R.id.btEscalera1, R.id.btEscalera2,R.id.btEscalera3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btEscalera1:
                btEscalera1.setImageResource(R.drawable.escalerablanca);
                stairEvent();
                break;
            case R.id.btEscalera2:
                stairEvent();
                break;
            case R.id.btEscalera3:
                stairEvent();
                break;
        }
    }

    public void stairEvent(){
        Log.e("TAG", "FUNCIONA");
        drawView.setVisibility(View.GONE);
        drawViewPoint.setVisibility(View.GONE);

        drawView.setVisibility(View.VISIBLE);
        drawViewPoint.setVisibility(View.VISIBLE);

        drawView.init(puntos, arregloRutaFinal, arregloStair, idCont);
        drawViewPoint.init(puntos, idCont);
    }

    public void calcularRuta(int a, int b) {
        Set<String> linkedHashSet = new LinkedHashSet<String>();

        for (int i = 0; i < arregloA.size(); i++) {
            Graph.Edge<String> ed = new Graph.Edge<String>((int) arregloCosto.get(i), vertices.get((int) arregloA.get(i)), vertices.get((int) arregloB.get(i)));
            edges.add(ed);
        }

        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
        Astar astar = new Astar();
        arregloRutaFinal = new ArrayList();
        arregloRutaFinal.clear();
        arregloStair = new ArrayList();
        arregloStair.clear();

        //////GENERAR RUTA FINAL//////////////////////////////
        stockList = astar.aStar(graph, vertices.get(a), vertices.get(b));
        Log.e("TAG","Stocklist "+stockList);
        String ss = "";
        for (int i = 0; i < stockList.size(); i++) {
            ss = String.valueOf(stockList.get(i));
            String[] sp = ss.split(",");
            arregloRutaFinal.add(sp[0]);
            arregloRutaFinal.add(sp[1]);

        }
        //Log.e("TAG","INICIO "+arregloRutaFinal);
        linkedHashSet.addAll(arregloRutaFinal);
        arregloRutaFinal.clear();
        arregloRutaFinal.addAll(linkedHashSet);
        ///////////////////////////////////////////////////////////////////

        puntos = new ArrayList<>();
        for (int i = 0; i < arregloRutaFinal.size(); i++) {
            for (int j = 0; j < arregloIdRuta.size(); j++) {
                if (arregloRutaFinal.get(i).equals(arregloIdRuta.get(j))) {
                    if (arregloType.get(j).equals("stair")) {
                        //Log.e("TAG","ENCONTRADO EN: "+i);
                        arregloStair.add(i);
                    }
                    puntos.add(new Nodes((float) arregloLocationX.get(j), (float) arregloLocationY.get(j)));
                }

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(Message event) {
        idCont = event.getCont();
        if (idCont > 0) {
            btEscalera1.setEnabled(true);
            btEscalera1.setImageResource(R.drawable.escaleraamarilla);
            btEscalera2.setEnabled(true);
            btEscalera3.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

