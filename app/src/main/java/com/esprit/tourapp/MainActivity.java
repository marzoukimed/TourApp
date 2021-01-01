package com.esprit.tourapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.request.RequestOptions;
import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable =new CompositeDisposable();

    MaterialEditText edt_email,edt_password ;
    MaterialButton btn_register , btn_login;
    private LoginButton loginf_button;
    private CallbackManager callbackManager;
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
        setContentView(R.layout.activity_main);

        loginf_button =findViewById(R.id.loginf_button);
        callbackManager = CallbackManager.Factory.create();
        loginf_button.setReadPermissions(Arrays.asList("email","public_profile"));

        loginf_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });





        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI =retrofit.create(INodeJS.class);

        btn_login = (MaterialButton)findViewById(R.id.login_button);
        btn_register = (MaterialButton)findViewById(R.id.register_button);

        edt_email = (MaterialEditText)findViewById(R.id.edt_email);
        edt_password = (MaterialEditText)findViewById(R.id.edt_password);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_email.getText().toString()=="admin@admin" && edt_password.getText().toString()=="admin"){
                    openAdmin();
                }
                else
                loginUser(edt_email.getText().toString(),edt_password.getText().toString());

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(edt_email.getText().toString(), edt_password.getText().toString());
            }
        });
    }

    private void registerUser(final String email, final String password) {

        final View enter_name_view = LayoutInflater.from(this).inflate(R.layout.register,null);

        new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("one more step !")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Register")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        MaterialEditText edt_name = (MaterialEditText)enter_name_view.findViewById(R.id.edt_name);

                        compositeDisposable.add(myAPI.registerUser(email,edt_name.getText().toString(),password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                            }
                        }));
                    }
                }).show();
    }

    private void loginUser(final String email, final String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (new Consumer<String>(){


                    @Override
                    public void accept(String s) throws Exception {
                            if(s.contains("encrypted_password")) {
                                Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                openAccueil();
                            }


                            else
                                Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                })

        );
    }

    public void openAccueil(){

        String email = edt_email.getText().toString();
        Intent intent =new Intent(this , accueil.class);
        intent.putExtra("keyname",email);
        startActivity(intent);
    }
    public void openAdmin(){
        Intent intent =new Intent(this , admin.class);
        startActivity(intent);
    }
////////facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
                if(currentAccessToken==null){
                    Toast.makeText(MainActivity.this,"User Logged out", Toast.LENGTH_LONG).show();
                }
                else
                    loadUserProfile(currentAccessToken);

        }
    };
    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {


                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                    openAccueil();


            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
        openAccueil();
    }

}