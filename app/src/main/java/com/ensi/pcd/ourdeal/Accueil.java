package com.ensi.pcd.ourdeal;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner categorie;
    ProgressBar ProgressBar;
    Button Search;
    ListView AnnonceListView;
    public static final int RC_SIGN_IN = 1;
    FirebaseDatabase firedb;
    DatabaseReference db;
    Firebase helper;
    CustomAdapter adapter1;
    String categ;
    EditText searchtxt;
    static boolean calledAlready = false;

    ArrayList<Annonce> annonces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        AnnonceListView = (ListView) findViewById(R.id.ListView);

        // initialiser base de données

        firedb = FirebaseDatabase.getInstance();

        if (!calledAlready) {
            firedb.setPersistenceEnabled(true);
            calledAlready = true;
        }
        //initialiser la réference de base de données
        db = firedb.getReference();
        db.keepSynced(true);
        helper = new Firebase(db);


        //enregistrer les annonces dans la base de données mobile
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
       // Picasso.setSingletonInstance(built);

        //adapter
        annonces = new ArrayList<Annonce>();
        annonces = helper.retrieve();

        adapter1 = new CustomAdapter(Accueil.this, annonces);
        AnnonceListView.setAdapter(adapter1);

        //rechercher annonces
        Search = (Button) findViewById(R.id.btn1);
        EditText searchTxt = (EditText) findViewById(R.id.editText);
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    //reset listview

                } else {
                    // searchItem(s.toString());
                    Search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent versRecherche = new Intent(Accueil.this, Recherche.class);
                            startActivity(versRecherche);
                            openDetailActivity(annonces, s.toString());
                        }
                    });

                    //adapter1.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Progress bar declaration
        ProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        // initialiser le progress bar
        ProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // categorie
        categorie = (Spinner) findViewById(R.id.categorie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listCategorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorie.setAdapter(adapter);
        categ = categorie.getSelectedItem().toString();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accueil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent verssettings = new Intent(Accueil.this, Parametres.class);
            startActivity(verssettings);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_depannonce) {
            Intent versajouter = new Intent(Accueil.this, Ajouter_annonce.class);
            startActivity(versajouter);
        } else if (id == R.id.nav_annonce) {
            Intent versApropos = new Intent(Accueil.this,Apropos.class);
            startActivity(versApropos);
        } else if (id == R.id.nav_connect) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(Accueil.this, "signed in !", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(Accueil.this, "signed in canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void openDetailActivity(ArrayList<Annonce> afiltrer, String mot_cle) {
        Intent i = new Intent(Accueil.this, Recherche.class);
        i.putExtra("ANNONCE_KEY", afiltrer);
        i.putExtra("KEYWORD_KEY", mot_cle);
        startActivity(i);
    }

   /*public void searchItem(String TextToSearch){

       Annonce o = new Annonce();
       o.setTitre(TextToSearch);

        for (o  : annonces. ){
            if(!item.contains(TextToSearch)){
                annonces.remove(item);
                adapter1.notifyDataSetChanged();
            }
        }

    }*/

}
