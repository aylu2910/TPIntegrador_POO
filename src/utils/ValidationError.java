package utils;

/**
 * Enum que define los tipos de errores de validación
 */
enum ValidationError {
  // Errores de nombre
  NOMBRE_REQUERIDO,
  NOMBRE_MUY_CORTO,
  NOMBRE_MUY_LARGO,
  NOMBRE_FORMATO_INVALIDO,

  // Errores de email
  EMAIL_REQUERIDO,
  EMAIL_FORMATO_INVALIDO,
  EMAIL_DUPLICADO,

  // Errores de teléfono
  TELEFONO_REQUERIDO,
  TELEFONO_MUY_CORTO,
  TELEFONO_MUY_LARGO,
  TELEFONO_FORMATO_INVALIDO,

  // Errores de negocio
  CAPACIDAD_COMPLETA
}
