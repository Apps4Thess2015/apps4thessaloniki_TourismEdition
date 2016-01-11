package com.rakir.stavgeo.thesstour;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.identity.intents.AddressConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text=(TextView) findViewById(R.id.text);
        ImageView image=(ImageView) findViewById(R.id.image);
        String id=getIntent().getStringExtra("id");
        try {
            JSONObject jO=new JSONObject(getResources().getStringArray(R.array.markers_array)[Integer.parseInt(id)]);
            getSupportActionBar().setTitle(jO.getString("name"));
        } catch (JSONException e) {
            Log.e("JE Info L30","!");
        }catch (NullPointerException e){
            Log.e("JN Info L30", "!");
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("files/"+id+"_"+Home.CUR_LANG+".txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            StringBuilder sB=new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                sB.append(mLine+"\n");
            }
            text.setText(sB.toString());
            reader.close();

        } catch (IOException e) {
            Log.e("IOE Info L55", e.getMessage()+"!");
            finish();
        }
        try{
            InputStream ims = getAssets().open("images/"+id+".jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            image.setImageDrawable(d);
        } catch (Exception e){
            Log.e("E Info L55", e.getMessage()+"!");
            image.setVisibility(View.GONE);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
