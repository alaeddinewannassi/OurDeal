package com.ensi.pcd.ourdeal;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by Ouannassi on 16/04/2017.
 * 1.SAVE DATA TO FIREBASE
 * 2. RETRIEVE
 * 3.RETURN AN ARRAYLIST */

public class Firebase  {

    DatabaseReference db;
    ChildEventListener ref ;
    Boolean saved=null;
    ArrayList<Annonce> annonces=new ArrayList<>();
    public Firebase(DatabaseReference db) {
        this.db = db;
    }
    //ecrire si annonce non nulle
    public Boolean save(Annonce annonce)
    {
        if(annonce==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("Annonces").push().setValue(annonce);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }
    // implementer recherche données et remplir arraylist
   private void fetchData(DataSnapshot dataSnapshot)
    {
        annonces.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Annonce annonce = ds.getValue(Annonce.class);
            annonces.add(annonce);
        }
    }
    //lire par recherche dans les données retounrnées par bd
    public ArrayList<Annonce> retrieve() {
        ref=  db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               fetchData(dataSnapshot);

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return annonces;
    }
    public ChildEventListener event() {
        return ref ;
    }

}



