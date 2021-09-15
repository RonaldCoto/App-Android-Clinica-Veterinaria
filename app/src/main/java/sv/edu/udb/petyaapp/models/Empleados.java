package sv.edu.udb.petyaapp.models;

public class Empleados {
    private int id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String foto_perfil;
    private String telefono;
    private String password;
    private String categoria;

    public Empleados(
             int id,
             String nombres,
             String apellidos,
             String correo,
             String foto_perfil,
             String telefono,
             String password,
             String categoria){

        this.id = id;
        this.nombres=nombres;
        this.apellidos=apellidos;
        this.correo=correo;
        this.foto_perfil=foto_perfil;
        this.telefono=telefono;
        this.password=password;
        this.categoria=categoria;
    }

    public int getId(){ return id; }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFoto() {
        return foto_perfil;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContraseña() {
        return password;
    }

    public String getCategoria() {
        return categoria;
    }
}
