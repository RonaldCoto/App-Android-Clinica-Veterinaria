package sv.edu.udb.petyaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;
import sv.edu.udb.petyaapp.models.Empleados;
import sv.edu.udb.petyaapp.models.RespEmpleado;

public class actualizar_perfil_vet extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ScrollView scrollView;

    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    //Llamando al servicio de Empleados
    private EmpleadoService empleadoservicio = Config.getRetrofit().create(EmpleadoService.class);

    //Variables de los campos del formulario
    TextInputEditText txtEDTnombre, txtEDTapellido, txtEDTtelefono;
    Button btnRegistro;
    /** variables globales para almacenar datos de usuario para actualizacion */
    Integer id=0;
    String nombres="";
    String apellidos="";
    String correo="";
    String foto_perfil="";
    String telefono="";
    String password="";
    String categoria="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
//Configurar las gso para google signIn con el fin de luego desloguear de google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) .requestEmail() .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil_vet);


        /*******Menu********/
        drawerLayout =findViewById(R.id.drawerlayout);
        navigationView =findViewById(R.id.nav_view);
        toolbar =findViewById(R.id.toolbar);

        /*******Campos del formulario*******/
        txtEDTnombre = findViewById(R.id.nombre_veterinario);
        txtEDTapellido = findViewById(R.id.apellido_veterinario);
        txtEDTtelefono = findViewById(R.id.telefono_veterinario);
        btnRegistro = findViewById(R.id.registroBTN);

        //Colocando correo e imagen en el header del perfil:
        View header=navigationView.getHeaderView(0);
        TextView txtemail=(TextView)  header.findViewById(R.id.txtCorreo);
        txtemail.setText(currentUser.getEmail());

        // cargar imágen con glide:
        ImageView imagenUser =(ImageView)  header.findViewById(R.id.imagenUser);
        if(currentUser.getPhotoUrl()!=null){
            Glide.with(this).load(currentUser.getPhotoUrl()).into(imagenUser);
        }
        //toolbar
        setSupportActionBar(toolbar);
        //Navigation Drawer Menu
        //ocultar o mostrar items
        Menu menu = navigationView.getMenu();
        //end ocultar o mostrar items

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.editcuenta);  //item seleccionado por defecto
        /*******End Menu********/
        /*******scroll view********/
        scrollView = findViewById(R.id.mainScroll);
        scrollView.setVerticalScrollBarEnabled(false);
        /*******end scroll view********/

        /***accion del boton Actualizar **/
        btnRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActualizarEmpleado();
            }
        });
    }
    /*******Menu********/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.cita:
                Intent intent = new Intent(actualizar_perfil_vet.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.editcuenta:

                break;
            case R.id.logoutBTN:
                cerrarSesion();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    /*******End Menu********



    /**El metodo onStart nos permitirá saber si el usuario no esta logeado, impidiendole estar en esta activity
    ademas nos permitirá la carga de datos de veterinario para actualizar en el formulario**/
    @Override protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            //si es null el usuario no esta logueado
            // mover al usuario al login
            Intent perfilusuario = new Intent(actualizar_perfil_vet.this, Login.class);
            startActivity(perfilusuario);
        }
        //en caso de que el usuario este logueado invocamos el metodo que llena el formulario
        llenarForm();

        super.onStart(); }

        /**metodo que llena el form com los datos del empleado a actualizar*/
        private void llenarForm(){
            FirebaseUser user = mAuth.getCurrentUser();
            //capturamos el resultado de la consulta que se hace a la API de laravel en la tabla de empleados
            Call<List<Empleados>> call = empleadoservicio.getEmpleados();
            call.enqueue(new Callback<List<Empleados>>() {
                @Override
                public void onResponse(Call<List<Empleados>> call, Response<List<Empleados>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();
                        return;
                    }
                    //almacenamos en "emps" el contenido de la consulta en formato gson
                    //actualmente "emps" guardará a todos los empleados de la API
                    List<Empleados> emps = response.body();
                    //recorremos "emps" para obtener los datos del empleado mediante comparacion de correos


                    for(Empleados empleado:emps){
                        //si se encuentra el correo, almacenamos los datos en variables y luego...
                        //desplegamos el contenido de las variables en los items del formulario
                        if(empleado.getCorreo().equals(user.getEmail()) ){
                            id=empleado.getId();
                            nombres=empleado.getNombres();
                            apellidos=empleado.getApellidos();
                            correo=empleado.getCorreo();
                            foto_perfil=empleado.getFoto();
                            telefono=empleado.getTelefono();
                            password=empleado.getContraseña();
                            categoria=empleado.getCategoria();

                            //llenando formulario
                            txtEDTnombre.setText(nombres);
                            txtEDTapellido.setText(apellidos);
                            txtEDTtelefono.setText(telefono);

                        }
                    }

                }

                @Override
                public void onFailure(Call<List<Empleados>> call, Throwable t) {
                    Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        }

        /*******  Metodo que se activa tras hacer click en el boton Actualizar*****/
        private void   ActualizarEmpleado(){


            //capturando los datos del formulario en caso de que el empleado los haya modificado
            nombres =txtEDTnombre.getText().toString();
            apellidos=txtEDTapellido.getText().toString();
            telefono=txtEDTtelefono.getText().toString();;
            //validando que los campos no esten vacios
            if (TextUtils.isEmpty(nombres)){
                Toast.makeText(getApplicationContext(), "Debe ingresar su nombre", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(apellidos)){
                Toast.makeText(getApplicationContext(), "Debe ingresar su apellido", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(telefono)){
                Toast.makeText(getApplicationContext(), "Debe ingresar su telefomo", Toast.LENGTH_LONG).show();
                return;
            }

            //realizando actualizacion
            Empleados datos_empleados = new Empleados(id,nombres,apellidos,correo,foto_perfil,telefono,password,categoria);
            Call<Empleados> call = empleadoservicio.updateEmpleados(datos_empleados);
            call.enqueue(new Callback<Empleados>() {
                @Override
                public void onResponse(Call<Empleados> call, Response<Empleados> response) {

                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(getBaseContext(),"Actualizado con éxito",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(actualizar_perfil_vet.this,MainActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<Empleados> call, Throwable t) {
                    Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();
                    System.out.println(t.getMessage());
                    System.out.println(String.valueOf(id));
                }
            });


        }



    //metodo para cerrar sesion con email y google tras hacer click en el boton
    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();

        //Cerrar sesión con google tambien: Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                //Abrir MainActivity con SigIn button
                if(task.isSuccessful()){
                    Intent intent = new Intent(actualizar_perfil_vet.this,Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Hasta Luego!", Toast.LENGTH_LONG).show();
                    actualizar_perfil_vet.this.finish();
                }else{ Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                } } });
    }
}