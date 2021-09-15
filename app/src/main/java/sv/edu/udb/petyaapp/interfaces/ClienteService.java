package sv.edu.udb.petyaapp.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import sv.edu.udb.petyaapp.config.Config;
import sv.edu.udb.petyaapp.models.Clientes;
import sv.edu.udb.petyaapp.models.Empleados;
import sv.edu.udb.petyaapp.models.RespCliente;

public interface ClienteService {
    //metodo get para listar todos los clientes
    @GET("petya-clientes")
    Call<List<Clientes>> getClientes();


    //metodo post para insertar
    @POST("petya-clientes")
    Call<Clientes> insertClientes(
            @Body Clientes clientes

            );

    //metodo put para actualizar
    @PUT("petya-clientes")
    Call<Clientes> updateClientes(
      @Url String url, @Body Clientes clientes
    );

    //metodo delete para eliminar
    @DELETE("petya-clientes")
    Call<Clientes> deleteClientes( @Url String url);
}
