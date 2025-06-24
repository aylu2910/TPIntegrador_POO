package logic;

import model.Asistente;
import model.Evento;
import utils.FileManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EventoService {
  private List<Evento> eventos;
  private FileManager fileManager;

  public EventoService() {
    this.fileManager = new FileManager();
    this.eventos = new ArrayList<>();
    cargarDatos();
  }

  // Cargar datos desde archivos
  private void cargarDatos() {
    eventos = fileManager.cargarEventos();
    Map<String, List<Asistente>> asistentesPorEvento = fileManager.cargarAsistentes();

    // Asignar asistentes a eventos
    for (Evento evento : eventos) {
      List<Asistente> asistentes = asistentesPorEvento.getOrDefault(evento.getId(), new ArrayList<>());
      evento.setAsistentes(asistentes);
    }
  }

  // Guardar todos los datos
  public void guardarDatos() {
    fileManager.guardarEventos(eventos);

    // Crear mapa de asistentes por evento
    Map<String, List<Asistente>> asistentesPorEvento = new HashMap<>();
    for (Evento evento : eventos) {
      if (!evento.getAsistentes().isEmpty()) {
        asistentesPorEvento.put(evento.getId(), evento.getAsistentes());
      }
    }

    fileManager.guardarAsistentes(asistentesPorEvento);
  }

  // CRUD Eventos
  public void crearEvento(Evento evento) {
    eventos.add(evento);
    guardarDatos();
  }

  public void actualizarEvento(Evento eventoActualizado) {
    for (int i = 0; i < eventos.size(); i++) {
      if (eventos.get(i).getId().equals(eventoActualizado.getId())) {
        eventos.set(i, eventoActualizado);
        guardarDatos();
        return;
      }
    }
  }

  public void eliminarEvento(String eventoId) {
    eventos.removeIf(e -> e.getId().equals(eventoId));
    guardarDatos();
  }

  public Evento obtenerEventoPorId(String id) {
    return eventos.stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElse(null);
  }

  // Obtener eventos filtrados
  public List<Evento> obtenerTodosLosEventos() {
    return new ArrayList<>(eventos);
  }

  public List<Evento> obtenerEventosFuturos() {
    LocalDateTime ahora = LocalDateTime.now();
    return eventos.stream()
            .filter(e -> e.getFechaHora().isAfter(ahora))
            .sorted((e1, e2) -> e1.getFechaHora().compareTo(e2.getFechaHora()))
            .collect(Collectors.toList());
  }

  public List<Evento> obtenerEventosPasados() {
    LocalDateTime ahora = LocalDateTime.now();
    return eventos.stream()
            .filter(e -> e.getFechaHora().isBefore(ahora))
            .sorted((e1, e2) -> e2.getFechaHora().compareTo(e1.getFechaHora())) // Más recientes primero
            .collect(Collectors.toList());
  }

  // Gestión de asistentes
  public boolean registrarAsistente(String eventoId, Asistente asistente) {
    Evento evento = obtenerEventoPorId(eventoId);
    if (evento != null && evento.agregarAsistente(asistente)) {
      guardarDatos();
      return true;
    }
    return false;
  }

  public boolean removerAsistente(String eventoId, String asistenteId) {
    Evento evento = obtenerEventoPorId(eventoId);
    if (evento != null && evento.removerAsistente(asistenteId)) {
      guardarDatos();
      return true;
    }
    return false;
  }

  // Validaciones
  public boolean existeEvento(String nombre, LocalDateTime fechaHora) {
    return eventos.stream()
            .anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre) &&
                    e.getFechaHora().equals(fechaHora));
  }

  public int getTotalEventos() {
    return eventos.size();
  }

  public int getTotalAsistentes() {
    return eventos.stream()
            .mapToInt(e -> e.getCantidadAsistentes())
            .sum();
  }

  // Métodos de búsqueda
  public List<Evento> buscarEventos(String filtro) {
    String filtroLower = filtro.toLowerCase();
    return eventos.stream()
            .filter(e -> e.getNombre().toLowerCase().contains(filtroLower) ||
                    e.getUbicacion().toLowerCase().contains(filtroLower) ||
                    e.getDescripcion().toLowerCase().contains(filtroLower))
            .collect(Collectors.toList());
  }
}