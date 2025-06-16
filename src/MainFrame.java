import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class MainFrame extends JFrame {
  private EventoService eventoService;
  private JTable tablaEventos;
  private DefaultTableModel tableModel;
  private JButton btnNuevo, btnEditar, btnEliminar, btnDetalles;
  private JButton btnFuturos, btnPasados, btnTodos;
  private JLabel lblEstatus;

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
    setSize(1000, 700);
    setLocationRelativeTo(null);

    // Tabla de eventos
    String[] columnas = {"Nombre", "Fecha/Hora", "Ubicación", "Asistentes", "Capacidad"};
    tableModel = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Tabla no editable
      }
    };

    tablaEventos = new JTable(tableModel);
    tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaEventos.setRowHeight(25);
    tablaEventos.getTableHeader().setReorderingAllowed(false);

    // Botones principales
    btnNuevo = new JButton("Nuevo Evento");
    btnEditar = new JButton("Editar");
    btnEliminar = new JButton("Eliminar");
    btnDetalles = new JButton("Ver Detalles");

    // Botones de filtro
    btnTodos = new JButton("Todos");
    btnFuturos = new JButton("Futuros");
    btnPasados = new JButton("Pasados");

    // Label de estatus
    lblEstatus = new JLabel("Listo");

    // Deshabilitar botones que requieren selección
    btnEditar.setEnabled(false);
    btnEliminar.setEnabled(false);
    btnDetalles.setEnabled(false);
  }

  private void setupLayout() {
    setLayout(new BorderLayout(10, 10));

    // Panel superior con filtros
    JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
    panelFiltros.add(new JLabel("Mostrar:"));
    panelFiltros.add(btnTodos);
    panelFiltros.add(btnFuturos);
    panelFiltros.add(btnPasados);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout());
    panelBotones.add(btnNuevo);
    panelBotones.add(btnEditar);
    panelBotones.add(btnEliminar);
    panelBotones.add(btnDetalles);

    // Panel derecho con filtros y botones
    JPanel panelDerecha = new JPanel(new BorderLayout());
    panelDerecha.add(panelFiltros, BorderLayout.NORTH);
    panelDerecha.add(panelBotones, BorderLayout.CENTER);

    // Tabla con scroll
    JScrollPane scrollPane = new JScrollPane(tablaEventos);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Eventos"));

    // Layout principal
    add(panelDerecha, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(lblEstatus, BorderLayout.SOUTH);

    // Márgenes
    ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
    btnTodos.addActionListener(e -> actualizarTabla("todos"));
    btnFuturos.addActionListener(e -> actualizarTabla("futuros"));
    btnPasados.addActionListener(e -> actualizarTabla("pasados"));

    // Botones principales
    btnNuevo.addActionListener(e -> nuevoEvento());
    btnEditar.addActionListener(e -> editarEvento());
    btnEliminar.addActionListener(e -> eliminarEvento());
    btnDetalles.addActionListener(e -> verDetalles());
  }

  private void actualizarTabla(String filtro) {
    tableModel.setRowCount(0); // Limpiar tabla

    List<Evento> eventos;
    switch (filtro) {
      case "futuros":
        eventos = eventoService.obtenerEventosFuturos();
        break;
      case "pasados":
        eventos = eventoService.obtenerEventosPasados();
        break;
      default:
        eventos = eventoService.obtenerTodosLosEventos();
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

    actualizarEstatus(eventos.size());
  }

  private void actualizarEstatus(int cantidadEventos) {
    lblEstatus.setText("Mostrando " + cantidadEventos + " evento(s) | " +
            "Total: " + eventoService.getTotalEventos() + " eventos, " +
            eventoService.getTotalAsistentes() + " asistentes");
  }

  private void nuevoEvento() {
    EventoDialog dialog = new EventoDialog(this, null);
    dialog.setVisible(true);

    if (dialog.isConfirmado()) {
      Evento nuevoEvento = dialog.getEvento();
      eventoService.crearEvento(nuevoEvento);
      actualizarTabla("todos");
      JOptionPane.showMessageDialog(this, "Evento creado exitosamente!",
              "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        actualizarTabla("todos");
        JOptionPane.showMessageDialog(this, "Evento actualizado exitosamente!",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  private void eliminarEvento() {
    int filaSeleccionada = tablaEventos.getSelectedRow();
    if (filaSeleccionada == -1) return;

    String nombreEvento = (String) tableModel.getValueAt(filaSeleccionada, 0);

    int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres eliminar el evento '" + nombreEvento + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (confirmacion == JOptionPane.YES_OPTION) {
      Evento evento = buscarEventoPorNombre(nombreEvento);
      if (evento != null) {
        eventoService.eliminarEvento(evento.getId());
        actualizarTabla("todos");
        JOptionPane.showMessageDialog(this, "Evento eliminado exitosamente!",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
      actualizarTabla("todos"); // Refrescar por si se agregaron/quitaron asistentes
    }
  }

  private Evento buscarEventoPorNombre(String nombre) {
    return eventoService.obtenerTodosLosEventos().stream()
            .filter(e -> e.getNombre().equals(nombre))
            .findFirst()
            .orElse(null);
  }

  private void crearDatosPrueba() {
    // Crear algunos eventos de ejemplo
    Evento evento1 = new Evento("Conferencia Tech 2025",
            LocalDateTime.now().plusDays(15),
            "Centro de Convenciones",
            "Conferencia anual de tecnología", 200);

    Evento evento2 = new Evento("Reunión de Equipo",
            LocalDateTime.now().plusDays(3),
            "Sala de Juntas",
            "Reunión mensual del equipo de desarrollo", 15);

    Evento evento3 = new Evento("Workshop React",
            LocalDateTime.now().minusDays(5),
            "Aula 301",
            "Taller práctico de React y Hooks", 30);

    eventoService.crearEvento(evento1);
    eventoService.crearEvento(evento2);
    eventoService.crearEvento(evento3);

    // Agregar algunos asistentes de ejemplo
    eventoService.registrarAsistente(evento1.getId(), new Asistente("Juan Pérez", "juan@email.com", "123456789"));
    eventoService.registrarAsistente(evento1.getId(), new Asistente("María García", "maria@email.com", "987654321"));
    eventoService.registrarAsistente(evento2.getId(), new Asistente("Carlos López", "carlos@email.com", "555666777"));
  }


}