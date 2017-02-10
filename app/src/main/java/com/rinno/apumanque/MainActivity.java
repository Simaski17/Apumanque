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

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(int i = 1; i <= dataSnapshot.child("Nodes").getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child("Nodes").child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId().trim());
                    vertices.add(a);
                }

                for(int i = 1; i <= dataSnapshot.child("Edges").getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child("Edges").child(String.valueOf(i)).getValue(Edges.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getInicio().trim());
                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getFin().trim());

                    for (int l = 0; l < vertices.size(); l++){
                        if(vertices.get(l).equals(a)){
                            arregloA.add(l);
                        }
                    }

                    for (int l = 0; l < vertices.size(); l++){
                        if(vertices.get(l).equals(b)){
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
                Log.e("TAG","RUTA: "+astar.aStar(graph, vertices.get(0), vertices.get(4)));
            }
        });

   }
}
