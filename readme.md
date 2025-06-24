## Diagrama de Clases UML

```mermaid
classDiagram
    %% Clases del Modelo
    class Evento {
        -String id
        -String nombre
        -LocalDateTime fechaHora
        -String ubicacion
        -String descripcion
        -int capacidadMaxima
        -List~Asistente~ asistentes
        
        +getCantidadAsistentes() int
        +puedeAgregarAsistente() boolean
        +agregarAsistente(asistente) boolean
        +removerAsistente(asistenteId) boolean
    }

    class Asistente {
        #String id
        #String nombre
        #String email
        #String telefono
        
        +isValidEmail() boolean
        +isValidTelefono() boolean
        +isValid() boolean
    }

    class Conferencista {
        -String areaDeEspecialidad
        -String biografia
    }

    class Invitado {
        -List~String~ intereses
        -String comentarios
    }

    class Sponsor {
        -String empresa
        -double montoPatrocinio
        -String tipoPatrocinio
    }

    class Organizador {
        -String rol
        -List~String~ responsabilidades
        -String departamento
    }

    %% Clases de Lógica
    class EventoService {
        -List~Evento~ eventos
        -FileManager fileManager
        
        +crearEvento(evento)
        +obtenerTodosLosEventos() List~Evento~
        +registrarAsistente(eventoId, asistente) boolean
    }

    %% Relaciones
    Conferencista --|> Asistente
    Invitado --|> Asistente
    Sponsor --|> Asistente
    Organizador --|> Asistente
    
    Evento *-- Asistente : contains
    EventoService *-- Evento : manages