package sv.edu.udb.petyaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.udb.petyaapp.adapters.CitasVetAdapter;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.CitasVetService;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;
import sv.edu.udb.petyaapp.models.CitasVeterinario;
import sv.edu.udb.petyaapp.models.Empleados;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    ListView listView;
    ArrayList<CitasVeterinario> citaLists;
    CitasVetService citaApi;
    CitasVetAdapter adapter;

    int id_veterinario = 0;

    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;

    private EmpleadoService empleadoservicio = Config.getRetrofit().create(EmpleadoService.class);
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//Configurar las gso para google signIn con el fin de luego desloguear de google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*******Menu********/
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //Colocando correo e imagen en el header del perfil:
        View header = navigationView.getHeaderView(0);
        TextView txtemail = (TextView) header.findViewById(R.id.txtCorreo);
        txtemail.setText(currentUser.getEmail());

        // cargar imágen con glide:
        ImageView imagenUser = (ImageView) header.findViewById(R.id.imagenUser);
        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this).load(currentUser.getPhotoUrl()).into(imagenUser);
        }
        //toolbar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu

        //ocultar o mostrar items
        Menu menu = navigationView.getMenu();

        //end ocultar o mostrar items

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.cita);  //item seleccionado por defecto


        /*******End Menu********/
        getidlogueado();

        /*******Lista********/
        listView = (ListView) findViewById(R.id.list_view_vet);
        //getData();
        /*******End Lista********/

    }

    private void getidlogueado() {
        Call<List<Empleados>> call = empleadoservicio.getEmpleados();
        call.enqueue(new Callback<List<Empleados>>() {
            @Override
            public void onResponse(Call<List<Empleados>> call, Response<List<Empleados>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Error:" + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                //almacenamos en "emps" el contenido de la consulta en formato gson
                //actualmente "emps" guardará a todos los empleados de la API
                List<Empleados> emps = response.body();
                //recorremos "emps" para validar si el usuario logeado es empleado o no
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                for (Empleados empleado : emps) {
                    //si existe en la tabla empleados almacenamos su categoria
                    if (empleado.getCorreo().equals(currentUser.getEmail())) {
                        id_veterinario = empleado.getId();
                    }
                }
                getData(id_veterinario);
                System.out.println(id_veterinario);


            }

            @Override
            public void onFailure(Call<List<Empleados>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error:" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getData(int id) {
        citaApi = Config.getRetrofit().create(CitasVetService.class);
        Call<ArrayList<CitasVeterinario>> call = citaApi.getcitasvet(String.valueOf(id));
        call.enqueue(new Callback<ArrayList<CitasVeterinario>>() {
            @Override
            public void onResponse(Call<ArrayList<CitasVeterinario>> call, Response<ArrayList<CitasVeterinario>> response) {
                citaLists = response.body();

                adapter = new CitasVetAdapter(getApplicationContext(), R.layout.listcitas, citaLists);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<CitasVeterinario>> call, Throwable t) {

            }
        });

    }

    /*******Menu********/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cita:
                break;


            case R.id.diagnosticos:
                Intent intent1 = new Intent(MainActivity.this, DiagnosticosVet.class);
                startActivity(intent1);
                break;
            case R.id.logoutBTN:
                cerrarSesion();
                break;
            case R.id.editcuenta:
                Intent intent2 = new Intent(MainActivity.this,actualizar_perfil_vet.class);
                startActivity(intent2);

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*******End Menu********/
    //metodo para cerrar sesion con email y google tras hacer click en el boton
    public void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        //Cerrar sesión con google tambien: Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Abrir MainActivity con SigIn button
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Hasta Luego!", Toast.LENGTH_LONG).show();
                    MainActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //El metodo onStart nos permitirá saber si el usuario no esta logeado, impidiendole estar en esta activity
    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            //si es null el usuario no esta logueado
            // mover al usuario al login
            Intent perfilusuario = new Intent(MainActivity.this, Login.class);
            startActivity(perfilusuario);
        }
        super.onStart();
    }

    /*******fin de public class********/
}