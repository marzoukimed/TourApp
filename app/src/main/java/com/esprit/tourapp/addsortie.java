package com.esprit.tourapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class addsortie extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    private TextView uem;
    private MaterialButton addsortie;
    private MaterialEditText dt_db;
    private MaterialEditText dt_fin;
    private MaterialEditText loc;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsortie);



        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI =retrofit.create(INodeJS.class);

        dt_db = (MaterialEditText) findViewById(R.id.dt_db);
        dt_fin = (MaterialEditText) findViewById(R.id.dt_fin);
        loc = (MaterialEditText) findViewById(R.id.loc);
        addsortie = (MaterialButton) findViewById(R.id.addsortie);

        uem = findViewById(R.id.uem);
        String emailll = getIntent().getStringExtra("keynamme");
        uem.setText(emailll);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dt_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            com.esprit.tourapp.addsortie.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {

                            month = month+1;
                            String date = day+"/"+month+"/"+year;
                            dt_db.setText(date);
                        }
                    },year,month,day);
                    datePickerDialog.show();
            }
        });

        dt_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        com.esprit.tourapp.addsortie.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dt_fin.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        addsortie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addSortie( dt_db.getText().toString(), dt_fin.getText().toString(), loc.getText().toString() , uem.getText().toString());
            }
        });
    }
    private void addSortie(final String dt_debut, final String dt_fin , final String location , final  String user_email) {

     //   final View enter_name_view = LayoutInflater.from(this).inflate(R.layout.register,null);

        new MaterialStyledDialog.Builder(this)
                .setTitle("Are you sure")
                .setDescription("one more step !")
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("ADD")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                      //  MaterialEditText edt_name = (MaterialEditText)enter_name_view.findViewById(R.id.edt_name);

                        compositeDisposable.add(myAPI.addSortie(dt_debut,dt_fin,location,user_email)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(addsortie.this, ""+s, Toast.LENGTH_SHORT).show();
                                    }
                                }));
                    }
                }).show();
    }


}