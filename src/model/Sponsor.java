package model;

class Sponsor extends Asistente {
  private String empresa;
  private double montoPatrocinio;

  public Sponsor(String nombre, String email, String telefono, String empresa, double montoPatrocinio) {
    super(nombre, email, telefono);
    this.empresa = empresa;
    this.montoPatrocinio = montoPatrocinio;
  }

  // Constructor para cargar desde archivo
  public Sponsor(String id, String nombre, String email, String telefono, String empresa, double montoPatrocinio) {
    super(id, nombre, email, telefono);
    this.empresa = empresa;
    this.montoPatrocinio = montoPatrocinio;
  }

  // Setters y Getters
  public String getEmpresa() {
    return empresa;
  }
  public void setEmpresa(String empresa) {
    this.empresa = empresa;
  }
  public double getMontoPatrocinio() {
    return montoPatrocinio;
  }
  public void setMontoPatrocinio(double montoPatrocinio) {
    this.montoPatrocinio = montoPatrocinio;
  }

  @Override
  public String toString() {
    return super.toString() + " - Empresa: " + empresa + " - Monto Patrocinio: " + montoPatrocinio;
  }

}

