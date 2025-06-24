package model;

public class Conferencista extends Asistente {
    private String areaDeEspecialidad;
    private String biografia;

    // Constructor para crear un nuevo conferencista
    public Conferencista(String nombre, String email, String telefono, String areaDeEspecialidad, String biografia) {
        super(nombre, email, telefono);
        this.areaDeEspecialidad = areaDeEspecialidad;
        this.biografia = biografia;
    }

    // Constructor para cargar desde archivo
    public Conferencista(String id, String nombre, String email, String telefono, String areaDeEspecialidad, String biografia) {
        super(id, nombre, email, telefono);
        this.areaDeEspecialidad = areaDeEspecialidad;
        this.biografia = biografia;
    }

    // Getters y Setters
    public String getAreaDeEspecialidad() { return areaDeEspecialidad; }
    public void setAreaDeEspecialidad(String areaDeEspecialidad) { this.areaDeEspecialidad = areaDeEspecialidad; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    @Override
    public String toString() {
        return super.toString() + " - Especialidad: " + areaDeEspecialidad;
    }
}
