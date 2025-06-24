package utils;

import model.Asistente;
import model.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AsistenteValidator {

  // Patrones de validación
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
          "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
  );

  private static final Pattern TELEFONO_PATTERN = Pattern.compile(
          "^[0-9+\\-\\s()]{7,20}$"
  );

  // Constantes de validación
  private static final int MIN_LONGITUD_NOMBRE = 2;
  private static final int MAX_LONGITUD_NOMBRE = 100;
  private static final int MIN_LONGITUD_TELEFONO = 7;
  private static final int MAX_LONGITUD_TELEFONO = 20;

  /**
   * Valida todos los datos de un asistente antes de agregarlo a un evento
   *
   * @param nombre Nombre del asistente
   * @param email Email del asistente
   * @param telefono Teléfono del asistente
   * @param evento Evento al que se quiere agregar el asistente
   * @return Resultado de la validación con errores si los hay
   */
  public ValidationResult validar(String nombre, String email, String telefono, Evento evento) {
    ValidationResult result = new ValidationResult();

    // Validar nombre
    ValidationResult nombreResult = validarNombre(nombre);
    if (!nombreResult.isValid()) {
      result.addErrors(nombreResult.getErrors());
    }

    // Validar email
    ValidationResult emailResult = validarEmail(email);
    if (!emailResult.isValid()) {
      result.addErrors(emailResult.getErrors());
    }

    // Validar teléfono
    ValidationResult telefonoResult = validarTelefono(telefono);
    if (!telefonoResult.isValid()) {
      result.addErrors(telefonoResult.getErrors());
    }

    // Validar si los datos básicos son válidos
    if (result.isValid()) {
      // Verificar capacidad del evento
      ValidationResult capacidadResult = validarCapacidadEvento(evento);
      if (!capacidadResult.isValid()) {
        result.addErrors(capacidadResult.getErrors());
      }

      // Verificar duplicados por email
      ValidationResult duplicadoResult = validarEmailDuplicado(email, evento);
      if (!duplicadoResult.isValid()) {
        result.addErrors(duplicadoResult.getErrors());
      }
    }

    return result;
  }

  /**
   * Valida el nombre del asistente
   */
  public ValidationResult validarNombre(String nombre) {
    ValidationResult result = new ValidationResult();

    if (nombre == null || nombre.trim().isEmpty()) {
      result.addError(ValidationError.NOMBRE_REQUERIDO, "El nombre es requerido.");
      return result;
    }

    String nombreTrim = nombre.trim();

    if (nombreTrim.length() < MIN_LONGITUD_NOMBRE) {
      result.addError(ValidationError.NOMBRE_MUY_CORTO,
              "El nombre debe tener al menos " + MIN_LONGITUD_NOMBRE + " caracteres.");
    }

    if (nombreTrim.length() > MAX_LONGITUD_NOMBRE) {
      result.addError(ValidationError.NOMBRE_MUY_LARGO,
              "El nombre no puede exceder " + MAX_LONGITUD_NOMBRE + " caracteres.");
    }

    // Verificar que solo contenga letras, espacios y caracteres válidos
    if (!nombreTrim.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
      result.addError(ValidationError.NOMBRE_FORMATO_INVALIDO,
              "El nombre solo puede contener letras y espacios.");
    }

    return result;
  }

  /**
   * Valida el email del asistente
   */
  public ValidationResult validarEmail(String email) {
    ValidationResult result = new ValidationResult();

    if (email == null || email.trim().isEmpty()) {
      result.addError(ValidationError.EMAIL_REQUERIDO, "El email es requerido.");
      return result;
    }

    String emailTrim = email.trim().toLowerCase();

    if (!EMAIL_PATTERN.matcher(emailTrim).matches()) {
      result.addError(ValidationError.EMAIL_FORMATO_INVALIDO,
              "Ingrese un email válido (ejemplo: usuario@dominio.com).");
    }

    return result;
  }

  /**
   * Valida el teléfono del asistente
   */
  public ValidationResult validarTelefono(String telefono) {
    ValidationResult result = new ValidationResult();

    if (telefono == null || telefono.trim().isEmpty()) {
      result.addError(ValidationError.TELEFONO_REQUERIDO, "El teléfono es requerido.");
      return result;
    }

    String telefonoTrim = telefono.trim();

    if (telefonoTrim.length() < MIN_LONGITUD_TELEFONO) {
      result.addError(ValidationError.TELEFONO_MUY_CORTO,
              "El teléfono debe tener al menos " + MIN_LONGITUD_TELEFONO + " dígitos.");
    }

    if (telefonoTrim.length() > MAX_LONGITUD_TELEFONO) {
      result.addError(ValidationError.TELEFONO_MUY_LARGO,
              "El teléfono no puede exceder " + MAX_LONGITUD_TELEFONO + " caracteres.");
    }

    if (!TELEFONO_PATTERN.matcher(telefonoTrim).matches()) {
      result.addError(ValidationError.TELEFONO_FORMATO_INVALIDO,
              "El teléfono solo puede contener números, espacios, guiones, paréntesis y el signo +.");
    }

    return result;
  }

  /**
   * Verifica si el evento tiene capacidad para más asistentes
   */
  public ValidationResult validarCapacidadEvento(Evento evento) {
    ValidationResult result = new ValidationResult();

    if (!evento.puedeAgregarAsistente()) {
      result.addError(ValidationError.CAPACIDAD_COMPLETA,
              "El evento ha alcanzado su capacidad máxima (" + evento.getCapacidadMaxima() + " asistentes).");
    }

    return result;
  }

  /**
   * Verifica si ya existe un asistente con el mismo email en el evento
   */
  public ValidationResult validarEmailDuplicado(String email, Evento evento) {
    ValidationResult result = new ValidationResult();

    boolean emailExiste = evento.getAsistentes().stream()
            .anyMatch(a -> a.getEmail().equalsIgnoreCase(email.trim()));

    if (emailExiste) {
      result.addError(ValidationError.EMAIL_DUPLICADO,
              "Ya existe un asistente registrado con ese email en este evento.");
    }

    return result;
  }

  /**
   * Valida si un asistente completo es válido (para edición)
   */
  public ValidationResult validarAsistenteCompleto(Asistente asistente) {
    return validar(asistente.getNombre(), asistente.getEmail(), asistente.getTelefono(), null);
  }

}
