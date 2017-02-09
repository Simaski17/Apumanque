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
import com.rinno.apumanque.algoritmo.Graph;
import com.rinno.apumanque.models.Edges;
import com.rinno.apumanque.models.Nodes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();

    Button btPrueba;

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
                Log.e("TAG","VALOR: "+dataSnapshot.child("nodes").getChildrenCount());

                for(int i = 1; i < dataSnapshot.child("nodes").getChildrenCount(); i++) {

                    Nodes nod = dataSnapshot.child("nodes").child(String.valueOf(i)).getValue(Nodes.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(nod.getId());
                    vertices.add(a);

                }

                for(int i = 1; i < dataSnapshot.child("Edges").getChildrenCount(); i++) {

                    Edges edg = dataSnapshot.child("Edges").child(String.valueOf(i)).getValue(Edges.class);
                    Graph.Vertex<String> a = new Graph.Vertex<String>(edg.getSource());
                    Graph.Vertex<String> b = new Graph.Vertex<String>(edg.getEnd());
                    Graph.Edge<String> ed = new Graph.Edge<String>(edg.getCost(),  a, b);
                    edges.add(ed);

                }




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
                Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
                Log.e("TAG","VERTICES: "+graph);
            }
        });


//        List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
//        List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
//        Graph.Vertex<String> a = new Graph.Vertex<String>("a");
//        Graph.Vertex<String> b = new Graph.Vertex<String>("b");
//        Graph.Vertex<String> c = new Graph.Vertex<String>("c");
//        Graph.Vertex<String> d = new Graph.Vertex<String>("d");
//        Graph.Vertex<String> e = new Graph.Vertex<String>("e");
//        Graph.Vertex<String> f = new Graph.Vertex<String>("f");
//        vertices.add(a);
//        vertices.add(b);
//        vertices.add(c);
//        vertices.add(d);
//        vertices.add(e);
//        vertices.add(f);
//
//        Graph.Edge<String> ea_b = new Graph.Edge<String>(3, a, b);
//        Graph.Edge<String> eb_c = new Graph.Edge<String>(3, b, c);
//        Graph.Edge<String> ea_c = new Graph.Edge<String>(5, a, c);
//        Graph.Edge<String> eb_d = new Graph.Edge<String>(4, b, d);
//        Graph.Edge<String> eb_e = new Graph.Edge<String>(7, b, e);
//        Graph.Edge<String> ec_d = new Graph.Edge<String>(2, c, d);
//        Graph.Edge<String> ed_e = new Graph.Edge<String>(2, d, e);
//        Graph.Edge<String> ed_f = new Graph.Edge<String>(2, d, f);
//        Graph.Edge<String> ee_f = new Graph.Edge<String>(5, e, f);
//        edges.add(ea_b);
//        edges.add(eb_c);
//        edges.add(eb_e);
//        edges.add(ea_c);
//        edges.add(eb_d);
//        edges.add(ed_e);
//        edges.add(ec_d);
//        edges.add(ed_e);
//        edges.add(ed_f);
//        edges.add(ee_f);
//        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);
//
//
//         Astar astar=new Astar();
//        astar.aStar(graph,   a, d);
//        Log.e("TAG","RUTA: "+astar.aStar(graph, a, f));


   }
}
