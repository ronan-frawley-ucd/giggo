package com.example.rf.gig;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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

public class Insert extends ActionBarActivity {

    String ll = "", bd = "";

    /* Sends a String to a remote db that holds the marker information
     * Info can be seen at http://ronanfrawley.com/test.php
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //Setting opacity for the backgrounds
        ImageView im2 =(ImageView)findViewById(R.id.imageView2);
        im2.getBackground().setAlpha(90);
        EditText e1 = (EditText)findViewById(R.id.editText1);
        e1.getBackground().setAlpha(90);
        EditText e2 = (EditText)findViewById(R.id.editText2);
        e2.getBackground().setAlpha(90);

        //Get position from intent
        Bundle bundle = getIntent().getParcelableExtra("ll");
        LatLng fromPosition = bundle.getParcelable("position");

        //Parsing the latlng object to get doubles for the map
        String latlngString = String.valueOf(fromPosition);
        String lat = "", lng = "";
        for (int i=0; i<latlngString.length(); i++){
            if(latlngString.charAt(i)=='('){
                i++;
                int count = 0;
                while(latlngString.charAt(i)!=')'){
                    if(latlngString.charAt(i)==','){
                        i++;
                        count++;
                    }else{
                        if(count==0){
                            lat = lat.concat(String.valueOf(latlngString.charAt(i)));
                            i++;
                        }else{
                            lng = lng.concat(String.valueOf(latlngString.charAt(i)));
                            i++;
                        }
                    }
                }
            }else{
                continue;
            }
        }

        //Build String of latitude and longitude for the date
        ll = lat+"%&"+lng;

        //Makes Post request to insert to database
        if(Build.VERSION.SDK_INT>=10){
            StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
            StrictMode.setThreadPolicy(tp);
        }

        //Sends the info to the remote db and finishes the activities
        final Button createButton = (Button)findViewById(R.id.button3);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et1 = (EditText)findViewById(R.id.editText1);
                EditText et2 = (EditText)findViewById(R.id.editText2);
                String band = et1.getText().toString();
                String desc = et2.getText().toString();
                bd = band+"%&"+desc;
                makePostRequest(ll, bd);
                createButton.getBackground().setAlpha(128);
                Intent intent = new Intent(Insert.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Makes a http request to the remote db
    private void makePostRequest(String ll, String bd){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://ronanfrawley.com/test.php");
        String result="";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("username2", ll));
        nameValuePair.add(new BasicNameValuePair("username", bd));

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

    //Sets the home button movement to a new activity and finishes creating
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Insert.this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}
