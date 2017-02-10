package com.rinno.apumanque;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rinno.apumanque.algoritmo.Astar;
import com.rinno.apumanque.algoritmo.Graph;
import com.rinno.apumanque.models.Edges;
import com.rinno.apumanque.models.Nodes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    ArrayList arregloA = new ArrayList();
    ArrayList arregloB = new ArrayList();
    ArrayList arregloCosto = new ArrayList();

    Button btPrueba;
    boolean bandera = false;
    boolean bandera2 = false;
    int j;
    int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference  dbApumanqueNodes = reference.child("nodes");
        //DatabaseReference  dbApumanqueEdges = reference.child("Edges");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("TAG","VALOR: "+dataSnapshot.child("Nodes").getChildrenCount());

                for(int i = 1; i <= dataSnapshot.child("Nodes").getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child("Nodes").child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId().trim());
                    //Log.e("TAG","VALOR A: "+nod.getId().getClass().toString());
                    vertices.add(a);
                   //Log.e("TAG","VALOR A: "+a);

                }
                //Log.e("TAG","VALOR ed: "+vertices);

                for(int i = 1; i <= dataSnapshot.child("Edges").getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child("Edges").child(String.valueOf(i)).getValue(Edges.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getInicio().trim());
                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getFin().trim());

                    for (int l = 0; l < vertices.size(); l++){
                        if(vertices.get(l).equals(a)){
                            //Log.e("TAG","ENCONTRADO A: "+vertices.get(l)+ i + "---> " +l);
                            arregloA.add(l);
                        }
                    }

                    for (int l = 0; l < vertices.size(); l++){
                        if(vertices.get(l).equals(b)){
                            //Log.e("TAG","ENCONTRADO B: "+vertices.get(l)+ i);
                            arregloB.add(l);
                        }
                    }
                    arregloCosto.add(edg.getCosto());
                    //Log.e("TAG","arregloA: "+arregloA);
                    //Log.e("TAG","arregloB: "+arregloB);
                    //Log.e("TAG","arregloCosto: "+arregloCosto);
                    //Graph.Edge<String> ed = new Graph.Edge<String>(edg.getCosto(),a,b);
                    //edges.add(ed);
                }
                //Log.e("TAG","Vertices: "+vertices);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        dbApumanqueNodes.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                for(int i = 1; i < dataSnapshot.getChildrenCount(); i++) {
//
//                    Nodes nod = dataSnapshot.child(String.valueOf(i)).getValue(Nodes.class);
//                    //Log.e("TAG","VALOR: "+nod.getId());
//
//                    //Log.e("TAG", "FIREBASE " + dataSnapshot.child(String.valueOf(i)).getValue().toString());
//                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId());
//                    vertices.add(a);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("TAG","ERROR "+databaseError);
//            }
//        });

//        dbApumanqueEdges.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(int i = 1; i < dataSnapshot.getChildrenCount(); i++) {
//
//                    Edges edg = dataSnapshot.child(String.valueOf(i)).getValue(Edges.class);
//                    Log.e("TAG","VALOR: "+edg.getCost());
//
//                    //Log.e("TAG", "FIREBASE " + dataSnapshot.child(String.valueOf(i)).getValue().toString());
////                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId());
////                    vertices.add(a);
//                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getSource());
//                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getEnd());
//                    Graph.Edge<String> edggg = new Graph.Edge<String>(edg.getCost(),  a, b);
//                    edges.add(edggg);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("TAG","ERROR "+databaseError);
//            }
//        });
//
        btPrueba = (Button) findViewById(R.id.btPrueba);
        btPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < arregloA.size(); i++){
                    Graph.Edge<String> ed = new Graph.Edge<String>((int) arregloCosto.get(i),  vertices.get((int) arregloA.get(i)), vertices.get((int) arregloB.get(i)));
                    edges.add(ed);
                }

                Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
                Astar astar=new Astar();
        //astar.aStar(graph,   a, d);
        Log.e("TAG","RUTA: "+astar.aStar(graph, vertices.get(0), vertices.get(4)));


                //Log.e("TAG","GRAFO: "+graph);
                //Log.e("TAG","VERTICES: "+vertices);
                //Log.e("TAG","VERTICES: "+edges);
            }
        });

   }
}
