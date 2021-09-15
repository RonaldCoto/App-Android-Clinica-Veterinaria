package sv.edu.udb.petyaapp.interfaces;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sv.edu.udb.petyaapp.models.Diagnosticos;

public interface DiagnosticosVetService {
    @GET("petya-diagnosticos/{id}")
    Call<ArrayList<Diagnosticos>> getdiagnosticosvet(@Path("id") String id);
}
