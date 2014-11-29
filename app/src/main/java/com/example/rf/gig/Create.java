package com.example.rf.gig;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Create extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener {

    private static final int GPS_ERRORDIAGLOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 14;
    GoogleMap mMap;

    /* Gets a position from a map using draggable markers
     * Sends it to be uploaded to a remote db in the insert method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check for google services and initialize a map by running the gotoLocation method
        if(servicesOK()){
            setContentView(R.layout.activity_create);
            if(initMap()){
                gotoLocation(DEFAULT_ZOOM);
            }else{
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }
        }else{
            setContentView(R.layout.activity_my);
        }
    }

    /* Establishes the marker point for the gig on the map
     * Controls map logic, i.e. clicking markers etc
     */
    private void gotoLocation(float zoom) {

        //Gets the marker position after dragging
        mMap.setOnMarkerDragListener(this);

        //Sets marker options
        MarkerOptions options = new MarkerOptions()
                .position(new LatLng(53.336755, -6.265667))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true);
        mMap.addMarker(options);

        //Instructional message
        Toast.makeText(Create.this, "Hold to move - Click to create gig", Toast.LENGTH_LONG).show();

        //Sets the first map to focus on Dublin
        LatLng ll = new LatLng(53.336755,-6.265667);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.animateCamera(update);

        //Goes to next screen and sets the marker position
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

            Bundle latlng = new Bundle();
            latlng.putParcelable("position", marker.getPosition());
            Intent intent = new Intent(Create.this, Insert.class);
            intent.putExtra("ll", latlng);

            startActivity(intent);
            finish();
            return false;

            }
        });
    }

    //Sets the home button movement to a new activity and finishes mapping
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Create.this, MyActivity.class);
        startActivity(intent);
        finish();
    }

    //Checks Google services
    public boolean servicesOK(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            //Toast.makeText(this, "Services OK!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIAGLOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //Initializes map fragment
    private boolean initMap(){
        if(mMap==null){
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return mMap!=null;
    }

    //Overrides method to set behaviour
    @Override
    public void onConnected(Bundle bundle) {

    }

    //Overrides method to set behaviour
    @Override
    public void onDisconnected() {

    }

    //Overrides method to set behaviour
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //Overrides method to set behaviour
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //Overrides method to set behaviour
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //Overrides method to set behaviour
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    //Overrides method to set behaviour
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    //Overrides method to set behaviour
    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
