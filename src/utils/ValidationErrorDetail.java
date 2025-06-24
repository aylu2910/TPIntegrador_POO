package utils;



/**
 * Clase que representa un error específico de validación
 */
class ValidationErrorDetail {
  private final ValidationError type;
  private final String message;

  public ValidationErrorDetail(ValidationError type, String message) {
    this.type = type;
    this.message = message;
  }

  public ValidationError getType() {
    return type;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return message;
  }
}
