package sv.edu.udb.petyaapp.models;

import com.google.gson.annotations.SerializedName;

public class Diagnosticos {
    @SerializedName("nombre_mascota")
    public String nombre_mascota;

    @SerializedName("especie")
    public String especie;

    @SerializedName("raza")
    public String raza;

    @SerializedName("edad")
    public String edad;

    @SerializedName("sexo")
    public String sexo;

    @SerializedName("motivo")
    public String motivo;

    @SerializedName("peso")
    public String peso;

    @SerializedName("pulso")
    public String pulso;

    @SerializedName("temperatura")
    public String temperatura;

    @SerializedName("diagnostico_final")
    public String diagnostico_final;

    @SerializedName("tratamiento")
    public String tratamiento;

    @SerializedName("created_at")
    public String created_at;

    /*
        @SerializedName("hora")
        public String hora;
    */
    public String getNombre_mascota() {
        return nombre_mascota;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public String getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getPeso() {
        return peso;
    }

    public String getPulso() {
        return pulso;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public String getDiagnostico_final() {
        return diagnostico_final;
    }

    public String getTratamiento() {
        return tratamiento;
    }
}
