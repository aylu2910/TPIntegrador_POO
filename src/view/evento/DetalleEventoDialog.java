package view.evento;

import logic.EventoService;
import model.Asistente;
import model.Evento;
import utils.AsistenteValidator;
import utils.ValidationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetalleEventoDialog extends JDialog {
  private Evento evento;
  private EventoService eventoService;
  private AsistenteValidator asistenteValidator;

  private JLabel lblNombre, lblFecha, lblUbicacion, lblDescripcion, lblCapacidad;
  private JTable tablaAsistentes;
  private DefaultTableModel tableModel;
  private JTextField txtNombreAsistente, txtEmailAsistente, txtTelefonoAsistente;
  private JButton btnAgregar, btnQuitar, btnCerrar;

  public DetalleEventoDialog(Frame parent, Evento evento, EventoService eventoService) {
    super(parent, "Detalles del Evento", true);
    this.evento = evento;
    this.eventoService = eventoService;

    initializeComponents();
    setupLayout();
    setupEventListeners();
    cargarDatos();
    actualizarTablaAsistentes();

    pack();
    setLocationRelativeTo(parent);
  }

  private void initializeComponents() {
    // Labels para mostrar información del evento
    lblNombre = new JLabel();
    lblFecha = new JLabel();
    lblUbicacion = new JLabel();
    lblDescripcion = new JLabel();
    lblCapacidad = new JLabel();

    // Configurar labels
    Font fontBold = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    lblNombre.setFont(fontBold);

    // Tabla de asistentes
    String[] columnas = {"Nombre", "Email", "Teléfono"};
    tableModel = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaAsistentes = new JTable(tableModel);
    tablaAsistentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaAsistentes.setRowHeight(25);

    // Campos para nuevo asistente
    txtNombreAsistente = new JTextField(15);
    txtEmailAsistente = new JTextField(15);
    txtTelefonoAsistente = new JTextField(15);

    // Botones
    btnAgregar = new JButton("Agregar Asistente");
    btnQuitar = new JButton("Quitar Asistente");
    btnCerrar = new JButton("Cerrar");

    // Deshabilitar botón quitar inicialmente
    btnQuitar.setEnabled(false);
  }

  private void setupLayout() {
    setLayout(new BorderLayout(10, 10));

    // Panel superior con información del evento
    JPanel panelInfo = new JPanel(new GridBagLayout());
    panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Evento"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Nombre
    gbc.gridx = 0; gbc.gridy = 0;
    panelInfo.add(new JLabel("Nombre:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelInfo.add(lblNombre, gbc);

    // Fecha
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelInfo.add(new JLabel("Fecha/Hora:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelInfo.add(lblFecha, gbc);

    // Ubicación
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelInfo.add(new JLabel("Ubicación:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelInfo.add(lblUbicacion, gbc);

    // Capacidad
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelInfo.add(new JLabel("Capacidad:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelInfo.add(lblCapacidad, gbc);

    // Descripción
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelInfo.add(new JLabel("Descripción:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelInfo.add(lblDescripcion, gbc);

    // Panel central con tabla de asistentes
    JPanel panelAsistentes = new JPanel(new BorderLayout(5, 5));
    panelAsistentes.setBorder(BorderFactory.createTitledBorder("Asistentes Registrados"));

    JScrollPane scrollPane = new JScrollPane(tablaAsistentes);
    scrollPane.setPreferredSize(new Dimension(500, 200));
    panelAsistentes.add(scrollPane, BorderLayout.CENTER);

    // Panel para agregar asistentes
    JPanel panelAgregar = new JPanel(new GridBagLayout());
    panelAgregar.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Asistente"));

    gbc = new GridBagConstraints();
    gbc.insets = new Insets(3, 3, 3, 3);
    gbc.anchor = GridBagConstraints.WEST;

    // Campos para nuevo asistente
    gbc.gridx = 0; gbc.gridy = 0;
    panelAgregar.add(new JLabel("Nombre:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelAgregar.add(txtNombreAsistente, gbc);

    gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
    panelAgregar.add(new JLabel("Email:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelAgregar.add(txtEmailAsistente, gbc);

    gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
    panelAgregar.add(new JLabel("Teléfono:"), gbc);
    gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelAgregar.add(txtTelefonoAsistente, gbc);

    // Botones
    JPanel panelBotonesAsistente = new JPanel(new FlowLayout());
    panelBotonesAsistente.add(btnAgregar);
    panelBotonesAsistente.add(btnQuitar);

    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelAgregar.add(panelBotonesAsistente, gbc);

    // Panel inferior con botón cerrar
    JPanel panelBotones = new JPanel(new FlowLayout());
    panelBotones.add(btnCerrar);

    // Layout principal
    add(panelInfo, BorderLayout.NORTH);
    add(panelAsistentes, BorderLayout.CENTER);
    add(panelAgregar, BorderLayout.EAST);
    add(panelBotones, BorderLayout.SOUTH);

    // Márgenes
    ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  private void setupEventListeners() {
    // Selección de tabla
    tablaAsistentes.getSelectionModel().addListSelectionListener(e -> {
      btnQuitar.setEnabled(tablaAsistentes.getSelectedRow() != -1);
    });

    // Botones
    btnAgregar.addActionListener(e -> agregarAsistente());
    btnQuitar.addActionListener(e -> quitarAsistente());
    btnCerrar.addActionListener(e -> dispose());

    // Enter en campos ejecuta agregar
    ActionListener agregarAction = e -> agregarAsistente();
    txtNombreAsistente.addActionListener(agregarAction);
    txtEmailAsistente.addActionListener(agregarAction);
    txtTelefonoAsistente.addActionListener(agregarAction);

    // Escape cierra el diálogo
    KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
    getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
    getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
  }

  private void cargarDatos() {
    lblNombre.setText(evento.getNombre());
    lblFecha.setText(evento.getFechaFormateada());
    lblUbicacion.setText(evento.getUbicacion());
    lblDescripcion.setText(evento.getDescripcion().isEmpty() ? "Sin descripción" : evento.getDescripcion());
    lblCapacidad.setText(evento.getCantidadAsistentes() + " / " + evento.getCapacidadMaxima() + " asistentes");
  }

  private void actualizarTablaAsistentes() {
    tableModel.setRowCount(0);

    for (Asistente asistente : evento.getAsistentes()) {
      Object[] fila = {
              asistente.getNombre(),
              asistente.getEmail(),
              asistente.getTelefono()
      };
      tableModel.addRow(fila);
    }

    // Actualizar label de capacidad
    lblCapacidad.setText(evento.getCantidadAsistentes() + " / " + evento.getCapacidadMaxima() + " asistentes");

    // Habilitar/deshabilitar botón agregar según capacidad
    btnAgregar.setEnabled(evento.puedeAgregarAsistente());
  }

  private void agregarAsistente() {
    String nombre = txtNombreAsistente.getText().trim();
    String email = txtEmailAsistente.getText().trim();
    String telefono = txtTelefonoAsistente.getText().trim();

    ValidationResult validationResult = asistenteValidator.validar(nombre, email, telefono, evento);

    // Crear y agregar asistente
    Asistente nuevoAsistente = new Asistente(nombre, email, telefono);

    if (eventoService.registrarAsistente(evento.getId(), nuevoAsistente)) {
      // Limpiar campos
      txtNombreAsistente.setText("");
      txtEmailAsistente.setText("");
      txtTelefonoAsistente.setText("");

      // Actualizar tabla
      actualizarTablaAsistentes();

      JOptionPane.showMessageDialog(this, "Asistente registrado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
      txtNombreAsistente.requestFocus();
    } else {
      JOptionPane.showMessageDialog(this, "Error al registrar el asistente.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void quitarAsistente() {
    int filaSeleccionada = tablaAsistentes.getSelectedRow();
    if (filaSeleccionada == -1) return;

    String emailAsistente = (String) tableModel.getValueAt(filaSeleccionada, 1);
    String nombreAsistente = (String) tableModel.getValueAt(filaSeleccionada, 0);

    int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres quitar a " + nombreAsistente + " del evento?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

    if (confirmacion == JOptionPane.YES_OPTION) {
      // Buscar el asistente por email
      Asistente asistenteAQuitar = evento.getAsistentes().stream()
              .filter(a -> a.getEmail().equals(emailAsistente))
              .findFirst()
              .orElse(null);

      if (asistenteAQuitar != null) {
        if (eventoService.removerAsistente(evento.getId(), asistenteAQuitar.getId())) {
          actualizarTablaAsistentes();
          JOptionPane.showMessageDialog(this, "Asistente removido exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(this, "Error al remover el asistente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
}