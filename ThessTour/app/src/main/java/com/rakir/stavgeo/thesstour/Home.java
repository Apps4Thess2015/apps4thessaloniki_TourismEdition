package com.rakir.stavgeo.thesstour;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Locale;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String LOCALE_GREEK = "el";
    public static final String LOCALE_ENGLISH = "en";
    public static String CUR_LANG;
    Locale mLocale;
    NavigationView navigationView;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs=getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        mLocale = new Locale(mPrefs.getString("locale",LOCALE_GREEK));
        CUR_LANG=mPrefs.getString("locale",LOCALE_GREEK);
        Locale.setDefault(mLocale);
        Configuration config = new Configuration();
        config.locale = mLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.home);
        create();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.home);
        setTitle(R.string.app_name);
        create();


    }

    private void create(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment=new MapFragment().newInstance(mPrefs.getString("category","map"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gr) {

            Configuration newConfig = new Configuration();
            mLocale = new Locale(LOCALE_GREEK);
            CUR_LANG=LOCALE_GREEK;
            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putString("locale",LOCALE_GREEK).apply();
            newConfig.locale = mLocale;
            onConfigurationChanged(newConfig);
            return true;
        }
        if (id == R.id.action_en) {
            Configuration newConfig = new Configuration();
            mLocale = new Locale(LOCALE_ENGLISH);
            CUR_LANG=LOCALE_ENGLISH;
            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putString("locale",LOCALE_ENGLISH).apply();
            newConfig.locale = mLocale;
            onConfigurationChanged(newConfig);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        SharedPreferences.Editor editor=mPrefs.edit();
        if (id == R.id.nav_map) {
            fragment=new MapFragment().newInstance("map");
            editor.putString("category","map").apply();
        } else if (id == R.id.nav_churches) {
            fragment=new MapFragment().newInstance("churches");
            editor.putString("category","churches").apply();
        } else if (id == R.id.nav_museums) {
            fragment=new MapFragment().newInstance("museums");
            editor.putString("category","museums").apply();
        } else if (id == R.id.nav_sights) {
            fragment=new MapFragment().newInstance("sights");
            editor.putString("category","sights").apply();
        } else if (id == R.id.nav_ancient) {
            fragment=new MapFragment().newInstance("ancients");
            editor.putString("category","ancients").apply();
        } else if (id == R.id.nav_exit) {
            finish();
        }

        if(fragment!=null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
//        navigationView.setCheckedItem(id);
        return true;
    }
}
