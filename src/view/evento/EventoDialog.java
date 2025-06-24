package view.evento;

import model.Evento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventoDialog extends JDialog {
  private JTextField txtNombre, txtUbicacion;
  private JTextArea txtDescripcion;
  private JTextField txtFecha, txtHora;
  private JSpinner spnCapacidad;
  private JButton btnGuardar, btnCancelar;

  private Evento evento;
  private boolean confirmado = false;

  public EventoDialog(Frame parent, Evento evento) {
    super(parent, evento == null ? "Nuevo Evento" : "Editar Evento", true);
    this.evento = evento;

    initializeComponents();
    setupLayout();
    setupEventListeners();

    if (evento != null) {
      cargarDatos();
    } else {
      // Valores por defecto para nuevo evento
      LocalDateTime fechaDefault = LocalDateTime.now().plusDays(1);
      txtFecha.setText(fechaDefault.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
      txtHora.setText(fechaDefault.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    pack();
    setLocationRelativeTo(parent);
  }

  private void initializeComponents() {
    txtNombre = new JTextField(20);
    txtUbicacion = new JTextField(20);
    txtDescripcion = new JTextArea(4, 20);
    txtDescripcion.setLineWrap(true);
    txtDescripcion.setWrapStyleWord(true);

    txtFecha = new JTextField(10);
    txtHora = new JTextField(8);

    spnCapacidad = new JSpinner(new SpinnerNumberModel(50, 1, 10000, 1));

    btnGuardar = new JButton("Guardar");
    btnCancelar = new JButton("Cancelar");

    // Tooltips para ayudar al usuario
    txtFecha.setToolTipText("Formato: DD/MM/YYYY");
    txtHora.setToolTipText("Formato: HH:MM");
  }

  private void setupLayout() {
    setLayout(new BorderLayout(10, 10));

    // Panel principal con campos
    JPanel panelCampos = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Nombre
    gbc.gridx = 0; gbc.gridy = 0;
    panelCampos.add(new JLabel("Nombre:*"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelCampos.add(txtNombre, gbc);

    // Fecha y Hora
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelCampos.add(new JLabel("Fecha:*"), gbc);
    gbc.gridx = 1;
    panelCampos.add(txtFecha, gbc);
    gbc.gridx = 2;
    panelCampos.add(new JLabel("Hora:*"), gbc);
    gbc.gridx = 3;
    panelCampos.add(txtHora, gbc);

    // Ubicación
    gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
    panelCampos.add(new JLabel("Ubicación:*"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
    panelCampos.add(txtUbicacion, gbc);

    // Capacidad
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
    panelCampos.add(new JLabel("Capacidad:*"), gbc);
    gbc.gridx = 1;
    panelCampos.add(spnCapacidad, gbc);

    // Descripción
    gbc.gridx = 0; gbc.gridy = 4;
    panelCampos.add(new JLabel("Descripción:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
    panelCampos.add(new JScrollPane(txtDescripcion), gbc);

    // Panel de botones
    JPanel panelBotones = new JPanel(new FlowLayout());
    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    // Nota sobre campos requeridos
    JLabel lblNota = new JLabel("* Campos requeridos");
    lblNota.setFont(lblNota.getFont().deriveFont(Font.ITALIC));
    lblNota.setForeground(Color.GRAY);

    // Layout principal
    add(panelCampos, BorderLayout.CENTER);
    add(panelBotones, BorderLayout.SOUTH);
    add(lblNota, BorderLayout.NORTH);

    // Márgenes
    ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  private void setupEventListeners() {
    btnGuardar.addActionListener(e -> guardar());
    btnCancelar.addActionListener(e -> dispose());

    // Enter en campos de texto ejecuta guardar
    ActionListener guardarAction = e -> guardar();
    txtNombre.addActionListener(guardarAction);
    txtFecha.addActionListener(guardarAction);
    txtHora.addActionListener(guardarAction);
    txtUbicacion.addActionListener(guardarAction);

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
    txtNombre.setText(evento.getNombre());
    txtUbicacion.setText(evento.getUbicacion());
    txtDescripcion.setText(evento.getDescripcion());
    spnCapacidad.setValue(evento.getCapacidadMaxima());

    LocalDateTime fechaHora = evento.getFechaHora();
    txtFecha.setText(fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    txtHora.setText(fechaHora.format(DateTimeFormatter.ofPattern("HH:mm")));
  }

  private void guardar() {
    if (validarCampos()) {
      try {
        String nombre = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        int capacidad = (Integer) spnCapacidad.getValue();

        // Parsear fecha y hora
        LocalDateTime fechaHora = parsearFechaHora();

        if (evento == null) {
          // Crear nuevo evento
          evento = new Evento(nombre, fechaHora, ubicacion, descripcion, capacidad);
        } else {
          // Actualizar evento existente
          evento.setNombre(nombre);
          evento.setFechaHora(fechaHora);
          evento.setUbicacion(ubicacion);
          evento.setDescripcion(descripcion);
          evento.setCapacidadMaxima(capacidad);
        }

        confirmado = true;
        dispose();

      } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this,
                "Formato de fecha u hora inválido.\nUse DD/MM/YYYY para fecha y HH:MM para hora.",
                "Error de Formato",
                JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error al guardar el evento: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private boolean validarCampos() {
    // Validar campos requeridos
    if (txtNombre.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "El nombre es requerido.", "Error", JOptionPane.ERROR_MESSAGE);
      txtNombre.requestFocus();
      return false;
    }

    if (txtFecha.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "La fecha es requerida.", "Error", JOptionPane.ERROR_MESSAGE);
      txtFecha.requestFocus();
      return false;
    }

    if (txtHora.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "La hora es requerida.", "Error", JOptionPane.ERROR_MESSAGE);
      txtHora.requestFocus();
      return false;
    }

    if (txtUbicacion.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this, "La ubicación es requerida.", "Error", JOptionPane.ERROR_MESSAGE);
      txtUbicacion.requestFocus();
      return false;
    }

    return true;
  }

  private LocalDateTime parsearFechaHora() throws DateTimeParseException {
    String fechaStr = txtFecha.getText().trim();
    String horaStr = txtHora.getText().trim();

    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    // Parsear fecha
    String[] partesFecha = fechaStr.split("/");
    if (partesFecha.length != 3) {
      throw new DateTimeParseException("Formato de fecha inválido", fechaStr, 0);
    }

    int dia = Integer.parseInt(partesFecha[0]);
    int mes = Integer.parseInt(partesFecha[1]);
    int año = Integer.parseInt(partesFecha[2]);

    // Parsear hora
    String[] partesHora = horaStr.split(":");
    if (partesHora.length != 2) {
      throw new DateTimeParseException("Formato de hora inválido", horaStr, 0);
    }

    int hora = Integer.parseInt(partesHora[0]);
    int minuto = Integer.parseInt(partesHora[1]);

    return LocalDateTime.of(año, mes, dia, hora, minuto);
  }

  public boolean isConfirmado() {
    return confirmado;
  }

  public Evento getEvento() {
    return evento;
  }
}