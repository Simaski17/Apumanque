package com.rinno.apumanque;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rinno.apumanque.algoritmo.Astar;
import com.rinno.apumanque.algoritmo.Graph;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference  mensajeReferencia = ref.child("Edges");
        Log.e("TAG","FIREBASE "+mensajeReferencia.getKey());

        List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
        List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
        Graph.Vertex<String> a = new Graph.Vertex<String>("a");
        Graph.Vertex<String> b = new Graph.Vertex<String>("b");
        Graph.Vertex<String> c = new Graph.Vertex<String>("c");
        Graph.Vertex<String> d = new Graph.Vertex<String>("d");
        Graph.Vertex<String> e = new Graph.Vertex<String>("e");
        Graph.Vertex<String> f = new Graph.Vertex<String>("f");
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
        vertices.add(e);
        vertices.add(f);

        Graph.Edge<String> ea_b = new Graph.Edge<String>(3, a, b);
        Graph.Edge<String> eb_c = new Graph.Edge<String>(3, b, c);
        Graph.Edge<String> ea_c = new Graph.Edge<String>(5, a, c);
        Graph.Edge<String> eb_d = new Graph.Edge<String>(4, b, d);
        Graph.Edge<String> eb_e = new Graph.Edge<String>(7, b, e);
        Graph.Edge<String> ec_d = new Graph.Edge<String>(2, c, d);
        Graph.Edge<String> ed_e = new Graph.Edge<String>(2, d, e);
        Graph.Edge<String> ed_f = new Graph.Edge<String>(2, d, f);
        Graph.Edge<String> ee_f = new Graph.Edge<String>(5, e, f);
        edges.add(ea_b);
        edges.add(eb_c);
        edges.add(ea_c);
        edges.add(eb_d);
        edges.add(ed_e);
        edges.add(ec_d);
        edges.add(ed_e);
        edges.add(ed_f);
        edges.add(ee_f);
        Graph<String> graph = new Graph<String>(Graph.TYPE.UNDIRECTED, vertices, edges);


        Astar astar=new Astar();
        astar.aStar(graph,   a, d);
        Log.e("TAG","RUTA: "+astar.aStar(graph, a, f));


    }
}
