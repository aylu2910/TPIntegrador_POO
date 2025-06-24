package model;

import java.util.List;

public class Invitado extends Asistente {
    private List<String> intereses;
    private String comentarios;

    public Invitado(String nombre, String email, String telefono, List<String> intereses, String comentarios) {
        super(nombre, email, telefono);
        this.intereses = intereses;
        this.comentarios = comentarios;
    }

    // Constructor para cargar desde archivo
    public Invitado(String id, String nombre, String email, String telefono, List<String> intereses, String comentarios) {
        super(id, nombre, email, telefono);
        this.intereses = intereses;
        this.comentarios = comentarios;
    }

    // Setters y Getters
    public List<String> getIntereses() {
        return intereses;
    }
    public void setIntereses(List<String> intereses) {
        this.intereses = intereses;
    }
    public String getComentarios() {
        return comentarios;
    }
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return super.toString() + " - Intereses: " + String.join(", ", intereses);
    }

}
