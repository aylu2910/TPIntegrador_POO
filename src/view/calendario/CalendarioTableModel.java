package view.calendario;
import logic.EventoService;
import model.Evento;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CalendarioTableModel extends AbstractTableModel {
  private static final String[] DIAS_SEMANA = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
  private static final int FILAS = 6;
  private static final int COLUMNAS = 7;

  private YearMonth mesActual;
  private LocalDate primerDiaGrid;
  private EventoService eventoService;
  private List<Evento> eventosDelMes;

  public CalendarioTableModel(EventoService eventoService) {
    this.eventoService = eventoService;
    this.mesActual = YearMonth.now();
    calcularPrimerDia();
    cargarEventosDelMes();
  }

  @Override
  public int getRowCount() { return FILAS; }

  @Override
  public int getColumnCount() { return COLUMNAS; }

  @Override
  public String getColumnName(int column) { return DIAS_SEMANA[column]; }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    LocalDate fecha = primerDiaGrid.plusDays(rowIndex * 7 + columnIndex);

    DiaCalendario dia = new DiaCalendario();
    dia.setFecha(fecha);
    dia.setDelMesActual(fecha.getMonth() == mesActual.getMonth() &&
            fecha.getYear() == mesActual.getYear());

    List<Evento> eventosDelDia = eventosDelMes.stream()
            .filter(evento -> evento.getFechaHora().toLocalDate().equals(fecha))
            .collect(Collectors.toList());

    dia.setEventos(eventosDelDia);
    return dia;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

  public void setMes(YearMonth nuevoMes) {
    this.mesActual = nuevoMes;
    calcularPrimerDia();
    cargarEventosDelMes();
    fireTableDataChanged();
  }

  public YearMonth getMesActual() { return mesActual; }

  public String getTituloMes() {
    return mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) +
            " " + mesActual.getYear();
  }

  public LocalDate getFechaEnPosicion(int row, int column) {
    return primerDiaGrid.plusDays(row * 7 + column);
  }

  private void calcularPrimerDia() {
    LocalDate primerDiaDelMes = mesActual.atDay(1);
    int diaSemana = primerDiaDelMes.getDayOfWeek().getValue() % 7;
    primerDiaGrid = primerDiaDelMes.minusDays(diaSemana);
  }

  private void cargarEventosDelMes() {
    LocalDateTime inicioMes = mesActual.atDay(1).atStartOfDay();
    LocalDateTime finMes = mesActual.atEndOfMonth().atTime(23, 59, 59);

    eventosDelMes = eventoService.obtenerTodosLosEventos().stream()
            .filter(evento -> {
              LocalDateTime fechaEvento = evento.getFechaHora();
              return !fechaEvento.isBefore(inicioMes) && !fechaEvento.isAfter(finMes);
            })
            .collect(Collectors.toList());
  }

  public static class DiaCalendario {
    private LocalDate fecha;
    private boolean delMesActual;
    private List<Evento> eventos;

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isDelMesActual() { return delMesActual; }
    public void setDelMesActual(boolean delMesActual) { this.delMesActual = delMesActual; }

    public List<Evento> getEventos() { return eventos; }
    public void setEventos(List<Evento> eventos) { this.eventos = eventos; }

    public int getCantidadEventos() { return eventos != null ? eventos.size() : 0; }
    public boolean tieneEventos() { return getCantidadEventos() > 0; }

    @Override
    public String toString() { return String.valueOf(fecha.getDayOfMonth()); }
  }
}
