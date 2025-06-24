package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que encapsula el resultado de una validación
 */
public class ValidationResult {
  private final List<ValidationErrorDetail> errors;

  public ValidationResult() {
    this.errors = new ArrayList<>();
  }

  public void addError(ValidationError type, String message) {
    errors.add(new ValidationErrorDetail(type, message));
  }

  public void addErrors(List<ValidationErrorDetail> newErrors) {
    errors.addAll(newErrors);
  }

  public boolean isValid() {
    return errors.isEmpty();
  }

  public List<ValidationErrorDetail> getErrors() {
    return new ArrayList<>(errors);
  }

  public String getFirstErrorMessage() {
    return errors.isEmpty() ? null : errors.get(0).getMessage();
  }

  public String getAllErrorMessages() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < errors.size(); i++) {
      sb.append(errors.get(i).getMessage());
      if (i < errors.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  public boolean hasErrorType(ValidationError type) {
    return errors.stream().anyMatch(error -> error.getType() == type);
  }
}

