package sv.edu.udb.petyaapp.interfaces;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sv.edu.udb.petyaapp.models.CitasSecretaria;


public interface Citas_secretariaService {
    @GET("petya-formcita/solicitado")
    Call<ArrayList<CitasSecretaria>> getCitas();

}
