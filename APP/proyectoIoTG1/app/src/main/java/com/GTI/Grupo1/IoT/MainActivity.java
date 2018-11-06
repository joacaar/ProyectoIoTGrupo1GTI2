package com.GTI.Grupo1.IoT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

/*
* TODO: Modificar la foto que se muestra de la cuenta de google en forma redonda
* TODO:  Cuando el usuario se regitra por usuario y contraseña, se muestra el nombre y el correo, pero la foto se muestra la que hay por defecto
**/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//

//
//        nombre.setText("Pepe");
//        correo.setText("Correo");

//        nombre.setText(user.getDisplayName());
//        correo.setText(user.getEmail());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView nombre = headerView.findViewById(R.id.nombreUsuario);
        TextView correo = headerView.findViewById(R.id.correoUsuario);
        final ImageView foto = headerView.findViewById(R.id.fotoUsuario);
        nombre.setText(user.getDisplayName());
        correo.setText(user.getEmail());
        String uri = user.getPhotoUrl().toString();
        Picasso.with(getBaseContext()).load(uri).into(foto);
//        Picasso.with(getApplicationContext()).load(uri)
//                .into(foto, new Snackbar.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Bitmap imageBitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
//                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
//                        imageDrawable.setCircular(true);
//                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
//                        foto.setImageDrawable(imageDrawable);
//                    }
//                    @Override
//                    public void onError() {
//                        foto.setImageResource(R.drawable.manolo);
//                    }
//                });

//        Bitmap imageBitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
//        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
//        imageDrawable.setCircular(true);
//        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
//        foto.setImageDrawable(imageDrawable);

        navigationView.setNavigationItemSelectedListener(this);
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("bascula").setIndicator("Bascula"),
                Tab1.class, null);
        tabHost.addTab(tabHost.newTabSpec("redDeSensores").setIndicator("Red de sensores"),
                Tab2.class, null);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            lanzarPreferencias(null);
            return true;
        }
        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.inicio) {
            // Handle the camera action
            // Handle the camera action

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            lanzarPreferencias(null);
            return true;

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void lanzarPreferencias(View view){
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivity(i);
    }


}
