package com.esprit.tourapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.esprit.tourapp.Adapters.EndroitListeAdapter;
import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class listeendroit extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    Toolbar toolbarE;
    RecyclerView recyclerViewE;
    Button addE;
    RequestQueue mRequest;

    EndroitListeAdapter endroitListeAdapter;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeendroit);

        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI =retrofit.create(INodeJS.class);

        addE= findViewById(R.id.addE);
        addE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenaddE();
            }
        });
        toolbarE = findViewById(R.id.toolbarE);
        recyclerViewE = findViewById(R.id.recyclerviewE);
        mRequest = Volley.newRequestQueue(getApplicationContext());

        recyclerViewE.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewE.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        endroitListeAdapter = new EndroitListeAdapter();

        getEndroit();

    }

    private void oppenaddE() {
        Intent intent = new Intent(this,Adendroit.class);
        startActivity(intent);
    }


    public void getEndroit(){

        Call<List<endroitt>> endroitts  = RetrofiteClient.getINodeJS().getEndroit();

        endroitts.enqueue(new Callback<List<endroitt>>() {
            @Override
            public void onResponse(Call<List<endroitt>> call, Response<List<endroitt>> response) {
                if (response.isSuccessful()){
                    List<endroitt> endroitts1 = response.body();
                    endroitListeAdapter.setData(endroitts1);
                    recyclerViewE.setAdapter(endroitListeAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<endroitt>> call, Throwable t) {

            }
        });
    }



}