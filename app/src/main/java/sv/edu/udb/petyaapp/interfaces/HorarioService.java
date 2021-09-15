package sv.edu.udb.petyaapp.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sv.edu.udb.petyaapp.models.Horarios;

public interface HorarioService {
    //metodo get para listar todos los horarios
    @GET("petya-horarios")
    Call<List<Horarios>> getHorarios();


}
