package com.esprit.tourapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

//import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Adendroit extends AppCompatActivity{

    INodeJS myAPI;
    Uri picUri;
    private Uri filePath;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ImageView imageendroit ;
    MaterialButton addendroit , addendroitofficiel, fabUpload ,fabCamera , addendroitlocation;
    private MaterialEditText nomendroit , locationendroit , imageurl , latlanendroit, laaendroit;

    TextView textmsg;
    Intent myFileIntent;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    Bitmap mBitmap;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

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
        setContentView(R.layout.activity_adendroit);

        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI =retrofit.create(INodeJS.class);

        imageendroit = findViewById(R.id.imageendroit);
        //addendroit = findViewById(R.id.addendroit);
        addendroitofficiel = findViewById(R.id.addendroitofficiel);
        nomendroit = findViewById(R.id.nomendroit);
        locationendroit = findViewById(R.id.locationendroit);
        imageurl = findViewById(R.id.imageurl);
        textmsg = findViewById(R.id.textmsg);
        fabCamera=findViewById(R.id.fabCamera);
        fabUpload = findViewById(R.id.fabUpload);
        addendroitlocation = findViewById(R.id.addendroitlocation);


        latlanendroit = findViewById(R.id.latlanendroit);
        String coor = getIntent().getStringExtra("keynamecoo");
        latlanendroit.setText(coor);

        laaendroit = findViewById(R.id.laaendroit);
        String laa = getIntent().getStringExtra("keyla");
        laaendroit.setText(laa);

        addendroitlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdendroitmap();
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });
        fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitmap != null) {

                    multipartImageUpload();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addendroitofficiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addendroit( nomendroit.getText().toString(), locationendroit.getText().toString() , imageurl.getText().toString() , latlanendroit.getText().toString(),laaendroit.getText().toString() );
            }
        });


        askPermissions();


    }

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }

        return outputFileUri;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            ImageView imageView = findViewById(R.id.imageendroit);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(mBitmap);
                }

            }

        }

    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }









    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }



    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");

            OutputStream os;
            try {
                os = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("myFile", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "myFile");

            Call<String> req = myAPI.postImage(body, name);
            req.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    Log.e("Upload", String.valueOf(response.body()));

                    if (response.code() == 200) {
                        imageurl.setText("http://192.168.1.5:8090/tourapp/"+response.body());
                        textmsg.setText("Uploaded Successfully!");
                        textmsg.setTextColor(Color.BLUE);
                    }
                    Toast.makeText(getApplicationContext(), response + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    textmsg.setText("Uploaded Failed!");
                    textmsg.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.e("ERROR", t.toString());
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCamera:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;

            case R.id.fabUpload:
                if (mBitmap != null)
                    multipartImageUpload();
                else {
                    Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
*/

    private void addendroit(final String nom, final String location , final String photo, final String lat, final String lan) {
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

                        compositeDisposable.add(myAPI.addendroit(nom,location,photo,lat,lan)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(Adendroit.this, ""+s, Toast.LENGTH_SHORT).show();
                                    }
                                }));
                    }
                }).show();
    }





/*
    private void addendroit(String name, String location , String url) {

        new MaterialStyledDialog.Builder(this)
                .setTitle("Are you sure")
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

                        compositeDisposable.add(myAPI.addendroit(name,location,url)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        Toast.makeText(Adendroit.this, ""+s, Toast.LENGTH_SHORT).show();
                                    }
                                }));
                    }
                }).show();
    }

    private void pickImageFomGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
*/

    public void openAdendroitmap(){
        Intent intent =new Intent(this , mapboxx.class);
        startActivity(intent);
    }
}