package sv.edu.udb.petyaapp.config;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    //definiendo url base para consumir la API de laravel
    private  static  final String BASEURL = "http://petyadsm.000webhostapp.com/api/";
    //generando instancia de retrofit
    private static Retrofit retrofit;
    public  static Retrofit getRetrofit(){
        if(retrofit==null){
            retrofit= new retrofit2.Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
    public static void Alerta(Context context, String mensaje){
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }
}
