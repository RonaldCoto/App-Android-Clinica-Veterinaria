package sv.edu.udb.petyaapp.models;

public class Horarios {
    private String dia;
    private String hora_inicio;
    private String hora_fin;
    private int empleado_id;

    public Horarios(String dia, String hora_inicio, String hora_fin, int empleado_id ){
        this.dia = dia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.empleado_id = empleado_id;
    }

    public String getDia() { return dia; }

    public String getHora_inicio() { return hora_inicio; }

    public String getHora_fin() { return hora_fin; }

    public int getEmpleado_id() { return empleado_id; }


}
