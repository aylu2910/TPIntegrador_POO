package view.calendario;

import model.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;

public class CalendarioCellRenderer extends DefaultTableCellRenderer {
  private static final Color COLOR_HOY = new Color(255, 223, 186);
  private static final Color COLOR_CON_EVENTOS = new Color(173, 216, 230);
  private static final Color COLOR_OTRO_MES = new Color(240, 240, 240);
  private static final Color COLOR_WEEKEND = new Color(255, 240, 245);
  private static final Color COLOR_NORMAL = Color.WHITE;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus,
                                                 int row, int column) {

    CalendarioTableModel.DiaCalendario dia = (CalendarioTableModel.DiaCalendario) value;

    setText(createCellText(dia));
    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.TOP);

    configurarColores(dia, isSelected, column, table);
    configurarBorde(dia);
    configurarFont(dia);
    setToolTipText(createTooltip(dia));

    return this;
  }

  private String createCellText(CalendarioTableModel.DiaCalendario dia) {
    StringBuilder html = new StringBuilder("<html><div style='text-align: center;'>");

    html.append("<div style='font-size: 12px; margin: 2px;'>");
    html.append(dia.getFecha().getDayOfMonth());
    html.append("</div>");

    if (dia.tieneEventos()) {
      html.append("<div style='font-size: 8px; color: green; margin: 1px;'>");
      html.append("● ").append(dia.getCantidadEventos());
      html.append(dia.getCantidadEventos() == 1 ? " evento" : " eventos");
      html.append("</div>");
    }

    html.append("</div></html>");
    return html.toString();
  }

  private void configurarColores(CalendarioTableModel.DiaCalendario dia, boolean isSelected,
                                 int column, JTable table) {
    if (isSelected) {
      setBackground(table.getSelectionBackground());
      setForeground(table.getSelectionForeground());
    } else {
      Color backgroundColor;
      Color textColor = Color.BLACK;

      if (dia.getFecha().equals(LocalDate.now())) {
        backgroundColor = COLOR_HOY;
      } else if (!dia.isDelMesActual()) {
        backgroundColor = COLOR_OTRO_MES;
        textColor = Color.GRAY;
      } else if (dia.tieneEventos()) {
        backgroundColor = COLOR_CON_EVENTOS;
        textColor = new Color(0, 100, 0);
      } else if (column == 0 || column == 6) {
        backgroundColor = COLOR_WEEKEND;
      } else {
        backgroundColor = COLOR_NORMAL;
      }

      setBackground(backgroundColor);
      setForeground(textColor);
    }
    setOpaque(true);
  }

  private void configurarBorde(CalendarioTableModel.DiaCalendario dia) {
    if (dia.getFecha().equals(LocalDate.now())) {
      setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    } else if (dia.tieneEventos()) {
      setBorder(BorderFactory.createLineBorder(new Color(0, 100, 200), 1));
    } else {
      setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }
  }

  private void configurarFont(CalendarioTableModel.DiaCalendario dia) {
    Font font = getFont();
    if (dia.getFecha().equals(LocalDate.now())) {
      setFont(font.deriveFont(Font.BOLD));
    } else {
      setFont(font.deriveFont(Font.PLAIN));
    }
  }

  private String createTooltip(CalendarioTableModel.DiaCalendario dia) {
    if (!dia.tieneEventos()) {
      return dia.getFecha().toString() + " - Sin eventos";
    }

    StringBuilder tooltip = new StringBuilder("<html>");
    tooltip.append("<b>").append(dia.getFecha()).append("</b><br>");
    tooltip.append("Eventos:<br>");

    for (Evento evento : dia.getEventos()) {
      tooltip.append("• <b>").append(evento.getNombre()).append("</b><br>");
      tooltip.append("  ").append(evento.getFechaFormateada()).append("<br>");
      tooltip.append("  ").append(evento.getUbicacion()).append("<br>");
      tooltip.append("  Asistentes: ").append(evento.getCantidadAsistentes())
              .append("/").append(evento.getCapacidadMaxima()).append("<br><br>");
    }

    tooltip.append("</html>");
    return tooltip.toString();
  }
}
