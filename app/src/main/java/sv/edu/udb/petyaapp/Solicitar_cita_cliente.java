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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.udb.petyaapp.adapters.EmpleadosAdapter;
import sv.edu.udb.petyaapp.adapters.HorariosAdapter;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.ClienteService;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;
import sv.edu.udb.petyaapp.interfaces.FormCitaService;
import sv.edu.udb.petyaapp.interfaces.HorarioService;
import sv.edu.udb.petyaapp.models.Clientes;
import sv.edu.udb.petyaapp.models.Empleados;
import sv.edu.udb.petyaapp.models.FormCitas;
import sv.edu.udb.petyaapp.models.Horarios;

public class Solicitar_cita_cliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ScrollView scrollView;
    //Variables de los campos del formulario
    AutoCompleteTextView autoCompleteTextViewEmpleados, autoCompleteTextViewHorarios, autoCompleteTextViewSexo;
    TextInputEditText textInputEditTextEmpleado, textInputEditTextCliente, textInputEditTextUID, txtEDTFecha, txtEDTNombre, txtEDTEdad, txtEDTEspecie, txtEDTRaza, txtEDTMotivo;
    Button btnRegistro;
    String[] sexo = {"Macho", "Hembra"};
    //Variables para el retrofit de Empleados
    ArrayList<Empleados> empleadosList;
    EmpleadosAdapter adapterEmpleado;
    private EmpleadoService empleadoservicio = Config.getRetrofit().create(EmpleadoService.class);
    //Variables para el retrofit de Horarios
    ArrayList<Horarios> horariosList;
    HorariosAdapter adapterHorario;
    private HorarioService horarioService = Config.getRetrofit().create(HorarioService.class);
    //Variables para el retrofit de Clientes
    private ClienteService clienteService = Config.getRetrofit().create(ClienteService.class);
    //Variables para el retrofit de FormCitas
    private FormCitaService formCitaService = Config.getRetrofit().create(FormCitaService.class);
    //Variable para gestionar FirebaseAuth
    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_solicitar_cita_cliente);
            // Inicializar Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //Configurar las gso para google signIn con el fin de luego desloguear de google
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)) .requestEmail() .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            /*******Menu********/
            drawerLayout =findViewById(R.id.drawerlayout);
            navigationView =findViewById(R.id.nav_view);
            toolbar =findViewById(R.id.toolbar);

            /*******Campos del formulario*******/
            autoCompleteTextViewEmpleados = findViewById(R.id.dropdown_veterinario);
            textInputEditTextEmpleado = findViewById(R.id.idEmpleado);
            textInputEditTextCliente = findViewById(R.id.idCliente);
            textInputEditTextUID = findViewById(R.id.uId);
            txtEDTFecha = findViewById(R.id.fecha_cita);
            autoCompleteTextViewHorarios = findViewById(R.id.dropdown_hora_cita);
            txtEDTNombre = findViewById(R.id.nombre_mascota);
            txtEDTEdad = findViewById(R.id.edad);
            autoCompleteTextViewSexo = findViewById(R.id.dropdown_sexo_mascota);
            txtEDTEspecie = findViewById(R.id.especie);
            txtEDTRaza = findViewById(R.id.raza);
            txtEDTMotivo = findViewById(R.id.motivo_cita);

            btnRegistro = findViewById(R.id.registroBTN);

            /******Llenando el combobox/autocompletetextview con la lista de los empleados******/
            listaEmpleados();
            /******Capturando el correo del usuario en sesion para obtener su id******/
            idCliente(currentUser);



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

            navigationView.setCheckedItem(R.id.solicitar_cita);  //item seleccionado por defecto
            /*******End Menu********/

            /*******scroll view********/
            scrollView = findViewById(R.id.mainScroll);
            scrollView.setVerticalScrollBarEnabled(false);
            /*******end scroll view********/


            /*********AutoCompleteTextView de Sexo de la Mascota********/
            ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(Solicitar_cita_cliente.this,
                    R.layout.dropdown_item, sexo);

            autoCompleteTextViewSexo.setAdapter(adapterGenero);

            btnRegistro.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    añadirCita();
                }
            });



    }



    /*******Obteniendo la lista de empleados*****/
    private void listaEmpleados() {
        empleadosList = new ArrayList<>();
        Call<List<Empleados>> call = empleadoservicio.getEmpleados();
        call.enqueue(new Callback<List<Empleados>>() {
            @Override
            public void onResponse(Call<List<Empleados>> call, Response<List<Empleados>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                List<Empleados> emps = response.body();
                for(Empleados empleado: emps) {

                    if (empleado.getCategoria().equals("E")) {
                        empleadosList.add(empleado);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Empleados>> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        adapterEmpleado = new EmpleadosAdapter(this, empleadosList);
        autoCompleteTextViewEmpleados.setAdapter(adapterEmpleado);

        autoCompleteTextViewEmpleados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextViewHorarios.setText("");
                textInputEditTextEmpleado.setText("");
                /*******Creo una variable tipo empleados y hago una conversion implicita de Empleados al resultado tipo object del item dentro del adapter *******/
                Empleados emp_seleccionado = (Empleados) autoCompleteTextViewEmpleados.getAdapter().getItem(position);

                String empleado = String.valueOf(emp_seleccionado.getId());


                listarHorarios(empleado);
            }
        });

    }

    /***OBTENIENDO EL LISTADO DE HORARIOS EN BASE AL ID DEL EMPLEADO****/
    public void listarHorarios(String id_empleado){

        horariosList = new ArrayList<>();
        textInputEditTextEmpleado.setText(id_empleado);

        Call<List<Horarios>> call2 = horarioService.getHorarios();
        call2.enqueue(new Callback<List<Horarios>>() {
            @Override
            public void onResponse(Call<List<Horarios>> call2, Response<List<Horarios>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                List<Horarios> hors = response.body();

                String id = "";
                for(Horarios horario: hors) {
                    id = String.valueOf(horario.getEmpleado_id());
                    if(id.equals(id_empleado)){
                        horariosList.add(horario);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Horarios>> call2, Throwable t) {
                Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        adapterHorario = new HorariosAdapter(this, horariosList);
        autoCompleteTextViewHorarios.setAdapter(adapterHorario);
    }

    /***METODO PARA OBTENER EL ID DEL CLIENTE*****/
    private void idCliente(FirebaseUser currentUser) {
        String correo = currentUser.getEmail();

        Call<List<Clientes>> call = clienteService.getClientes();
        call.enqueue(new Callback<List<Clientes>>() {
            @Override
            public void onResponse(Call<List<Clientes>> call, Response<List<Clientes>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();
                    return;
                }
                List<Clientes> hors = response.body();
                for(Clientes cli: hors){
                    if(cli.getCorreo().equals(correo)){
                        String id = String.valueOf(cli.getId());


                        if(!id.equals("") || !id.equals("1")) {
                            textInputEditTextCliente.setText(id);
                        }

                    }
                }
                textInputEditTextUID.setText(currentUser.getUid());

            }

            @Override
            public void onFailure(Call<List<Clientes>> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    /*****BTN AÑADIR******/
    private void añadirCita() {
        String empleado, fecha_cita, hora_cita, nombre_mascota, edad, sexo, especie, raza, motivo, estado, uid;
        int idEmpleado, clienteId;
        empleado = autoCompleteTextViewEmpleados.getText().toString();
        fecha_cita = txtEDTFecha.getText().toString();
        hora_cita = autoCompleteTextViewHorarios.getText().toString();
        nombre_mascota = txtEDTNombre.getText().toString();
        edad = txtEDTEdad.getText().toString();
        sexo = autoCompleteTextViewSexo.getText().toString();
        especie = txtEDTEspecie.getText().toString();
        raza = txtEDTRaza.getText().toString();
        motivo = txtEDTMotivo.getText().toString();
        estado = "solicitado";
        uid = textInputEditTextUID.getText().toString();

        if(TextUtils.isEmpty(empleado)){
            Toast.makeText(getBaseContext(),"Falta por seleccionar a un empleado" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(fecha_cita)){
            Toast.makeText(getBaseContext(),"Falta por ingresar una fecha" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(hora_cita)){
            Toast.makeText(getBaseContext(),"Falta por seleccionar un intervalo de hora" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(nombre_mascota)){
            Toast.makeText(getBaseContext(),"Falta por ingresar el nombre de la mascota" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(edad)){
            Toast.makeText(getBaseContext(),"Falta por ingresar una edad" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(sexo)){
            Toast.makeText(getBaseContext(),"Falta por seleccionar el sexo de su mascota" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(especie)){
            Toast.makeText(getBaseContext(),"Falta por ingresar la especie de su mascota" ,Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(raza)){
            Toast.makeText(getBaseContext(),"Falta por ingresar la raza de su mascota" ,Toast.LENGTH_LONG).show();
            return;
        }

        idEmpleado = Integer.parseInt(textInputEditTextEmpleado.getText().toString());
        clienteId = Integer.parseInt(textInputEditTextCliente.getText().toString());



        FormCitas formCitas = new FormCitas(fecha_cita, hora_cita, nombre_mascota, especie, raza, edad, sexo,  motivo, estado, idEmpleado, clienteId, uid);

        Call<FormCitas> call = formCitaService.insertarCitas(formCitas);

        call.enqueue(new Callback<FormCitas>() {
            @Override
            public void onResponse(Call<FormCitas> call, Response<FormCitas> response) {
                if(response.code() == 200){
                    Toast.makeText(getBaseContext(), "Cita añadida satisfactoriamente", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                }else{
                    Toast.makeText(getBaseContext(), "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<FormCitas> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void limpiarCampos(){
        autoCompleteTextViewEmpleados.setText("");
        txtEDTFecha.setText("");
        autoCompleteTextViewHorarios.setText("");
        txtEDTNombre.setText("");
        txtEDTEdad.setText("");
        autoCompleteTextViewSexo.setText("");
        txtEDTEspecie.setText("");
        txtEDTRaza.setText("");
        txtEDTMotivo.setText("");
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
            case R.id.logoutBTN:
                cerrarSesion();
                break;
            case R.id.solicitar_cita:

                break;
            case R.id.historial:
                Intent intent = new Intent(Solicitar_cita_cliente.this, DiagnosticosCliente.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    /*******End Menu********/



    //metodo para cerrar sesion con email y google tras hacer click en el boton
    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();

        //Cerrar sesión con google tambien: Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                //Abrir MainActivity con SigIn button
                if(task.isSuccessful()){
                    Intent intent = new Intent(Solicitar_cita_cliente.this,Login.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Hasta Luego!", Toast.LENGTH_LONG).show();
                    Solicitar_cita_cliente.this.finish();
                }else{ Toast.makeText(getApplicationContext(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                } } });
            }
    //El metodo onStart nos permitirá saber si el usuario no esta logeado, impidiendole estar en esta activity
    @Override protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            //si es null el usuario no esta logueado
            // mover al usuario al login
            Intent perfilusuario = new Intent(Solicitar_cita_cliente.this, Login.class);
            startActivity(perfilusuario);
        } super.onStart(); }

    /*******fin de public class********/
    }
