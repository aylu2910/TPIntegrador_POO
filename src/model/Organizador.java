package model;

import java.util.List;

class Organizador extends Asistente {
  private String rol;
  private List<String> responsabilidades;

  public Organizador(String nombre, String email, String telefono, String rol, List<String> responsabilidades) {
    super(nombre, email, telefono);
    this.rol = rol;
    this.responsabilidades = responsabilidades;
  }

  // Constructor para cargar desde archivo
  public Organizador(String id, String nombre, String email, String telefono, String rol, List<String> responsabilidades) {
    super(id, nombre, email, telefono);
    this.rol = rol;
    this.responsabilidades = responsabilidades;
  }

  // Setters y Getters
  public String getRol() {
    return rol;
  }
  public void setRol(String rol) {
    this.rol = rol;
  }
  public List<String> getResponsabilidades() {
    return responsabilidades;
  }
  public void setResponsabilidades(List<String> responsabilidades) {
    this.responsabilidades = responsabilidades;
  }
  @Override
  public String toString() {
    return super.toString() + " - Rol: " + rol + " - Responsabilidades: " + String.join(", ", responsabilidades);
  }
}
