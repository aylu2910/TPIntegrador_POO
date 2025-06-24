package model;

import java.util.UUID;

public class Asistente {
  private String id;
  private String nombre;
  private String email;
  private String telefono;

  // Constructor para nuevos asistentes
  public Asistente(String nombre, String email, String telefono) {
    this.id = UUID.randomUUID().toString().substring(0, 8);
    this.nombre = nombre;
    this.email = email;
    this.telefono = telefono;
  }

  // Constructor para cargar desde archivo
  public Asistente(String id, String nombre, String email, String telefono) {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.telefono = telefono;
  }

  // Getters y Setters
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getTelefono() { return telefono; }
  public void setTelefono(String telefono) { this.telefono = telefono; }

  // Validaciones básicas
  public boolean isValidEmail() {
    return email != null && email.contains("@") && email.contains(".");
  }

  public boolean isValidTelefono() {
    return telefono != null && telefono.trim().length() >= 7;
  }

  public boolean isValid() {
    return nombre != null && !nombre.trim().isEmpty() &&
            isValidEmail() && isValidTelefono();
  }

  @Override
  public String toString() {
    return nombre + " (" + email + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Asistente asistente = (Asistente) obj;
    return id.equals(asistente.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}