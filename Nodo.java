public class Nodo {

    private String codigo;
    private double capacidadMaxima;
    private boolean tieneGPU;

    public Nodo(String codigo, double capacidadMaxima, boolean tieneGPU) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del nodo no puede estar vacio");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad maxima debe ser mayor que cero");
        }
        this.codigo = codigo;
        this.capacidadMaxima = capacidadMaxima;
        this.tieneGPU = tieneGPU;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean tieneGPU() {
        return tieneGPU;
    }

    // Devuelve la informacion del nodo como texto.
    @Override
    public String toString() {
        // Une los atributos del nodo en una sola cadena.
        return "Codigo: " + codigo +
                ", Capacidad maxima: " + capacidadMaxima +
                ", Tiene GPU: " + (tieneGPU ? "Si" : "No");
    }
}
