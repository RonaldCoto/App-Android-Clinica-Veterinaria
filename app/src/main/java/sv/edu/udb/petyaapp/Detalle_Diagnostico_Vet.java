package sv.edu.udb.petyaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;

public class Detalle_Diagnostico_Vet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView edt2,edt4,edt5,edt6,edt7,edt8,edt9,edt10,edt11,edt12,edt13,edt14;

    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;

    private EmpleadoService empleadoservicio = Config.getRetrofit().create(EmpleadoService.class);
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__diagnostico__vet);

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

        navigationView.setCheckedItem(R.id.diagnosticos);  //item seleccionado por defecto


        /*******End Menu********/

        edt2=(TextView) findViewById(R.id.textView2);
        edt4=(TextView) findViewById(R.id.textView4);
        edt5=(TextView) findViewById(R.id.textView5);
        edt6=(TextView) findViewById(R.id.textView6);
        edt7=(TextView) findViewById(R.id.textView7);
        edt8=(TextView) findViewById(R.id.textView8);
        edt9=(TextView) findViewById(R.id.textView9);
        edt10=(TextView) findViewById(R.id.textView10);
        edt11=(TextView) findViewById(R.id.textView11);
        edt12=(TextView) findViewById(R.id.textView12);
        edt13=(TextView) findViewById(R.id.textView13);
        edt14=(TextView) findViewById(R.id.textView14);

        Bundle datos = getIntent().getExtras();


        edt2.setText("Nombre de mascota: " + datos.getString("nombre"));
        edt4.setText("Especie: " + datos.getString("especie"));
        edt5.setText("Raza: " + datos.getString("raza"));
        edt6.setText("Edad: " + datos.getString("edad"));
        edt7.setText("Sexo: " + datos.getString("sexo"));
        edt8.setText("Motivo: " + datos.getString("motivo"));
        edt9.setText("Peso: " + datos.getString("peso"));
        edt10.setText("Pulso: " + datos.getString("pulso"));
        edt11.setText("Temperatura:" + datos.getString("temperatura"));
        edt12.setText("Diagnostico: " + datos.getString("diagnostico"));
        edt13.setText("Tratamiento:" + datos.getString("tratamiento"));
        edt14.setText("Fecha:" + datos.getString("fecha"));

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
                Intent intent2 = new Intent(Detalle_Diagnostico_Vet.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.diagnosticos:
                Intent intent1 = new Intent(Detalle_Diagnostico_Vet.this, DiagnosticosVet.class);
                startActivity(intent1);
                break;
            case R.id.logoutBTN:
                cerrarSesion();
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
                    Intent intent = new Intent(Detalle_Diagnostico_Vet.this, Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Hasta Luego!", Toast.LENGTH_LONG).show();
                    Detalle_Diagnostico_Vet.this.finish();
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
            Intent perfilusuario = new Intent(Detalle_Diagnostico_Vet.this, Login.class);
            startActivity(perfilusuario);
        }
        super.onStart();
    }
}