package com.esprit.tourapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esprit.tourapp.Adapters.SortieListeAdapter;
import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sortie extends AppCompatActivity {
    INodeJS myAPI;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    MaterialButton btn_adds;

    private TextView yrmaill;
    private TextView usermail;
    Toolbar toolbar;
    RecyclerView recyclerView;



    SortieListeAdapter sortieListeAdapter;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortie);
        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI =retrofit.create(INodeJS.class);
        btn_adds = (MaterialButton)findViewById(R.id.addS);

        usermail = findViewById(R.id.usermail);
        yrmaill = findViewById(R.id.yrmaill);
        String emaill = getIntent().getStringExtra("keyname");
        yrmaill.setText(emaill);






        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        btn_adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openadd();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        sortieListeAdapter = new SortieListeAdapter();

        getSortie();





    }




    public void getSortie (){
        Call<List<Sortiee>> sortiees = RetrofiteClient.getINodeJS().getSortie();

        sortiees.enqueue(new Callback<List<Sortiee>>() {
            @Override
            public void onResponse(Call<List<Sortiee>> call, Response<List<Sortiee>> response) {
                if (response.isSuccessful()){
                    List<Sortiee> sortiees1 = response.body();
                    sortieListeAdapter.setData(sortiees1);
                    recyclerView.setAdapter(sortieListeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Sortiee>> call, Throwable t) {

            }
        });
    }
    public void openadd(){
        String emailll = yrmaill.getText().toString();
        Intent intent =new Intent(this , addsortie.class);
        intent.putExtra("keynamme",emailll);
        startActivity(intent);
    }






    }
