package sv.edu.udb.petyaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.interfaces.ClienteService;
import sv.edu.udb.petyaapp.interfaces.EmpleadoService;
import sv.edu.udb.petyaapp.models.Clientes;
import sv.edu.udb.petyaapp.models.Empleados;
import sv.edu.udb.petyaapp.models.RespCliente;

public class Registro extends AppCompatActivity {
    private EditText emailET, passwordET, nombreET, telefonoET;
    private Button registroBTN,callSingIn;
    private FirebaseAuth mAuth;
    private ClienteService clienteservicio = Config.getRetrofit().create(ClienteService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro);
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        nombreET=findViewById(R.id.nombreET);
        telefonoET=findViewById(R.id.telefonoET);
        registroBTN=findViewById(R.id.registroBTN);
        callSingIn=findViewById(R.id.call_sig_in);
        //instancia de firebase auth
        mAuth = FirebaseAuth.getInstance();
       //evento del boton que registra a los nuevos usuarios
        registroBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ registroUsuario();}
        });

        //Evento del boton que redirecciona al login
        callSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this,Login.class);
                startActivity(intent);
            }
        });
    }
    private void  registroUsuario(){
        //validacion de entradas
        String email,password,nombre,telefono;
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        nombre = nombreET.getText().toString();
        telefono = telefonoET.getText().toString();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(telefono)){
            Toast.makeText(getApplicationContext(), "Debe ingresar su telefono", Toast.LENGTH_LONG).show();
            return;
        }

        //Registro mediante Firebase
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Registrado con exito!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Registro.this,Login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error!", Toast.LENGTH_LONG).show();

                }
            }
        });
        //Registro a base de datos
        Clientes clientes = new Clientes(nombre,email,"",telefono,"","");
        Call<Clientes> call = clienteservicio.insertClientes(clientes);
        call.enqueue(new Callback<Clientes>() {
            @Override
            public void onResponse(Call<Clientes> call, Response<Clientes> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"Error:"+response.code(),Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<Clientes> call, Throwable t) {
                Toast.makeText(getBaseContext(),"Error:"+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}