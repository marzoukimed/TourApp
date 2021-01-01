package com.esprit.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class accueil extends AppCompatActivity {

    private ImageView btn_sortie ;
    private ImageView endroit ;
    private ImageView btn_local;
    private ImageView btn_mapbox;
    private TextView yrmail;
    private ImageView pubb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        yrmail = findViewById(R.id.yrmaiil);
        String email = getIntent().getStringExtra("keyname");
        yrmail.setText(email);
        btn_mapbox = (ImageView)findViewById(R.id.mapboxxi);
        btn_mapbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapbox();
            }
        });



        btn_sortie = (ImageView)findViewById(R.id.sortieeb);
        btn_sortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSortie();
            }
        });
        pubb = (ImageView) findViewById(R.id.pubbb);
        pubb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPubb();
            }
        });


        endroit = (ImageView)findViewById(R.id.endroitidd);
        endroit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdendroit();
            }
        });
    }

    private void openPubb() {
        Intent intent =new Intent(this , Pub.class);
        startActivity(intent);
    }

    public void openSortie(){
        String emaill = yrmail.getText().toString();
        Intent intent =new Intent(this , Sortie.class);
        intent.putExtra("keyname",emaill);
        startActivity(intent);
    }
    public void openLocal(){
        Intent intent =new Intent(this , location.class);
        startActivity(intent);
    }

    public void openMap(){
        Intent intent =new Intent(this , positionMap.class);
        startActivity(intent);
    }
    public void openMapbox(){
        Intent intent =new Intent(this , mapboxx.class);
        startActivity(intent);
    }
    public void openAdendroit(){
        Intent intent =new Intent(this , listeendroit.class);
        startActivity(intent);
    }
}