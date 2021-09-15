package sv.edu.udb.petyaapp.models;

public class Clientes {

    private int id;
    private String nombre;
    private String correo;
    private String foto;
    private String telefono;
    private String contraseña;
    private String categoria;

    public Clientes(

                      String nombre,
                      String correo,
                      String foto,
                      String telefono,
                      String contraseña,
                      String categoria){


        this.nombre=nombre;
        this.correo=correo;
        this.foto=foto;
        this.telefono=telefono;
        this.contraseña=contraseña;
        this.categoria=categoria;
    }


    public int getId(){ return id; }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFoto() {
        return foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getCategoria() {
        return categoria;
    }
}
