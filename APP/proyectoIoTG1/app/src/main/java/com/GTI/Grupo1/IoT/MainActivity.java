package com.GTI.Grupo1.IoT;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/*
* TODO: Modificar la foto que se muestra de la cuenta de google en forma redonda
* TODO:  Cuando el usuario se regitra por usuario y contraseña, se muestra el nombre y el correo, pero la foto se muestra la que hay por defecto
**/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user;


    ////////recycler view////////
    private RecyclerView recyclerView;
    public AdaptadorUsuarios adaptador;
    private RecyclerView.LayoutManager layoutManager;
    public static UsuarioInterface usuarios = new UsuariosVector();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Página de inicio
        InicioFragment fragment = new InicioFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();


       user = FirebaseAuth.getInstance().getCurrentUser();

        FloatingActionButton fab = findViewById(R.id.fab);
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

// Codigo para mostrar los datos del usuario en la parte superior del menu
        //Obtenemos las referencias de las vistas
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menuView = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        TextView nombre = headerView.findViewById(R.id.nombreUsuario);
        TextView correo = headerView.findViewById(R.id.correoUsuario);
        final ImageView foto = headerView.findViewById(R.id.fotoUsuario);
        // Asignamos los valores que se desean mostrar a las vistas
        nombre.setText(user.getDisplayName());
        correo.setText(user.getEmail());
// Codigo para identificar el provedor, soluciona problema cuando se hace login por Correo-contraseña
// no hay foto y da error, de esta manera filtramos y solo ejecuta el codigo cuando se hace login por google.
        String proveedor = user.getProviders().get(0);
        if(proveedor.equals("google.com")){
            String uri = user.getPhotoUrl().toString();
            Picasso.with(getBaseContext()).load(uri).into(foto);
            System.out.println("dentro de getPhoto");
        }
//        System.out.println("El proveedor de la cuenta es " + proveedor +".");



        navigationView.setNavigationItemSelectedListener(this);

//------------------------------------------------------------------------------------------------------------------
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("YCBIiP0cbezTvmHiFPq3").get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if (task.isSuccessful()) {
                                    String dato1 = task.getResult().getString("peso");
                                    //double dato2 = task.getResult().getDouble("altura");

                                    Toast toast1 = Toast.makeText(getApplicationContext(), dato1, Toast.LENGTH_SHORT);

                                    TextView view3 = findViewById(R.id.peso);

                                    view3.setText(dato1);
                                    toast1.show();
                                } else {
                                    Toast toast2 = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT);


                                    toast2.show();
                                }
                            }
                        });
//-------------------------------------------------------------------------------------------------------------------
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


/*
*  Codigo para el funcionamiento del segundo menu, ubicado a la parte superio derecha del activity_main
*/
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


/*
* Codigo para el funcionamiento del menu principal estilo google
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_inicio) {

            fragment = new InicioFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);//.addToBackStack(null); //para hacer que al pulsar atras no salga de la aplicación
            fragmentTransaction.commit();


        } else if (id == R.id.nav_bascula) {

            fragment = new BasculaFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);//.addToBackStack(null); //para hacer que al pulsar atras no salga de la aplicación
            fragmentTransaction.commit();

        } else if (id == R.id.nav_casa) {

        } else if (id == R.id.nav_usuarios) {

            fragment = new UsuariosFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_preferencias) {
            lanzarPreferencias(null);
            return true;
        } else if (id == R.id.nav_chart){
            lanzarChart(null);
            return true;

        } else if (id == R.id.nav_perfil) {

            fragment = new PerfilFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);//.addToBackStack(null); //para hacer que al pulsar atras no salga de la aplicación
            fragmentTransaction.commit();

        } else if (id == R.id.nav_cerrar_sesion) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK|
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    MainActivity.this.finish();
                }
            });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    public void lanzarChart(View view){
        Intent i = new Intent(this, ChartActivity.class);
        startActivity(i);
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
