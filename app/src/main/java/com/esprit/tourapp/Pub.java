package com.esprit.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.esprit.tourapp.Adapters.VideoAdapterrrrr;

import java.util.ArrayList;

public class Pub extends AppCompatActivity {


    RecyclerView recyclerViewP;

    ArrayList<Pliste> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        recyclerViewP = findViewById(R.id.recyclerviewP);

        recyclerViewP.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewP.setHasFixedSize(true);
        arrayList = new ArrayList<Pliste>();

        Pliste pliste =new Pliste("https://www.youtube.com/watch?v=OtJVufo3IrA");
        arrayList.add(pliste);
        pliste =new Pliste("https://www.youtube.com/watch?v=mtIeLsMbfzg&pbjreload=101");
        arrayList.add(pliste);
        pliste =new Pliste("https://www.youtube.com/watch?v=bZkLuCgORi4");
        arrayList.add(pliste);
        pliste =new Pliste("https://www.youtube.com/watch?v=8PNTZEv-e54");
        arrayList.add(pliste);
        pliste =new Pliste("https://www.youtube.com/watch?v=4It9yQSjaGA");
        arrayList.add(pliste);


        VideoAdapterrrrr videoAdapterrrrr = new VideoAdapterrrrr(arrayList,getApplicationContext());
        recyclerViewP.setAdapter(videoAdapterrrrr);

    }
}