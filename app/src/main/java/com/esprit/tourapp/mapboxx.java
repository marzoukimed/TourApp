package com.esprit.tourapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.tourapp.Retrofite.INodeJS;
import com.esprit.tourapp.Retrofite.RetrofiteClient;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class mapboxx extends AppCompatActivity implements OnMapReadyCallback , LocationEngineListener ,
        PermissionsListener , MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
   // private Marker Marker;
    private static final String TAG = "mapboxx";
    private TextView coord;
    private TextView laaa;
    private Button setLocation;
    private List<LatLng> myDataSet;
    endroitt endroittt;
    LatLng center,latLng;
    String title;
    INodeJS myAPI;


    LatLng loc1 = new LatLng( 36.86435520205643,10.275475788962808);
    LatLng loc2 = new LatLng(36.85675444453227,10.277679881531753);
    LatLng loc3 = new LatLng( 35.16097735582669,9.229802803438588);

    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_mapboxx);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Retrofit retrofit = RetrofiteClient.getInstance();
        myAPI=retrofit.create(INodeJS.class);
       // myDataSet=new ArrayList<>();








        laaa = findViewById(R.id.laaa);
        coord = findViewById(R.id.Coord);

        setLocation = findViewById(R.id.setLocation);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openadd();
            }
        });


    }
    public void getlatlan(){

        Call<List<endroitt>> endroitts  = RetrofiteClient.getINodeJS().getEndroit();

        endroitts.enqueue(new Callback<List<endroitt>>() {
            @Override
            public void onResponse(Call<List<endroitt>> call, retrofit2.Response<List<endroitt>> response) {
                if (response.isSuccessful()) {

                    List<endroitt> endroitts1 = response.body();
                    for (endroitt a : endroitts1) {
                        String lat = String.valueOf(a.getLat());
                       // String lan =String.valueOf(a.getLan());
                        //System.out.println(a.toString());

                       // endroitt endr = new endroitt(lat, lan);
                        myDataSet=new ArrayList<LatLng>();


                        String[] latLng = lat.split(",");
                       // myDataSet.add();
                        
                        double latitude = Double.valueOf(latLng[1]);
                        double longitude = Double.parseDouble(latLng[0]);
                        LatLng location = new LatLng(latitude, longitude);
                       // System.out.println(lan);
                        myDataSet.add(location);
                        for (int i=0; i<myDataSet.size(); i++){


                        System.out.println(myDataSet);
                        map.addMarker(new MarkerOptions().position(myDataSet.get(i)));
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<endroitt>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getMessage());
            }
        });
    }



    private void openadd() {
        String coor = coord.getText().toString();
        String laa = laaa.getText().toString();
        Intent intent = new Intent(this , Adendroit.class);
        intent.putExtra("keynamecoo",coor);
        intent.putExtra("keyla",laa);
        startActivity(intent);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;


        map.animateCamera(CameraUpdateFactory.zoomTo(2.0f));

        //map.moveCamera(CameraUpdateFactory.newLatLng(location));



        map.addOnMapClickListener(this);
        getlatlan();


    }

    private void enableLocation(){
        if (permissionsManager.areLocationPermissionsGranted(this)){
                initializeLocationEngine();
                initializeLocationLayer();
              // getlatlan();

        }else{
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
    }


    @SuppressWarnings("MissingPermission")
    private  void initializeLocationEngine(){
            locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
            locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
            locationEngine.activate();

            Location lastLocation = locationEngine.getLastLocation();


            if(lastLocation != null){
                originLocation = lastLocation;
               // setCameraPosition(lastLocation);
               // getlatlan();
                map.animateCamera(CameraUpdateFactory.zoomTo(2.0f));
            } else {
                locationEngine.addLocationEngineListener(this);
            }

    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map , locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }












    private void setCameraPosition (Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()),13.0));
    }


    @Override
    public void onMapClick(@NonNull LatLng point) {

            if(destinationMarker != null){

                map.removeMarker(destinationMarker);
            }

            destinationMarker = map.addMarker(new MarkerOptions().position(point));
            destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());

            //originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            System.out.println(destinationPosition.coordinates().toString());
            System.out.println(destinationPosition.longitude());
            String result = String.valueOf(destinationPosition.coordinates());
            String rest = String.valueOf(destinationPosition.longitude()).concat(" ");
            coord.setText(result);
            laaa.setText(rest);
    }
    /*

    private void getRoute(Point origin , Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null){
                            Log.e(TAG , "No routes");

                            return;
                        }else if (response.body().routes().size() == 0){
                            Log.e(TAG,"No routes found");
                            return;
                        }

                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        if (navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }else{
                            navigationMapRoute = new NavigationMapRoute(null, mapView,map);
                        }


                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                            Log.e(TAG,"ERROR"+ t.getMessage());
                    }
                });
    }
*/
    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
            locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
            if(location != null){
                originLocation = location;
               // setCameraPosition(location);
                map.animateCamera(CameraUpdateFactory.zoomTo(2.0f));
            }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {


    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode,permissions , grantResults);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null){
            locationEngine.requestLocationUpdates();

        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }




}