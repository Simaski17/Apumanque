package com.rinno.apumanque;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.rinno.apumanque.models.Nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvInicio)
    TextView tvInicio;
    @BindView(R.id.etInicio)
    EditText etInicio;
    @BindView(R.id.tvFin)
    TextView tvFin;
    @BindView(R.id.etFin)
    EditText etFin;
    @BindView(R.id.btIr)
    Button btIr;

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    private ArrayList arregloA = new ArrayList();
    private ArrayList arregloB = new ArrayList();
    private ArrayList arregloCosto = new ArrayList();
    private ArrayList arregloIdRuta = new ArrayList();
    private ArrayList arregloLocationX = new ArrayList();
    private ArrayList arregloLocationY = new ArrayList();
    private ArrayList arregloLocationZ = new ArrayList();
    private ArrayList arregloRutaFinal = new ArrayList();
    private List<String> stockList = new ArrayList<String>();
    private String joined;
    private String noComa;
    private int start;
    private int end;

    //custom drawing view
    private DrawingView drawView;
    private DrawingPointView drawViewPoint;

    //Button btPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                    arregloLocationX.add(String.valueOf(nod.getLocationX()));
                    arregloLocationY.add(String.valueOf(nod.getLocationY()));
                    arregloLocationZ.add(String.valueOf(nod.getLocationZ()));
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

            }
        });
    }

    @OnClick(R.id.btIr)
    public void onClick() {

        start = Integer.parseInt(etInicio.getText().toString());
        end = Integer.parseInt(etFin.getText().toString());
        calcularRuta(start,end);

    }

    public void calcularRuta(int a ,int b){
        Set<String> linkedHashSet = new LinkedHashSet<String>();

        for (int i = 0; i < arregloA.size(); i++){
            Graph.Edge<String> ed = new Graph.Edge<String>((int) arregloCosto.get(i),  vertices.get((int) arregloA.get(i)), vertices.get((int) arregloB.get(i)));
            edges.add(ed);
        }

        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
        Astar astar=new Astar();

        //////GENERAR RUTA FINAL//////////////////////////////
        stockList = astar.aStar(graph, vertices.get(a), vertices.get(b));
        joined = TextUtils.join(",", stockList);
        noComa = joined.toString().replace(",","");
        String[] parts = noComa.split("(?!^)");
        Collections.addAll(arregloRutaFinal, parts);
        Log.e("TAG","Arreglo antes de ordenar"+arregloRutaFinal);
        linkedHashSet.addAll(arregloRutaFinal);
        arregloRutaFinal.clear();
        arregloRutaFinal.addAll(linkedHashSet);
        ///////////////////////////////////////////////////////////////////
        Log.e("TAG","Arreglo Final: " + Arrays.toString(parts));
        Log.e("TAG","Ruta Final: " + arregloRutaFinal);

        etInicio.setText("");
        etFin.setText("");
        arregloRutaFinal = new ArrayList();
        arregloRutaFinal.clear();
    }


}
