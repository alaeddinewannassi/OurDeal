package com.ensi.pcd.ourdeal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Recherche extends AppCompatActivity {
    ArrayList<Annonce> filtered;
    ListView AnnonceListView;
    CustomAdapter adapter;
    String mot_cle;
    Button accueil;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        AnnonceListView = (ListView) findViewById(R.id.ListRech);
        accueil = (Button) findViewById(R.id.accueil);

        accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent versAcceuil = new Intent(Recherche.this, Accueil.class);
                startActivity(versAcceuil);
            }
        });
        //GET INTENT
        Intent i = this.getIntent();
        //RECEIVE DATA
        if (i.getSerializableExtra("ANNONCE_KEY") != null && i.getStringExtra("KEYWORD_KEY") != null) {
            filtered = (ArrayList<Annonce>) i.getExtras().getSerializable("ANNONCE_KEY");
            mot_cle = i.getExtras().getString("KEYWORD_KEY");
        }

        // set Adapter
        adapter = new CustomAdapter(Recherche.this, filtered);
        AnnonceListView.setAdapter(adapter);

        //rechercher le mot clé
        adapter.getFilter().filter(mot_cle);


    }
}
