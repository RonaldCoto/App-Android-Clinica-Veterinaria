package sv.edu.udb.petyaapp.interfaces;

import android.os.Bundle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sv.edu.udb.petyaapp.models.CitasVeterinario;

public interface CitasVetService {

    @GET("petya-formcita/proceso/{id}")
    Call<ArrayList<CitasVeterinario>> getcitasvet(@Path("id") String id);
}
