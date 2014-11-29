package com.example.rf.gig;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class Info extends ActionBarActivity {

    //Displays the map marker information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String snippet = intent.getStringExtra("snippet");

        TextView tv3=(TextView)findViewById(R.id.textView3);
        tv3.getBackground().setAlpha(90);
        tv3.append(title);
        TextView tv4=(TextView)findViewById(R.id.textView4);
        tv4.getBackground().setAlpha(90);
        tv4.append(snippet);
        TextView tv0=(TextView)findViewById(R.id.textView0);
        tv0.getBackground().setAlpha(90);
        ImageView im2 =(ImageView)findViewById(R.id.imageView2);
        im2.getBackground().setAlpha(90);
    }
}
