package com.ensi.pcd.ourdeal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
TextView nameTxt , descTxt ;
    ImageView photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameTxt = (TextView) findViewById(R.id.nameDetail);
        descTxt= (TextView) findViewById(R.id.descDetail);
        photoUrl = (ImageView) findViewById(R.id.photoDetail);
        //GET INTENT
        Intent i=this.getIntent();
        //RECEIVE DATA
        String name=i.getExtras().getString("NAME_KEY");
        String desc=i.getExtras().getString("DESC_KEY");
        String photo=i.getExtras().getString("PHOTO_KEY");
        //BIND DATA
        nameTxt.setText(name);
        descTxt.setText(desc);
        Picasso.with(this).load(photo).into(photoUrl);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnline()) {
                    Toast.makeText(DetailActivity.this,"Connecté à l'internet",Toast.LENGTH_SHORT).show(); ;
                    Intent versMessage = new Intent(DetailActivity.this, MessageActivity.class);
                    startActivity(versMessage);
                }
                else {
                    Toast.makeText(DetailActivity.this,"non connecté et redirigé a la connection adhoc",Toast.LENGTH_SHORT).show(); ;
                    Intent versAdhoc = new Intent(DetailActivity.this, AdhocActivity.class);
                    startActivity(versAdhoc);

                }
            }
        });

    }
    public  boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue=ipProcess.waitFor();
            return  (exitValue==0);
        }
        catch (IOException e)
        {e.printStackTrace();}
        catch(InterruptedException e) {
            e.printStackTrace();}
        return false ;
    }
}
