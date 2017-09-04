package com.ensi.pcd.ourdeal;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.data;

public class Ajouter_annonce extends AppCompatActivity {

    Spinner categorie;
    Button btnValider;
    EditText Editprix, Edittitre, Editdescription, Editadresse, Editville;
    ImageButton mPhotoPickerButton;
    StorageReference storagephotoref;
    Uri downloadUrl;
    private static final int RC_PHOTO_PICKER = 2;
    DatabaseReference mRef;
    Firebase helper;
    Annonce objet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_annonce);

        //firebase

        mRef = FirebaseDatabase.getInstance().getReference();
        storagephotoref = FirebaseStorage.getInstance().getReference().child("photos");

        helper = new Firebase(mRef);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


        categorie = (Spinner) findViewById(R.id.categ);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listCategorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorie.setAdapter(adapter);
        Editprix = (EditText) findViewById(R.id.prix);
        Editadresse = (EditText) findViewById(R.id.adresse);
        Editville = (EditText) findViewById(R.id.ville);
        Edittitre = (EditText) findViewById(R.id.titre);
        Editdescription = (EditText) findViewById(R.id.desc);
        btnValider = (Button) findViewById(R.id.ajouter);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.imageButton);


        // ImagePickerButton renvoie un utilitaire de choix d 'image pour uploder une image pour une deposition d annonce
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });


        btnValider.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // reception de données
                        int prix = Integer.parseInt(Editprix.getText().toString());
                        String titre = Edittitre.getText().toString();
                        String description = Editdescription.getText().toString();
                        String adresse = Editadresse.getText().toString();
                        String ville = Editville.getText().toString();
                        String categ = categorie.getSelectedItem().toString();


                        // transformer les données en input

                        objet.setAdresse(adresse);
                        objet.setCategorie(categ);
                        objet.setDescription(description);
                        objet.setPrix(prix);
                        objet.setVille(ville);
                        objet.setTitre(titre);
                        objet.setDate_pub(sdf.format(new Date()));

                        //sauvegarder annonces dans firebase
                        if (helper.save(objet)) {
                            Intent versAccueil = new Intent(Ajouter_annonce.this, Accueil.class);
                            Toast.makeText(Ajouter_annonce.this, "annonce crée avec succés ! ", Toast.LENGTH_SHORT).show();
                            startActivity(versAccueil);
                        } else {
                            Toast.makeText(Ajouter_annonce.this, "probléme lors d 'insertion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            Uri selectedImageUri = data.getData();

            // avoir une réference pour sauvegarder image dans photos/<FILENAME>
            final StorageReference photoRef = storagephotoref.child(selectedImageUri.getLastPathSegment());

            // Uploader image dans Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            downloadUrl = taskSnapshot.getDownloadUrl();
                            objet = new Annonce();
                            objet.setPhotoUrl(downloadUrl.toString());


                            //Toast.makeText(Ajouter_annonce.this,"upload done !",Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }


}


