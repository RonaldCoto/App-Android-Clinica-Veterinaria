package sv.edu.udb.petyaapp.interfaces;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sv.edu.udb.petyaapp.models.Diagnosticos;

public interface DiagnosticosClienteService {
    @GET("petya-diagnosticos-clientes/{id}")
    Call<ArrayList<Diagnosticos>> getdiagnosticoscliente(@Path("id") String id);
}
