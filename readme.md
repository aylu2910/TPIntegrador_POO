## Diagrama de Clases UML

```mermaid
classDiagram
    class Evento {
        -String id
        -String nombre
        -LocalDateTime fechaHora
        -String ubicacion
        -String descripcion
        -int capacidadMaxima
        -List~Asistente~ asistentes
        
        +Evento(nombre, fechaHora, ubicacion, descripcion, capacidadMaxima)
        +getCantidadAsistentes() int
        +puedeAgregarAsistente() boolean
        +agregarAsistente(asistente) boolean
        +removerAsistente(asistenteId) boolean
        +getFechaFormateada() String
        +esFuturo() boolean
    }
    
    class Asistente {
        #String id
        #String nombre
        #String email
        #String telefono
        
        +Asistente(nombre, email, telefono)
        +isValidEmail() boolean
        +isValidTelefono() boolean
        +isValid() boolean
    }
    
    class Conferencista {
        -String areaDeEspecialidad
        -String biografia
        
        +Conferencista(nombre, email, telefono, areaDeEspecialidad, biografia)
    }
    
    class Invitado {
        -List~String~ intereses
        -String comentarios
        
        +Invitado(nombre, email, telefono, intereses, comentarios)
    }
    
    class Sponsor {
        -String empresa
        -double montoPatrocinio
        -String tipoPatrocinio
        
        +Sponsor(nombre, email, telefono, empresa, montoPatrocinio, tipoPatrocinio)
        +getMontoPatrocinio() double
        +getTipoPatrocinio() String
    }
    
    class Organizador {
        -String rol
        -List~String~ responsabilidades
        -String departamento
        
        +Organizador(nombre, email, telefono, rol, responsabilidades, departamento)
        +agregarResponsabilidad(responsabilidad)
        +removerResponsabilidad(responsabilidad)
    }
    
    %% ===============================================
    %% PACKAGE LOGIC - Lógica de Negocio
    %% ===============================================
    
    class EventoService {
        -List~Evento~ eventos
        -FileManager fileManager
        
        +EventoService()
        -cargarDatos()
        +guardarDatos()
        +crearEvento(evento)
        +actualizarEvento(eventoActualizado)
        +eliminarEvento(eventoId)
        +obtenerEventoPorId(id) Evento
        +obtenerTodosLosEventos() List~Evento~
        +obtenerEventosFuturos() List~Evento~
        +obtenerEventosPasados() List~Evento~
        +registrarAsistente(eventoId, asistente) boolean
        +removerAsistente(eventoId, asistenteId) boolean
        +existeEvento(nombre, fechaHora) boolean
        +getTotalEventos() int
        +getTotalAsistentes() int
        +buscarEventos(filtro) List~Evento~
    }
    class FileManager {
        -String EVENTOS_FILE$
        -String ASISTENTES_FILE$
        -String DELIMITER$
        -DateTimeFormatter DATE_FORMATTER$
        
        +guardarEventos(eventos)
        +cargarEventos() List~Evento~
        +guardarAsistentes(asistentesPorEvento)
        +cargarAsistentes() Map~String, List~Asistente~~
        +archivosExisten() boolean
    }
    
    class AsistenteValidator {
        -Pattern EMAIL_PATTERN$
        -Pattern TELEFONO_PATTERN$
        -int MIN_LONGITUD_NOMBRE$
        -int MAX_LONGITUD_NOMBRE$
        -int MIN_LONGITUD_TELEFONO$
        -int MAX_LONGITUD_TELEFONO$
        
        +validar(nombre, email, telefono, evento) ValidationResult
        +validarNombre(nombre) ValidationResult
        +validarEmail(email) ValidationResult
        +validarTelefono(telefono) ValidationResult
        +validarCapacidadEvento(evento) ValidationResult
        +validarEmailDuplicado(email, evento) ValidationResult
        +validarAsistenteCompleto(asistente) ValidationResult
    }
    
    class ValidationResult {
        -List~ValidationErrorDetail~ errors
        
        +ValidationResult()
        +addError(type, message)
        +addErrors(newErrors)
        +isValid() boolean
        +getErrors() List~ValidationErrorDetail~
        +getFirstErrorMessage() String
        +getAllErrorMessages() String
        +hasErrorType(type) boolean
    }
    
    class ValidationErrorDetail {
        -ValidationError type
        -String message
        
        +ValidationErrorDetail(type, message)
        +getType() ValidationError
        +getMessage() String
    }
    
    class ValidationError {
        <<enumeration>>
        NOMBRE_REQUERIDO
        NOMBRE_MUY_CORTO
        NOMBRE_MUY_LARGO
        NOMBRE_FORMATO_INVALIDO
        EMAIL_REQUERIDO
        EMAIL_FORMATO_INVALIDO
        EMAIL_DUPLICADO
        TELEFONO_REQUERIDO
        TELEFONO_MUY_CORTO
        TELEFONO_MUY_LARGO
        TELEFONO_FORMATO_INVALIDO
        CAPACIDAD_COMPLETA
    }

    class MainFrame {
        -EventoService eventoService
        -JTable tablaEventos
        -DefaultTableModel tableModel
        -JButton btnNuevo, btnEditar, btnEliminar, btnDetalles
        -JButton btnFuturos, btnPasados, btnTodos
        -JLabel lblEstatus
        -CalendarioPanel calendarioPanel
        -JTabbedPane tabbedPane
        
        +MainFrame()
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -actualizarTabla(filtro)
        -actualizarEstatus(cantidadMostrada, filtro)
        -nuevoEvento()
        -editarEvento()
        -eliminarEvento()
        -verDetalles()
        +actualizarVistas()
        -buscarEventoPorNombre(nombre) Evento
        -crearDatosPrueba()
    }
    
    class EventoDialog {
        -JTextField txtNombre, txtUbicacion
        -JTextArea txtDescripcion
        -JTextField txtFecha, txtHora
        -JSpinner spnCapacidad
        -JButton btnGuardar, btnCancelar
        -Evento evento
        -boolean confirmado
        
        +EventoDialog(parent, evento)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cargarDatos()
        -guardar()
        -validarCampos() boolean
        -parsearFechaHora() LocalDateTime
        +isConfirmado() boolean
        +getEvento() Evento
    }
    
    class DetalleEventoDialog {
        -Evento evento
        -EventoService eventoService
        -AsistenteValidator asistenteValidator
        -JLabel lblNombre, lblFecha, lblUbicacion, lblDescripcion, lblCapacidad
        -JTable tablaAsistentes
        -DefaultTableModel tableModel
        -JTextField txtNombreAsistente, txtEmailAsistente, txtTelefonoAsistente
        -JButton btnAgregar, btnQuitar, btnCerrar
        
        +DetalleEventoDialog(parent, evento, eventoService)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cargarDatos()
        -actualizarTablaAsistentes()
        -agregarAsistente()
        -quitarAsistente()
    }
    
    class CalendarioPanel {
        -CalendarioTableModel calendarioModel
        -JTable tablaCalendario
        -JLabel lblTituloMes
        -JButton btnAnterior, btnSiguiente, btnHoy
        -EventoService eventoService
        -MainFrame parentFrame
        
        +CalendarioPanel(eventoService, parentFrame)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cambiarMes(direccion)
        -irAHoy()
        -actualizarTitulo()
        +refrescarCalendario()
        -abrirDialogoNuevoEvento(fecha)
        -mostrarEventosDelDia(dia)
    }
    
    class CalendarioTableModel {
        -String[] DIAS_SEMANA$
        -int FILAS$
        -int COLUMNAS$
        -YearMonth mesActual
        -LocalDate primerDiaGrid
        -EventoService eventoService
        -List~Evento~ eventosDelMes
        
        +CalendarioTableModel(eventoService)
        +getRowCount() int
        +getColumnCount() int
        +getColumnName(column) String
        +getValueAt(rowIndex, columnIndex) Object
        +isCellEditable(rowIndex, columnIndex) boolean
        +setMes(nuevoMes)
        +getMesActual() YearMonth
        +getTituloMes() String
        +getFechaEnPosicion(row, column) LocalDate
        -calcularPrimerDia()
        -cargarEventosDelMes()
    }
    
    class DiaCalendario {
        -LocalDate fecha
        -boolean delMesActual
        -List~Evento~ eventos
        
        +getFecha() LocalDate
        +setFecha(fecha)
        +isDelMesActual() boolean
        +setDelMesActual(delMesActual)
        +getEventos() List~Evento~
        +setEventos(eventos)
        +getCantidadEventos() int
        +tieneEventos() boolean
        +toString() String
    }
    
    class CalendarioCellRenderer {
        -Color COLOR_HOY$
        -Color COLOR_CON_EVENTOS$
        -Color COLOR_OTRO_MES$
        -Color COLOR_WEEKEND$
        -Color COLOR_NORMAL$
        
        +getTableCellRendererComponent(...) Component
        -createCellText(dia) String
        -configurarColores(dia, isSelected, column, table)
        -configurarBorde(dia)
        -configurarFont(dia)
        -createTooltip(dia) String
    }
    
    class Main {
        +main(args) void$
    }
    
    %% ===============================================
    %% RELACIONES
    %% ===============================================
    
    %% Herencia
    Conferencista --|> Asistente : extends
    Invitado --|> Asistente : extends
    Sponsor --|> Asistente : extends
    Organizador --|> Asistente : extends
    
    %% Composición y Agregación
    Evento *-- Asistente : contains 1..*
    EventoService *-- Evento : manages 0..*
    EventoService *-- FileManager : uses
    MainFrame *-- EventoService : uses
    MainFrame *-- CalendarioPanel : contains
    CalendarioPanel *-- CalendarioTableModel : uses
    CalendarioTableModel +-- DiaCalendario : inner class
    DetalleEventoDialog *-- AsistenteValidator : uses
    
    %% Dependencias
    EventoService ..> Asistente : creates
    AsistenteValidator ..> ValidationResult : returns
    ValidationResult *-- ValidationErrorDetail : contains
    ValidationErrorDetail *-- ValidationError : uses
    CalendarioTableModel ..> DiaCalendario : creates
    
    %% Uso/Asociación
    Main --> MainFrame : creates
    MainFrame --> EventoDialog : creates
    MainFrame --> DetalleEventoDialog : creates
    CalendarioPanel --> EventoDialog : creates
    CalendarioTableModel --> EventoService : queries
    CalendarioCellRenderer ..> DiaCalendario : renders
    
    %% Packages
    classDef modelClass fill:#e1f5fe
    classDef logicClass fill:#f3e5f5
    classDef utilsClass fill:#fff3e0
    classDef viewClass fill:#e8f5e8
    classDef mainClass fill:#ffebee
    
    class Evento,Asistente,Conferencista,Invitado,Sponsor,Organizador modelClass
    class EventoService logicClass
    class FileManager,AsistenteValidator,ValidationResult,ValidationErrorDetail,ValidationError utilsClass
    class MainFrame,EventoDialog,DetalleEventoDialog,CalendarioPanel,CalendarioTableModel,DiaCalendario,CalendarioCellRenderer viewClass
    class Main mainClass
# 📊 Diagrama UML - Sistema de Gestión de Eventos

```mermaid
classDiagram
    %% ===============================================
    %% PACKAGE MODEL - Clases de Dominio
    %% ===============================================
    
    class Evento {
        -String id
        -String nombre
        -LocalDateTime fechaHora
        -String ubicacion
        -String descripcion
        -int capacidadMaxima
        -List~Asistente~ asistentes
        
        +Evento(nombre, fechaHora, ubicacion, descripcion, capacidadMaxima)
        +getCantidadAsistentes() int
        +puedeAgregarAsistente() boolean
        +agregarAsistente(asistente) boolean
        +removerAsistente(asistenteId) boolean
        +getFechaFormateada() String
        +esFuturo() boolean
    }
    
    class Asistente {
        #String id
        #String nombre
        #String email
        #String telefono
        
        +Asistente(nombre, email, telefono)
        +isValidEmail() boolean
        +isValidTelefono() boolean
        +isValid() boolean
    }
    
    class Conferencista {
        -String areaDeEspecialidad
        -String biografia
        
        +Conferencista(nombre, email, telefono, areaDeEspecialidad, biografia)
    }
    
    class Invitado {
        -List~String~ intereses
        -String comentarios
        
        +Invitado(nombre, email, telefono, intereses, comentarios)
    }
    
    class Sponsor {
        -String empresa
        -double montoPatrocinio
        -String tipoPatrocinio
        
        +Sponsor(nombre, email, telefono, empresa, montoPatrocinio, tipoPatrocinio)
        +getMontoPatrocinio() double
        +getTipoPatrocinio() String
    }
    
    class Organizador {
        -String rol
        -List~String~ responsabilidades
        -String departamento
        
        +Organizador(nombre, email, telefono, rol, responsabilidades, departamento)
        +agregarResponsabilidad(responsabilidad)
        +removerResponsabilidad(responsabilidad)
    }
    
    %% ===============================================
    %% PACKAGE LOGIC - Lógica de Negocio
    %% ===============================================
    
    class EventoService {
        -List~Evento~ eventos
        -FileManager fileManager
        
        +EventoService()
        -cargarDatos()
        +guardarDatos()
        +crearEvento(evento)
        +actualizarEvento(eventoActualizado)
        +eliminarEvento(eventoId)
        +obtenerEventoPorId(id) Evento
        +obtenerTodosLosEventos() List~Evento~
        +obtenerEventosFuturos() List~Evento~
        +obtenerEventosPasados() List~Evento~
        +registrarAsistente(eventoId, asistente) boolean
        +removerAsistente(eventoId, asistenteId) boolean
        +existeEvento(nombre, fechaHora) boolean
        +getTotalEventos() int
        +getTotalAsistentes() int
        +buscarEventos(filtro) List~Evento~
    }
    
    %% ===============================================
    %% PACKAGE UTILS - Utilities y Validaciones
    %% ===============================================
    
    class FileManager {
        -String EVENTOS_FILE$
        -String ASISTENTES_FILE$
        -String DELIMITER$
        -DateTimeFormatter DATE_FORMATTER$
        
        +guardarEventos(eventos)
        +cargarEventos() List~Evento~
        +guardarAsistentes(asistentesPorEvento)
        +cargarAsistentes() Map~String, List~Asistente~~
        +archivosExisten() boolean
    }
    
    class AsistenteValidator {
        -Pattern EMAIL_PATTERN$
        -Pattern TELEFONO_PATTERN$
        -int MIN_LONGITUD_NOMBRE$
        -int MAX_LONGITUD_NOMBRE$
        -int MIN_LONGITUD_TELEFONO$
        -int MAX_LONGITUD_TELEFONO$
        
        +validar(nombre, email, telefono, evento) ValidationResult
        +validarNombre(nombre) ValidationResult
        +validarEmail(email) ValidationResult
        +validarTelefono(telefono) ValidationResult
        +validarCapacidadEvento(evento) ValidationResult
        +validarEmailDuplicado(email, evento) ValidationResult
        +validarAsistenteCompleto(asistente) ValidationResult
    }
    
    class ValidationResult {
        -List~ValidationErrorDetail~ errors
        
        +ValidationResult()
        +addError(type, message)
        +addErrors(newErrors)
        +isValid() boolean
        +getErrors() List~ValidationErrorDetail~
        +getFirstErrorMessage() String
        +getAllErrorMessages() String
        +hasErrorType(type) boolean
    }
    
    class ValidationErrorDetail {
        -ValidationError type
        -String message
        
        +ValidationErrorDetail(type, message)
        +getType() ValidationError
        +getMessage() String
    }
    
    class ValidationError {
        <<enumeration>>
        NOMBRE_REQUERIDO
        NOMBRE_MUY_CORTO
        NOMBRE_MUY_LARGO
        NOMBRE_FORMATO_INVALIDO
        EMAIL_REQUERIDO
        EMAIL_FORMATO_INVALIDO
        EMAIL_DUPLICADO
        TELEFONO_REQUERIDO
        TELEFONO_MUY_CORTO
        TELEFONO_MUY_LARGO
        TELEFONO_FORMATO_INVALIDO
        CAPACIDAD_COMPLETA
    }
    
    %% ===============================================
    %% PACKAGE VIEW - Vista Principal
    %% ===============================================
    
    class MainFrame {
        -EventoService eventoService
        -JTable tablaEventos
        -DefaultTableModel tableModel
        -JButton btnNuevo, btnEditar, btnEliminar, btnDetalles
        -JButton btnFuturos, btnPasados, btnTodos
        -JLabel lblEstatus
        -CalendarioPanel calendarioPanel
        -JTabbedPane tabbedPane
        
        +MainFrame()
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -actualizarTabla(filtro)
        -actualizarEstatus(cantidadMostrada, filtro)
        -nuevoEvento()
        -editarEvento()
        -eliminarEvento()
        -verDetalles()
        +actualizarVistas()
        -buscarEventoPorNombre(nombre) Evento
        -crearDatosPrueba()
    }
    
    %% ===============================================
    %% PACKAGE VIEW.EVENTO - Diálogos de Eventos
    %% ===============================================
    
    class EventoDialog {
        -JTextField txtNombre, txtUbicacion
        -JTextArea txtDescripcion
        -JTextField txtFecha, txtHora
        -JSpinner spnCapacidad
        -JButton btnGuardar, btnCancelar
        -Evento evento
        -boolean confirmado
        
        +EventoDialog(parent, evento)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cargarDatos()
        -guardar()
        -validarCampos() boolean
        -parsearFechaHora() LocalDateTime
        +isConfirmado() boolean
        +getEvento() Evento
    }
    
    class DetalleEventoDialog {
        -Evento evento
        -EventoService eventoService
        -AsistenteValidator asistenteValidator
        -JLabel lblNombre, lblFecha, lblUbicacion, lblDescripcion, lblCapacidad
        -JTable tablaAsistentes
        -DefaultTableModel tableModel
        -JTextField txtNombreAsistente, txtEmailAsistente, txtTelefonoAsistente
        -JButton btnAgregar, btnQuitar, btnCerrar
        
        +DetalleEventoDialog(parent, evento, eventoService)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cargarDatos()
        -actualizarTablaAsistentes()
        -agregarAsistente()
        -quitarAsistente()
    }
    
    %% ===============================================
    %% PACKAGE VIEW.CALENDARIO - Vista de Calendario
    %% ===============================================
    
    class CalendarioPanel {
        -CalendarioTableModel calendarioModel
        -JTable tablaCalendario
        -JLabel lblTituloMes
        -JButton btnAnterior, btnSiguiente, btnHoy
        -EventoService eventoService
        -MainFrame parentFrame
        
        +CalendarioPanel(eventoService, parentFrame)
        -initializeComponents()
        -setupLayout()
        -setupEventListeners()
        -cambiarMes(direccion)
        -irAHoy()
        -actualizarTitulo()
        +refrescarCalendario()
        -abrirDialogoNuevoEvento(fecha)
        -mostrarEventosDelDia(dia)
    }
    
    class CalendarioTableModel {
        -String[] DIAS_SEMANA$
        -int FILAS$
        -int COLUMNAS$
        -YearMonth mesActual
        -LocalDate primerDiaGrid
        -EventoService eventoService
        -List~Evento~ eventosDelMes
        
        +CalendarioTableModel(eventoService)
        +getRowCount() int
        +getColumnCount() int
        +getColumnName(column) String
        +getValueAt(rowIndex, columnIndex) Object
        +isCellEditable(rowIndex, columnIndex) boolean
        +setMes(nuevoMes)
        +getMesActual() YearMonth
        +getTituloMes() String
        +getFechaEnPosicion(row, column) LocalDate
        -calcularPrimerDia()
        -cargarEventosDelMes()
    }
    
    class DiaCalendario {
        -LocalDate fecha
        -boolean delMesActual
        -List~Evento~ eventos
        
        +getFecha() LocalDate
        +setFecha(fecha)
        +isDelMesActual() boolean
        +setDelMesActual(delMesActual)
        +getEventos() List~Evento~
        +setEventos(eventos)
        +getCantidadEventos() int
        +tieneEventos() boolean
        +toString() String
    }
    
    class CalendarioCellRenderer {
        -Color COLOR_HOY$
        -Color COLOR_CON_EVENTOS$
        -Color COLOR_OTRO_MES$
        -Color COLOR_WEEKEND$
        -Color COLOR_NORMAL$
        
        +getTableCellRendererComponent(...) Component
        -createCellText(dia) String
        -configurarColores(dia, isSelected, column, table)
        -configurarBorde(dia)
        -configurarFont(dia)
        -createTooltip(dia) String
    }
    
    %% ===============================================
    %% MAIN CLASS
    %% ===============================================
    
    class Main {
        +main(args) void$
    }
    
    %% ===============================================
    %% RELACIONES
    %% ===============================================
    
    %% Herencia
    Conferencista --|> Asistente : extends
    Invitado --|> Asistente : extends
    Sponsor --|> Asistente : extends
    Organizador --|> Asistente : extends
    
    %% Composición y Agregación
    Evento *-- Asistente : contains 1..*
    EventoService *-- Evento : manages 0..*
    EventoService *-- FileManager : uses
    MainFrame *-- EventoService : uses
    MainFrame *-- CalendarioPanel : contains
    CalendarioPanel *-- CalendarioTableModel : uses
    CalendarioTableModel +-- DiaCalendario : inner class
    DetalleEventoDialog *-- AsistenteValidator : uses
    
    %% Dependencias
    EventoService ..> Asistente : creates
    AsistenteValidator ..> ValidationResult : returns
    ValidationResult *-- ValidationErrorDetail : contains
    ValidationErrorDetail *-- ValidationError : uses
    CalendarioTableModel ..> DiaCalendario : creates
    
    %% Uso/Asociación
    Main --> MainFrame : creates
    MainFrame --> EventoDialog : creates
    MainFrame --> DetalleEventoDialog : creates
    CalendarioPanel --> EventoDialog : creates
    CalendarioTableModel --> EventoService : queries
    CalendarioCellRenderer ..> DiaCalendario : renders
    
    %% Packages
    classDef modelClass fill:#e1f5fe
    classDef logicClass fill:#f3e5f5
    classDef utilsClass fill:#fff3e0
    classDef viewClass fill:#e8f5e8
    classDef mainClass fill:#ffebee
    
    class Evento,Asistente,Conferencista,Invitado,Sponsor,Organizador modelClass
    class EventoService logicClass
    class FileManager,AsistenteValidator,ValidationResult,ValidationErrorDetail,ValidationError utilsClass
    class MainFrame,EventoDialog,DetalleEventoDialog,CalendarioPanel,CalendarioTableModel,DiaCalendario,CalendarioCellRenderer viewClass
    class Main mainClass
```

## 📋 Descripción de Packages y Responsabilidades

### 🎯 **Package MODEL** (Azul claro)
- **Evento**: Entidad principal del dominio
- **Asistente**: Clase base para participantes (con herencia múltiple)
- **Conferencista**: Especialización de Asistente con área de especialidad
- **Invitado**: Especialización de Asistente con intereses
- **Sponsor**: Especialización de Asistente con información de patrocinio
- **Organizador**: Especialización de Asistente con roles y responsabilidades

### **Package LOGIC**
- **EventoService**: Servicio principal que maneja toda la lógica de negocio

### **Package UTILS**
- **FileManager**: Persistencia en archivos de texto
- **AsistenteValidator**: Validaciones centralizadas
- **ValidationResult**: Encapsula resultados de validación
- **ValidationError**: Enum de tipos de errores

### **Package VIEW**
- **MainFrame**: Ventana principal con tabs
- **EventoDialog**: Crear/editar eventos
- **DetalleEventoDialog**: Gestión de asistentes
- **CalendarioPanel**: Vista de calendario
- **CalendarioTableModel**: Modelo de datos del calendario
- **CalendarioCellRenderer**: Renderizado personalizado

### **MAIN** 
- **Main**: Punto de entrada de la aplicación


##  Descripción de Packages y Responsabilidades

###  **Package MODEL**
- **Evento**: Entidad principal del dominio
- **Asistente**: Clase base para participantes (con herencia múltiple)
- **Conferencista**: Especialización de Asistente con área de especialidad
- **Invitado**: Especialización de Asistente con intereses
- **Sponsor**: Especialización de Asistente con información de patrocinio
- **Organizador**: Especialización de Asistente con roles y responsabilidades

### ️ **Package LOGIC**
- **EventoService**: Servicio principal que maneja toda la lógica de negocio

### ️ **Package UTILS**
- **FileManager**: Persistencia en archivos de texto
- **AsistenteValidator**: Validaciones centralizadas
- **ValidationResult**: Encapsula resultados de validación
- **ValidationError**: Enum de tipos de errores

### ️ **Package VIEW**
- **MainFrame**: Ventana principal con tabs
- **EventoDialog**: Crear/editar eventos
- **DetalleEventoDialog**: Gestión de asistentes
- **CalendarioPanel**: Vista de calendario
- **CalendarioTableModel**: Modelo de datos del calendario
- **CalendarioCellRenderer**: Renderizado personalizado

###  **MAIN** (Rosa claro)
- **Main**: Punto de entrada de la aplicación
