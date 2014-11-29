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

public class Make extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private static final int GPS_ERRORDIAGLOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 16;
    GoogleMap mMap;

    /*
     * Gets a String from a remote db that holds the marker information
     * Info can be set at http://ronanfrawley.com/giggo.php
     * Takes db info as a string from Post request
     * Generates String array of info for the points
     * Sends this String array to populate the map in gotoLocation method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Makes Post request to return a long string of information called result
        if(Build.VERSION.SDK_INT>=10){
            StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
            StrictMode.setThreadPolicy(tp);
        }
        String result = makePostRequest();

        //Identify how many markers need to be established as count
        int count = 0;
        for (int i=0; i<result.length(); i++) {
            if (result.charAt(i)=='<' && result.charAt(i+1)=='/' && result.charAt(i+2)=='p' && result.charAt(i+3)=='>')
                count++;
        }

        //Separate the band name, description, lat and lng values into an array
        String[] desc = new String[count*4];
        int checkWhichValue, multidimensionalCount = 0;
        for (int i=0; i<result.length(); i++) {
            if (result.charAt(i)=='<' && result.charAt(i+1)=='p' && result.charAt(i+2)=='>'){
                i+=3;
                checkWhichValue = 0;
                String band = "", venue = "", lat = "", lng = "";
                while (result.charAt(i+1)!='/' && result.charAt(i+3)!='>') {
                    if (result.charAt(i) == '%'){
                        i+=2;
                        checkWhichValue++;
                    }else{
                        switch (checkWhichValue){
                            case 0: band+=result.charAt(i); i++; break;
                            case 1: venue+=result.charAt(i); i++; break;
                            case 2: lat+=result.charAt(i); i++; break;
                            case 3: lng+=result.charAt(i); i++; break;
                        }
                    }
                }
                desc[multidimensionalCount] = band;
                desc[multidimensionalCount+1] = venue;
                desc[multidimensionalCount+2] = lat;
                desc[multidimensionalCount+3] = lng;
                multidimensionalCount+=4;
            }else{
                continue;
            }
        }

        //Check for google services and initialize a map by running the gotoLocation method
        if(servicesOK()){
            setContentView(R.layout.activity_map);
            if(initMap()){
                gotoLocation(desc, DEFAULT_ZOOM, count);
            }else{
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }
        }else{
            setContentView(R.layout.activity_my);
        }
    }

    /*
     *Establishes the marker points for each gig on the map
     *Controls map logic, i.e. clicking markers etc
     */
    private void gotoLocation(String[] desc, float zoom, int howLong) {

        //Puts market points and information into multidimensional arrays
        double[][] markerPoints = new double[howLong][2];
        String[][] description = new String[howLong][2];
        for (int i=0; i < desc.length; i++) {
            if (i % 4 == 0) {
                try {
                    markerPoints[i / 4][0] = Double.valueOf(desc[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (i % 4 == 1) {
                try {
                    markerPoints[i / 4][1] = Double.valueOf(desc[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (i % 4 == 2) {
                description[i / 4][0] = desc[i].replaceAll("\\\\", "");
            }else{
                description[i / 4][1] = desc[i].replaceAll("\\\\", "");
            }
        }

        //Adds each marker on the map using multidimensional array values
        for(int i=0; i<howLong; i++){
            int lat = (int)markerPoints[i][0];
            int lng = (int)markerPoints[i][1];
            if(lat==0 && lng==0){
                continue;
            }else{
                try {
                    MarkerOptions options = new MarkerOptions()
                            .title(description[i][0])
                            .position(new LatLng(markerPoints[i][0],markerPoints[i][1]))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_music));
                    options.snippet(description[i][1]);
                    mMap.addMarker(options);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        //Goes to the info activity if user clicks marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String title = marker.getTitle();
                String snippet = marker.getSnippet();
                Bundle latlng = new Bundle();
                latlng.putParcelable("position", marker.getPosition());

                Intent intent = new Intent(Make.this, Info.class);
                intent.putExtra("title", title);
                intent.putExtra("snippet", snippet);
                intent.putExtra("ll", latlng);

                startActivity(intent);
                return false;

            }
        });

        //Sets the first map to focus on Dublin
        LatLng ll = new LatLng(markerPoints[0][0],markerPoints[0][1]);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.animateCamera(update);
    }

    //Makes a http request and returns a long String of remote db information
    private String makePostRequest(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://ronanfrawley.com/test.php");
        String result="";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("x", ""));
        nameValuePair.add(new BasicNameValuePair("y", ""));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (java.io.UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            result = convertInputStreamToString(inputStream);
            System.out.println(result);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
        return result;

    }

    //Converts the input stream to a String for the makePostRequest method
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    //Sets the home button movement to a new activity and finishes mapping
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Make.this, MyActivity.class);
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
}