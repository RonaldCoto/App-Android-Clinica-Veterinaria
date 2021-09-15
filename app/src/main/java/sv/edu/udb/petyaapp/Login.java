package sv.edu.udb.petyaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;
import sv.edu.udb.petyaapp.models.Clientes;
import sv.edu.udb.petyaapp.models.Empleados;
import sv.edu.udb.petyaapp.models.RespEmpleado;

public class Login extends AppCompatActivity {
    private EditText emailET, passwordET;
   private Button loginemailBTN,logingoogleBTN, callSingUp;
    //Variable para gestionar FirebaseAuth
   private FirebaseAuth mAuth;
    int RC_SIGN_IN = 1;
    String TAG = "GoogleSignIn";
    //variable para redireccionar usuarios
    String redirect = "";
    //Agregar cliente de inicio de sesión de Google
    private GoogleSignInClient mGoogleSignInClient;
    //Llamando al servicio de Empleados
    private EmpleadoService empleadoservicio = Config.getRetrofit().create(EmpleadoService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        loginemailBTN=findViewById(R.id.loginemailBTN);
        logingoogleBTN=findViewById(R.id.logingoogleBTN);
        callSingUp=findViewById(R.id.sign_up_screen);
        //instancia de firebase auth
        mAuth = FirebaseAuth.getInstance();

        //evento del boton "IR" que realiza el logeo mediante email
        loginemailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail();
            }
        });
        //evento del boton "GOOGLE" que realiza el logeo mediante GOOGLE
        logingoogleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginGoogle();
            }
        });
        // evento de boton "REGISTRATE" que redirecciona a la actividad de registro
        callSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registro.class);
                startActivity(intent);
            }
        });

        // Configurar Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Crear un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*******Fin de OnCreate()********/
    }
    //metodo para logeo con email
    private void loginEmail(){
        String email, password;
        email=emailET.getText().toString();
        password=passwordET.getText().toString();
        //validando campos de correo y contraseña
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        //realizando logeo y capturando el resultado con "OnComplete"
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           //OnComplete nos permitira saber si el logeo fue exitoso o no
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
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
                              //recorremos "emps" para validar si el usuario logeado es empleado o no
                              String categoria="";
                              for(Empleados empleado:emps){
                                  //si existe en la tabla empleados almacenamos su categoria
                                  if(empleado.getCorreo().equals(email) ){
                                      categoria=empleado.getCategoria();
                                  }
                              }
                              //si la cuenta es de veterinaro
                              if(categoria.equals("E")){
                                  Intent intent = new Intent(Login.this,MainActivity.class);
                                  startActivity(intent);

                                  //si la cuenta es de recepcionista
                              }else if( categoria.equals("R")){
                                  Intent intent = new Intent(Login.this,Citas_secretaria.class);
                                  startActivity(intent);
                                  //si no es empleado es cliente
                              }else {
                                  Intent intent = new Intent(Login.this,Solicitar_cita_cliente.class);
                                  startActivity(intent);

                              }
                              Toast.makeText(getApplicationContext(),"Bienvenido!",Toast.LENGTH_LONG).show();

                          }

                          @Override
                          public void onFailure(Call<List<Empleados>> call, Throwable t) {
                              Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();

                          }
                      });

                }else{
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    //metodo para logeo com google
    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN); }
        //metodo OnActivityResult nos permitirá saber si el logeo com google fue exitoso
        @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Resultado devuelto al iniciar el Intent de GoogleSignInApi.getSignInIntent (...);
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                if(task.isSuccessful()){
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken()); } catch (ApiException e) {
                        // Google Sign In fallido, actualizar GUI
                        Log.w(TAG, "Google sign in failed", e);
                    }
                }else{ Log.d(TAG, "Error, login no exitoso:" + task.getException().toString());
                Toast.makeText(this, "Ocurrio un error. "+task.getException().toString(), Toast.LENGTH_LONG).show();
                }

        } }
        //metodo que permite completar el logeo mediante Google Auth. Este método recibe el token del usuario y obtiene su credencial.
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Redireccion al perfil de cliente

                            Intent perfilcliente = new Intent(Login.this, Solicitar_cita_cliente.class);
                            startActivity(perfilcliente);
                            Login.this.finish();
                            } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    //El metodo onStart nos permitirá saber si el usuario ya esta logeado, impidiendole estar en esta activity
    @Override protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //si no es null el usuario ya esta logueado
            //desactivar botones
            loginemailBTN.setEnabled(false);
            logingoogleBTN.setEnabled(false);
            callSingUp.setEnabled(false);
            // mover al usuario a su perfil
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
                    //recorremos "emps" para validar si el usuario logeado es empleado o no
                    String categoria="";
                    for(Empleados empleado:emps){
                        //si existe en la tabla empleados almacenamos su categoria
                        if(empleado.getCorreo().equals(user.getEmail()) ){
                            categoria=empleado.getCategoria();
                        }
                    }
                    //si la cuenta es de veterinaro
                    if(categoria.equals("E")){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);

                        //si la cuenta es de recepcionista
                    }else if( categoria.equals("R")){
                        Intent intent = new Intent(Login.this,Citas_secretaria.class);
                        startActivity(intent);
                        //si no es empleado es cliente
                    }else {
                        Intent intent = new Intent(Login.this,Solicitar_cita_cliente.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onFailure(Call<List<Empleados>> call, Throwable t) {
                    Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });
        } super.onStart(); }

    /*******fin de public class********/
}

