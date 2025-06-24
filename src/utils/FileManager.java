package utils;

import model.Asistente;
import model.Evento;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileManager {
  private static final String EVENTOS_FILE = "eventos.txt";
  private static final String ASISTENTES_FILE = "asistentes.txt";
  private static final String DELIMITER = "|";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  // Guardar eventos
  public void guardarEventos(List<Evento> eventos) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(EVENTOS_FILE))) {
      for (Evento evento : eventos) {
        String linea = String.join(DELIMITER,
                evento.getId(),
                evento.getNombre(),
                evento.getFechaHora().format(DATE_FORMATTER),
                evento.getUbicacion(),
                evento.getDescripcion(),
                String.valueOf(evento.getCapacidadMaxima())
        );
        writer.println(linea);
      }
    } catch (IOException e) {
      System.err.println("Error guardando eventos: " + e.getMessage());
    }
  }

  // Cargar eventos
  public List<Evento> cargarEventos() {
    List<Evento> eventos = new ArrayList<>();
    File file = new File(EVENTOS_FILE);

    if (!file.exists()) {
      return eventos;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String linea;
      while ((linea = reader.readLine()) != null) {
        String[] partes = linea.split("\\" + DELIMITER);
        if (partes.length == 6) {
          try {
            String id = partes[0];
            String nombre = partes[1];
            LocalDateTime fechaHora = LocalDateTime.parse(partes[2], DATE_FORMATTER);
            String ubicacion = partes[3];
            String descripcion = partes[4];
            int capacidad = Integer.parseInt(partes[5]);

            eventos.add(new Evento(id, nombre, fechaHora, ubicacion, descripcion, capacidad));
          } catch (Exception e) {
            System.err.println("Error parseando evento: " + linea);
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Error cargando eventos: " + e.getMessage());
    }

    return eventos;
  }

  // Guardar asistentes por evento
  public void guardarAsistentes(Map<String, List<Asistente>> asistentesPorEvento) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(ASISTENTES_FILE))) {
      for (Map.Entry<String, List<Asistente>> entry : asistentesPorEvento.entrySet()) {
        String eventoId = entry.getKey();
        for (Asistente asistente : entry.getValue()) {
          String linea = String.join(DELIMITER,
                  eventoId,
                  asistente.getId(),
                  asistente.getNombre(),
                  asistente.getEmail(),
                  asistente.getTelefono()
          );
          writer.println(linea);
        }
      }
    } catch (IOException e) {
      System.err.println("Error guardando asistentes: " + e.getMessage());
    }
  }

  // Cargar asistentes por evento
  public Map<String, List<Asistente>> cargarAsistentes() {
    Map<String, List<Asistente>> asistentesPorEvento = new HashMap<>();
    File file = new File(ASISTENTES_FILE);

    if (!file.exists()) {
      return asistentesPorEvento;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String linea;
      while ((linea = reader.readLine()) != null) {
        String[] partes = linea.split("\\" + DELIMITER);
        if (partes.length == 5) {
          try {
            String eventoId = partes[0];
            String asistenteId = partes[1];
            String nombre = partes[2];
            String email = partes[3];
            String telefono = partes[4];

            Asistente asistente = new Asistente(asistenteId, nombre, email, telefono);

            asistentesPorEvento.computeIfAbsent(eventoId, k -> new ArrayList<>()).add(asistente);
          } catch (Exception e) {
            System.err.println("Error parseando asistente: " + linea);
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Error cargando asistentes: " + e.getMessage());
    }

    return asistentesPorEvento;
  }

  // Verificar si los archivos existen
  public boolean archivosExisten() {
    return new File(EVENTOS_FILE).exists() && new File(ASISTENTES_FILE).exists();
  }
}