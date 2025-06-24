package view;

import logic.EventoService;
import model.Asistente;
import model.Evento;
import view.calendario.CalendarioPanel;
import view.evento.DetalleEventoDialog;
import view.evento.EventoDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class MainFrame extends JFrame {
  private EventoService eventoService;
  private JTable tablaEventos;
  private DefaultTableModel tableModel;
  private JButton btnNuevo, btnEditar, btnEliminar, btnDetalles;
  private JButton btnFuturos, btnPasados, btnTodos;
  private JLabel lblEstatus;
  private CalendarioPanel calendarioPanel;
  private JTabbedPane tabbedPane;

  public MainFrame() {
    eventoService = new EventoService();
    initializeComponents();
    setupLayout();
    setupEventListeners();
    actualizarTabla("todos");

    // Datos de prueba si no hay eventos
    if (eventoService.getTotalEventos() == 0) {
      crearDatosPrueba();
    }
  }

  private void initializeComponents() {
    setTitle("Gestión de Eventos");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);

    // Tabla de eventos
    String[] columnas = {"Nombre", "Fecha/Hora", "Ubicación", "Asistentes", "Capacidad"};
    tableModel = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaEventos = new JTable(tableModel);
    tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaEventos.setRowHeight(25);
    tablaEventos.getTableHeader().setReorderingAllowed(false);

    // Botones principales
    btnNuevo = new JButton("➕ Nuevo Evento");
    btnEditar = new JButton("✏  Editar");
    btnEliminar = new JButton("❌ Eliminar");
    btnDetalles = new JButton("👥 Ver Detalles");

    // Botones de filtro
    btnTodos = new JButton("Todos");
    btnFuturos = new JButton("Futuros");
    btnPasados = new JButton("Pasados");

    lblEstatus = new JLabel("✅ Sistema listo - Bienvenido/a");

    // Calendario panel
    calendarioPanel = new CalendarioPanel(eventoService, this);

    // TabbedPane para alternar entre vistas
    tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

    // Deshabilitar botones que requieren selección
    btnEditar.setEnabled(false);
    btnEliminar.setEnabled(false);
    btnDetalles.setEnabled(false);
  }

  private void setupLayout() {
    setLayout(new BorderLayout(10, 10));

    // Panel superior con filtros y botones
    JPanel panelSuperior = createPanelSuperior();

    // Vista de lista (tab 1)
    JScrollPane scrollPane = new JScrollPane(tablaEventos);
    scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Lista de Eventos"));

    JPanel panelLista = new JPanel(new BorderLayout(5, 5));
    panelLista.add(panelSuperior, BorderLayout.NORTH);
    panelLista.add(scrollPane, BorderLayout.CENTER);

    // Agregar tabs con iconos
    tabbedPane.addTab("📋 Lista de Eventos", panelLista);
    tabbedPane.addTab("📅 Calendario", calendarioPanel);

    // Panel de estadísticas
    JPanel panelEstadisticas = createPanelEstadisticas();

    // Layout principal
    add(tabbedPane, BorderLayout.CENTER);
    add(panelEstadisticas, BorderLayout.SOUTH);

    // Márgenes
    ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  private JPanel createPanelSuperior() {
    JPanel panelSuperior = new JPanel(new BorderLayout(10, 5));

    // Panel de filtros
    JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelFiltros.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
    panelFiltros.add(new JLabel("Mostrar:"));
    panelFiltros.add(btnTodos);
    panelFiltros.add(btnFuturos);
    panelFiltros.add(btnPasados);

    // Panel de botones de acción
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelBotones.setBorder(BorderFactory.createTitledBorder("\uD83D\uDD27 Acciones"));
    panelBotones.add(btnNuevo);
    panelBotones.add(btnEditar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnDetalles);

    panelSuperior.add(panelFiltros, BorderLayout.WEST);
    panelSuperior.add(panelBotones, BorderLayout.EAST);

    return panelSuperior;
  }

  private JPanel createPanelEstadisticas() {
    JPanel panelEstadisticas = new JPanel(new BorderLayout());
    panelEstadisticas.setBorder(BorderFactory.createTitledBorder("📊 Estado del Sistema"));

    // Status principal
    lblEstatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Botón de refresh
    JButton btnRefresh = new JButton("🔄 Actualizar");
    btnRefresh.addActionListener(e -> {
      actualizarVistas();
      lblEstatus.setText("✅ Sistema actualizado - " + java.time.LocalTime.now().toString().substring(0, 8));
    });

    panelEstadisticas.add(lblEstatus, BorderLayout.CENTER);
    panelEstadisticas.add(btnRefresh, BorderLayout.EAST);

    return panelEstadisticas;
  }

  private void setupEventListeners() {
    // Selección de tabla
    tablaEventos.getSelectionModel().addListSelectionListener(e -> {
      boolean haySeleccion = tablaEventos.getSelectedRow() != -1;
      btnEditar.setEnabled(haySeleccion);
      btnEliminar.setEnabled(haySeleccion);
      btnDetalles.setEnabled(haySeleccion);
    });

    // Botones de filtro
    btnTodos.addActionListener(e -> {
      actualizarTabla("todos");
      btnTodos.setBackground(Color.LIGHT_GRAY);
      btnFuturos.setBackground(null);
      btnPasados.setBackground(null);
    });

    btnFuturos.addActionListener(e -> {
      actualizarTabla("futuros");
      btnFuturos.setBackground(Color.LIGHT_GRAY);
      btnTodos.setBackground(null);
      btnPasados.setBackground(null);
    });

    btnPasados.addActionListener(e -> {
      actualizarTabla("pasados");
      btnPasados.setBackground(Color.LIGHT_GRAY);
      btnTodos.setBackground(null);
      btnFuturos.setBackground(null);
    });

    // Botones principales
    btnNuevo.addActionListener(e -> nuevoEvento());
    btnEditar.addActionListener(e -> editarEvento());
    btnEliminar.addActionListener(e -> eliminarEvento());
    btnDetalles.addActionListener(e -> verDetalles());

    // Listener para cambio de tabs
    tabbedPane.addChangeListener(e -> {
      if (tabbedPane.getSelectedIndex() == 1) { // Tab del calendario
        calendarioPanel.refrescarCalendario();
        lblEstatus.setText("📅 Vista de calendario activa");
      } else {
        lblEstatus.setText("📋 Vista de lista activa");
      }
    });

    // Doble click en tabla para ver detalles
    tablaEventos.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 2 && tablaEventos.getSelectedRow() != -1) {
          verDetalles();
        }
      }
    });
  }

  private void actualizarTabla(String filtro) {
    tableModel.setRowCount(0);

    List<Evento> eventos;
    String mensajeFiltro;

    switch (filtro) {
      case "futuros":
        eventos = eventoService.obtenerEventosFuturos();
        mensajeFiltro = "eventos futuros";
        break;
      case "pasados":
        eventos = eventoService.obtenerEventosPasados();
        mensajeFiltro = "eventos pasados";
        break;
      default:
        eventos = eventoService.obtenerTodosLosEventos();
        mensajeFiltro = "todos los eventos";
    }

    for (Evento evento : eventos) {
      Object[] fila = {
              evento.getNombre(),
              evento.getFechaFormateada(),
              evento.getUbicacion(),
              evento.getCantidadAsistentes(),
              evento.getCapacidadMaxima()
      };
      tableModel.addRow(fila);
    }

    actualizarEstatus(eventos.size(), mensajeFiltro);
  }

  private void actualizarEstatus(int cantidadMostrada, String filtro) {
    String estadisticas = String.format(
            "📊 Mostrando %d %s | Total: %d eventos, %d asistentes registrados",
            cantidadMostrada, filtro,
            eventoService.getTotalEventos(),
            eventoService.getTotalAsistentes()
    );
    lblEstatus.setText(estadisticas);
  }

  private void nuevoEvento() {
    EventoDialog dialog = new EventoDialog(this, null);
    dialog.setVisible(true);

    if (dialog.isConfirmado()) {
      Evento nuevoEvento = dialog.getEvento();
      eventoService.crearEvento(nuevoEvento);
      actualizarVistas();

      lblEstatus.setText("✅ Evento '" + nuevoEvento.getNombre() + "' creado exitosamente");

      // Cambiar al tab del calendario si el evento es futuro
      if (nuevoEvento.esFuturo()) {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Quieres ver el evento en el calendario?",
                "Evento Creado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
          tabbedPane.setSelectedIndex(1); // Cambiar a tab calendario
        }
      }
    }
  }

  private void editarEvento() {
    int filaSeleccionada = tablaEventos.getSelectedRow();
    if (filaSeleccionada == -1) return;

    String nombreEvento = (String) tableModel.getValueAt(filaSeleccionada, 0);
    Evento evento = buscarEventoPorNombre(nombreEvento);

    if (evento != null) {
      EventoDialog dialog = new EventoDialog(this, evento);
      dialog.setVisible(true);

      if (dialog.isConfirmado()) {
        eventoService.actualizarEvento(dialog.getEvento());
        actualizarVistas();
        lblEstatus.setText("✅ Evento '" + evento.getNombre() + "' actualizado exitosamente");
      }
    }
  }

  private void eliminarEvento() {
    int filaSeleccionada = tablaEventos.getSelectedRow();
    if (filaSeleccionada == -1) return;

    String nombreEvento = (String) tableModel.getValueAt(filaSeleccionada, 0);

    int confirmacion = JOptionPane.showConfirmDialog(this,
            "⚠️ ¿Estás seguro de que quieres eliminar el evento '" + nombreEvento + "'?\n" +
                    "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (confirmacion == JOptionPane.YES_OPTION) {
      Evento evento = buscarEventoPorNombre(nombreEvento);
      if (evento != null) {
        eventoService.eliminarEvento(evento.getId());
        actualizarVistas();
        lblEstatus.setText("🗑️ Evento '" + nombreEvento + "' eliminado exitosamente");
      }
    }
  }

  private void verDetalles() {
    int filaSeleccionada = tablaEventos.getSelectedRow();
    if (filaSeleccionada == -1) return;

    String nombreEvento = (String) tableModel.getValueAt(filaSeleccionada, 0);
    Evento evento = buscarEventoPorNombre(nombreEvento);

    if (evento != null) {
      DetalleEventoDialog dialog = new DetalleEventoDialog(this, evento, eventoService);
      dialog.setVisible(true);
      actualizarVistas();
      lblEstatus.setText("👁️ Consultando detalles de '" + evento.getNombre() + "'");
    }
  }

  // Método público para actualizar todas las vistas
  public void actualizarVistas() {
    actualizarTabla("todos");
    btnTodos.setBackground(Color.LIGHT_GRAY);
    btnFuturos.setBackground(null);
    btnPasados.setBackground(null);
    calendarioPanel.refrescarCalendario();
  }

  private Evento buscarEventoPorNombre(String nombre) {
    return eventoService.obtenerTodosLosEventos().stream()
            .filter(e -> e.getNombre().equals(nombre))
            .findFirst()
            .orElse(null);
  }

  private void crearDatosPrueba() {
    // Crear eventos de ejemplo variados
    Evento conferencia = new Evento("🚀 Conferencia Tech 2025",
            LocalDateTime.now().plusDays(15).withHour(9).withMinute(0),
            "Centro de Convenciones",
            "Conferencia anual de tecnología e innovación", 200);

    Evento reunion = new Evento("📊 Reunión de Equipo",
            LocalDateTime.now().plusDays(3).withHour(14).withMinute(30),
            "Sala de Juntas A",
            "Reunión mensual del equipo de desarrollo", 15);

    Evento workshop = new Evento("⚛️ Workshop React Avanzado",
            LocalDateTime.now().minusDays(5).withHour(10).withMinute(0),
            "Aula 301",
            "Taller práctico de React Hooks y Context API", 30);

    Evento networking = new Evento("🤝 Networking Night",
            LocalDateTime.now().plusDays(7).withHour(19).withMinute(0),
            "Rooftop Plaza Hotel",
            "Evento de networking para profesionales IT", 100);

    eventoService.crearEvento(conferencia);
    eventoService.crearEvento(reunion);
    eventoService.crearEvento(workshop);
    eventoService.crearEvento(networking);

    // Agregar algunos asistentes de ejemplo
    eventoService.registrarAsistente(conferencia.getId(),
            new Asistente("Juan Pérez", "juan.perez@email.com", "123456789"));
    eventoService.registrarAsistente(conferencia.getId(),
            new Asistente("María García", "maria.garcia@email.com", "987654321"));
    eventoService.registrarAsistente(reunion.getId(),
            new Asistente("Carlos López", "carlos.lopez@email.com", "555666777"));
    eventoService.registrarAsistente(networking.getId(),
            new Asistente("Ana Martínez", "ana.martinez@email.com", "444555666"));

    lblEstatus.setText("🎯 Datos de ejemplo cargados - " + eventoService.getTotalEventos() + " eventos creados");
  }
}