package view.calendario;

import logic.EventoService;

import model.Evento;
import view.MainFrame;
import view.evento.EventoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class CalendarioPanel extends JPanel {
  private CalendarioTableModel calendarioModel;
  private JTable tablaCalendario;
  private JLabel lblTituloMes;
  private JButton btnAnterior, btnSiguiente, btnHoy;
  private EventoService eventoService;
  private MainFrame parentFrame;

  public CalendarioPanel(EventoService eventoService, MainFrame parentFrame) {
    this.eventoService = eventoService;
    this.parentFrame = parentFrame;

    initializeComponents();
    setupLayout();
    setupEventListeners();
  }

  private void initializeComponents() {
    calendarioModel = new CalendarioTableModel(eventoService);
    tablaCalendario = new JTable(calendarioModel);

    tablaCalendario.setDefaultRenderer(Object.class, new CalendarioCellRenderer());
    tablaCalendario.setRowHeight(80);
    tablaCalendario.setTableHeader(null);
    tablaCalendario.setShowGrid(true);
    tablaCalendario.setGridColor(Color.LIGHT_GRAY);
    tablaCalendario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaCalendario.setCellSelectionEnabled(true);

    for (int i = 0; i < 7; i++) {
      tablaCalendario.getColumnModel().getColumn(i).setPreferredWidth(120);
      tablaCalendario.getColumnModel().getColumn(i).setMinWidth(120);
      tablaCalendario.getColumnModel().getColumn(i).setMaxWidth(120);
    }

    btnAnterior = new JButton("◀ Anterior");
    btnSiguiente = new JButton("Siguiente ▶");
    btnHoy = new JButton("Hoy");

    lblTituloMes = new JLabel();
    lblTituloMes.setHorizontalAlignment(SwingConstants.CENTER);
    lblTituloMes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    actualizarTitulo();
  }

  private void setupLayout() {
    setLayout(new BorderLayout(10, 10));

    JPanel panelNavegacion = new JPanel(new BorderLayout());
    JPanel panelBotones = new JPanel(new FlowLayout());
    panelBotones.add(btnAnterior);
    panelBotones.add(btnHoy);
    panelBotones.add(btnSiguiente);

    panelNavegacion.add(lblTituloMes, BorderLayout.CENTER);
    panelNavegacion.add(panelBotones, BorderLayout.EAST);

    JPanel panelHeaders = createHeaderPanel();
    JPanel panelCalendario = new JPanel(new BorderLayout());
    panelCalendario.add(panelHeaders, BorderLayout.NORTH);
    panelCalendario.add(tablaCalendario, BorderLayout.CENTER);

    JPanel panelLeyenda = createLeyendaPanel();

    add(panelNavegacion, BorderLayout.NORTH);
    add(panelCalendario, BorderLayout.CENTER);
    add(panelLeyenda, BorderLayout.SOUTH);

    setBorder(BorderFactory.createTitledBorder("Calendario de Eventos"));
  }

  private JPanel createHeaderPanel() {
    JPanel panelHeaders = new JPanel(new GridLayout(1, 7));
    String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};

    for (String dia : diasSemana) {
      JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
      lblDia.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
      lblDia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      lblDia.setOpaque(true);
      lblDia.setBackground(new Color(230, 230, 230));
      panelHeaders.add(lblDia);
    }

    return panelHeaders;
  }

  private JPanel createLeyendaPanel() {
    JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelLeyenda.setBorder(BorderFactory.createTitledBorder("Leyenda"));

    panelLeyenda.add(createLeyendaItem("Hoy", new Color(255, 223, 186)));
    panelLeyenda.add(createLeyendaItem("Con eventos", new Color(173, 216, 230)));
    panelLeyenda.add(createLeyendaItem("Fin de semana", new Color(255, 240, 245)));
    panelLeyenda.add(createLeyendaItem("Otro mes", new Color(240, 240, 240)));

    JLabel lblInstrucciones = new JLabel("   |   Doble click: crear evento   |   Click: ver eventos del día");
    lblInstrucciones.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
    lblInstrucciones.setForeground(Color.GRAY);
    panelLeyenda.add(lblInstrucciones);

    return panelLeyenda;
  }

  private JPanel createLeyendaItem(String texto, Color color) {
    JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));

    JLabel cuadrado = new JLabel("  ");
    cuadrado.setOpaque(true);
    cuadrado.setBackground(color);
    cuadrado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    cuadrado.setPreferredSize(new Dimension(15, 15));

    JLabel etiqueta = new JLabel(texto);
    etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

    item.add(cuadrado);
    item.add(etiqueta);
    return item;
  }

  private void setupEventListeners() {
    btnAnterior.addActionListener(e -> cambiarMes(-1));
    btnSiguiente.addActionListener(e -> cambiarMes(1));
    btnHoy.addActionListener(e -> irAHoy());

    tablaCalendario.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = tablaCalendario.rowAtPoint(e.getPoint());
        int col = tablaCalendario.columnAtPoint(e.getPoint());

        if (row >= 0 && col >= 0) {
          if (e.getClickCount() == 2) {
            LocalDate fechaSeleccionada = calendarioModel.getFechaEnPosicion(row, col);
            abrirDialogoNuevoEvento(fechaSeleccionada);
          } else if (e.getClickCount() == 1) {
            CalendarioTableModel.DiaCalendario dia =
                    (CalendarioTableModel.DiaCalendario) calendarioModel.getValueAt(row, col);
            if (dia.tieneEventos()) {
              mostrarEventosDelDia(dia);
            }
          }
        }
      }
    });
  }

  private void cambiarMes(int direccion) {
    YearMonth nuevoMes = calendarioModel.getMesActual().plusMonths(direccion);
    calendarioModel.setMes(nuevoMes);
    actualizarTitulo();
  }

  private void irAHoy() {
    calendarioModel.setMes(YearMonth.now());
    actualizarTitulo();
  }

  private void actualizarTitulo() {
    lblTituloMes.setText(calendarioModel.getTituloMes());
  }

  public void refrescarCalendario() {
    calendarioModel.setMes(calendarioModel.getMesActual());
    actualizarTitulo();
  }

  private void abrirDialogoNuevoEvento(LocalDate fecha) {
    LocalDateTime fechaHoraDefault = fecha.atTime(9, 0);
    Evento eventoDefault = new Evento("", fechaHoraDefault, "", "", 50);

    EventoDialog dialog = new EventoDialog(parentFrame, eventoDefault);
    dialog.setVisible(true);

    if (dialog.isConfirmado()) {
      Evento nuevoEvento = dialog.getEvento();
      eventoService.crearEvento(nuevoEvento);
      refrescarCalendario();
      parentFrame.actualizarVistas();

      JOptionPane.showMessageDialog(this,
              "¡Evento creado exitosamente para el " + fecha + "!",
              "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private void mostrarEventosDelDia(CalendarioTableModel.DiaCalendario dia) {
    if (!dia.tieneEventos()) return;

    StringBuilder mensaje = new StringBuilder();
    mensaje.append("Eventos para ").append(dia.getFecha()).append(":\n\n");

    for (Evento evento : dia.getEventos()) {
      mensaje.append("• ").append(evento.getNombre()).append("\n");
      mensaje.append("  Hora: ").append(evento.getFechaHora().toLocalTime()).append("\n");
      mensaje.append("  Lugar: ").append(evento.getUbicacion()).append("\n");
      mensaje.append("  Asistentes: ").append(evento.getCantidadAsistentes())
              .append("/").append(evento.getCapacidadMaxima()).append("\n\n");
    }

    JOptionPane.showMessageDialog(this, mensaje.toString(),
            "Eventos del Día", JOptionPane.INFORMATION_MESSAGE);
  }}