public abstract class Trabajo {

    private String codigo;
    private String estudio;
    private String proyecto;
    private double horasEstimadas;
    private Horario horario;
    private EstadoTrabajo estado;
    private int intentos;

    public Trabajo(String codigo, String estudio, String proyecto, double horasEstimadas, Horario horario) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del trabajo no puede estar vacio");
        }
        if (estudio == null || estudio.trim().isEmpty()) {
            throw new IllegalArgumentException("El estudio no puede estar vacio");
        }
        if (proyecto == null || proyecto.trim().isEmpty()) {
            throw new IllegalArgumentException("El proyecto no puede estar vacio");
        }
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas estimadas deben ser mayores que cero");
        }
        this.codigo = codigo;
        this.estudio = estudio;
        this.proyecto = proyecto;
        this.horasEstimadas = horasEstimadas;
        this.horario = horario;
        this.estado = EstadoTrabajo.EnCola;
        this.intentos = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstudio() {
        return estudio;
    }

    public String getProyecto() {
        return proyecto;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public Horario getHorario() {
        return horario;
    }

    public EstadoTrabajo getEstado() {
        return estado;
    }

    public int getIntentos() {
        return intentos;
    }

    // Calcula el costo del trabajo; cada categoria define su propia formula.
    public abstract double calcularCosto();

    // Calcula el tiempo de entrega en horas; cada categoria define sus propios tiempos.
    public abstract double calcularTiempoEntrega();

    // Indica si el trabajo puede ejecutarse en el nodo recibido segun las reglas de su categoria.
    public abstract boolean esCompatibleCon(Nodo nodo);

    // Devuelve el maximo de intentos permitido segun la categoria.
    public abstract int getMaxIntentos();

    // Indica si el trabajo todavia dispone de intentos de ejecucion.
    public boolean puedeReintentarse() {
        // Compara los intentos realizados contra el maximo permitido de la categoria.
        return intentos < getMaxIntentos();
    }

    // Registra un intento mas de ejecucion sobre el trabajo.
    public void incrementarIntentos() {
        // Aumenta en uno el contador de intentos.
        intentos++;
    }

    // Cambia el estado del trabajo dentro de su ciclo de vida.
    public void cambiarEstado(EstadoTrabajo nuevoEstado) {
        // Reemplaza el estado actual por el nuevo estado recibido.
        this.estado = nuevoEstado;
    }

    // Devuelve la informacion del trabajo como texto.
    @Override
    public String toString() {
        // Une todos los atributos del trabajo en una sola cadena.
        return "Codigo: " + codigo +
                ", Estudio: " + estudio +
                ", Proyecto: " + proyecto +
                ", Horas estimadas: " + horasEstimadas +
                ", Horario: " + horario +
                ", Estado: " + estado +
                ", Intentos: " + intentos;
    }
}
