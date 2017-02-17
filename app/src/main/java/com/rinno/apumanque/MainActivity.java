package com.rinno.apumanque;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rinno.apumanque.algoritmo.Graph;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Listas donde manejamos la adicion de los vertices y edges del grafo
    public List<Graph.Vertex<String>> vertices = new ArrayList<Graph.Vertex<String>>();
    public List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();

    private ArrayList arregloA = new ArrayList();
    private ArrayList arregloB = new ArrayList();

    private ArrayList arregloLocationX = new ArrayList();
    private ArrayList arregloLocationY = new ArrayList();

    private ArrayList arregloIdRuta = new ArrayList();
    private ArrayList arregloType = new ArrayList();

    private ArrayList arregloCosto = new ArrayList();

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

    private int start;
    private int end;
    DatabaseReference referenceNodes;
    DatabaseReference referenceEdges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        referenceNodes = FirebaseDatabase.getInstance().getReference();
        referenceEdges = FirebaseDatabase.getInstance().getReference();
        referenceNodes.keepSynced(true);
        referenceEdges.keepSynced(true);

    }

    @OnClick({R.id.btIr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btIr:

                start = Integer.parseInt(etInicio.getText().toString());
                end = Integer.parseInt(etFin.getText().toString());

                Intent i = new Intent(this, MapaActivity.class);
                i.putExtra("start", start);
                i.putExtra("end", end);
                startActivity(i);

                etInicio.setText("");
                etFin.setText("");

                Log.e("TAG","INICIO: "+start + "FINAL: "+end);
                Log.e("TAG","VERTICES MAIN: "+vertices.size());
        }
    }
}
