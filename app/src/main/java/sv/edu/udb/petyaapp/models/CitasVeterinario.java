package sv.edu.udb.petyaapp.models;

import com.google.gson.annotations.SerializedName;

public class CitasVeterinario {
    @SerializedName("nombre_mascota")
    public String nombre_mascota;

    @SerializedName("fecha_cita")
    public String fecha_cita;

    @SerializedName("hora")
    public String hora;

    @SerializedName("estado")
    public String estado;

    @SerializedName("cliente_id")
    public int cliente_id;

    //@SerializedName("id_empleado")
    //public int id_empleado;

    public String getNombre_mascota() {
        return nombre_mascota;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public int getCliente_id() {
        return cliente_id;
    }

   // public int getId_empleado() {
      //  return id_empleado;
    //}

}
