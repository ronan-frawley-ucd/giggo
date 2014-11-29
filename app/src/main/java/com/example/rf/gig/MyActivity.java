package com.example.rf.gig;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends ActionBarActivity {

    /*
     * Goes to the map fragment activity Make.java, on button click
     * Finishes home activity in the intent
     * Use onBackPressed in the Make to finish the map and create the home activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        TextView tv1=(TextView)findViewById(R.id.textView1);
        tv1.getBackground().setAlpha(90);
        TextView tv2=(TextView)findViewById(R.id.textView2);
        tv2.getBackground().setAlpha(90);
        ImageView im1 =(ImageView)findViewById(R.id.imageView1);
        im1.getBackground().setAlpha(90);

        final Button makeButton = (Button)findViewById(R.id.button2);
        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeButton.getBackground().setAlpha(128);
                Intent intent = new Intent(MyActivity.this, Make.class);
                startActivity(intent);
                finish();
            }
        });

        final Button createButton = (Button)findViewById(R.id.button1);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createButton.getBackground().setAlpha(128);
                Intent intent = new Intent(MyActivity.this, Create.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
