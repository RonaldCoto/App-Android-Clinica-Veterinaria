package sv.edu.udb.petyaapp.models;

public class FormCitas {
    private int id;
    private String fecha_cita;
    private String hora;
    private String nombre_mascota;
    private String especie;
    private String raza;
    private String edad;
    private String sexo;
    private String color;
    private String vacunacion;
    private String motivo;
    private String estado;
    private int id_empleado;
    private String peso;
    private String pulso;
    private String temperatura;
    private int cliente_id;
    private String cliente_uid;

    public FormCitas(
            String fecha_cita,
            String hora,
            String nombre_mascota,
            String especie,
            String raza,
            String edad,
            String sexo,
            String motivo,
            String estado,
            int id_empleado,
            int cliente_id,
            String cliente_uid){

        this.fecha_cita = fecha_cita;
        this.hora = hora;
        this.nombre_mascota = nombre_mascota;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.motivo = motivo;
        this.estado = estado;
        this.id_empleado = id_empleado;
        this.cliente_id = cliente_id;
        this.cliente_uid = cliente_uid;
    }

    public int getId() { return id; }


    public String getFecha_cita() { return fecha_cita; }

    public void setFecha_cita(String fecha_cita) { this.fecha_cita = fecha_cita; }



    public String getHora() { return hora; }

    public void setHora(String hora) { this.hora = hora; }



    public String getNombre_mascota() { return nombre_mascota; }

    public void setNombre_mascota(String nombre_mascota) { this.nombre_mascota = nombre_mascota; }



    public String getEspecie() { return especie; }

    public void setEspecie(String especie) { this.especie = especie; }



    public String getRaza() { return raza; }

    public void setRaza(String raza) { this.raza = raza; }



    public String getEdad() { return edad; }

    public void setEdad(String edad) { this.edad = edad; }



    public String getSexo() { return sexo; }

    public void setSexo(String sexo) { this.sexo = sexo; }



    public String getMotivo() { return motivo; }

    public void setMotivo(String motivo) { this.motivo = motivo; }



    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }



    public int getId_empleado() { return id_empleado; }

    public void setId_empleado(int id_empleado) { this.id_empleado = id_empleado; }



    public int getCliente_id() { return cliente_id; }

    public void setCliente_id(int cliente_id) { this.cliente_id = cliente_id; }


    public String getCliente_uid() { return cliente_uid; }

    public void setCliente_uid(String cliente_uid) { this.cliente_uid = cliente_uid; }
}
