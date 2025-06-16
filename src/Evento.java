import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Evento {
  private String id;
  private String nombre;
  private LocalDateTime fechaHora;
  private String ubicacion;
  private String descripcion;
  private int capacidadMaxima;
  private List<Asistente> asistentes;

  // Constructor para nuevos eventos
  public Evento(String nombre, LocalDateTime fechaHora, String ubicacion, String descripcion, int capacidadMaxima) {
    this.id = UUID.randomUUID().toString().substring(0, 8);
    this.nombre = nombre;
    this.fechaHora = fechaHora;
    this.ubicacion = ubicacion;
    this.descripcion = descripcion;
    this.capacidadMaxima = capacidadMaxima;
    this.asistentes = new ArrayList<>();
  }

  // Constructor para cargar desde archivo
  public Evento(String id, String nombre, LocalDateTime fechaHora, String ubicacion, String descripcion, int capacidadMaxima) {
    this.id = id;
    this.nombre = nombre;
    this.fechaHora = fechaHora;
    this.ubicacion = ubicacion;
    this.descripcion = descripcion;
    this.capacidadMaxima = capacidadMaxima;
    this.asistentes = new ArrayList<>();
  }

  // Getters y Setters
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public LocalDateTime getFechaHora() { return fechaHora; }
  public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

  public String getUbicacion() { return ubicacion; }
  public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

  public int getCapacidadMaxima() { return capacidadMaxima; }
  public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

  public List<Asistente> getAsistentes() { return asistentes; }
  public void setAsistentes(List<Asistente> asistentes) { this.asistentes = asistentes; }

  // Métodos útiles
  public int getCantidadAsistentes() {
    return asistentes.size();
  }

  public boolean puedeAgregarAsistente() {
    return asistentes.size() < capacidadMaxima;
  }

  public boolean agregarAsistente(Asistente asistente) {
    if (puedeAgregarAsistente()) {
      asistentes.add(asistente);
      return true;
    }
    return false;
  }

  public boolean removerAsistente(String asistenteId) {
    return asistentes.removeIf(a -> a.getId().equals(asistenteId));
  }

  public String getFechaFormateada() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return fechaHora.format(formatter);
  }

  public boolean esFuturo() {
    return fechaHora.isAfter(LocalDateTime.now());
  }

  @Override
  public String toString() {
    return nombre + " - " + getFechaFormateada() + " (" + getCantidadAsistentes() + "/" + capacidadMaxima + ")";
  }
}