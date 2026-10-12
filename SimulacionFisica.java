public class SimulacionFisica extends Trabajo {

    private double resolucionMalla;

    public SimulacionFisica(String codigo, String estudio, String proyecto, double horasEstimadas, Horario horario, double resolucionMalla) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);
        if (horario != Horario.Nocturno) {
            throw new IllegalArgumentException("Una simulacion fisica solo puede registrarse en horario nocturno");
        }
        if (resolucionMalla < 1 || resolucionMalla > 50) {
            throw new IllegalArgumentException("La resolucion de la malla debe estar entre 1 y 50 millones de celdas");
        }
        this.resolucionMalla = resolucionMalla;
    }

    public double getResolucionMalla() {
        return resolucionMalla;
    }

    // Calcula el costo: tarifa base nocturna, Q22 por hora estimada y Q65 fijos por uso de GPU.
    @Override
    public double calcularCosto() {
        // Usa la tarifa base nocturna de Q40, unico horario permitido.
        double tarifaBase = 40.0;
        // Suma la tarifa base, el cobro por horas estimadas y el cargo fijo de GPU.
        return tarifaBase + 22.0 * getHorasEstimadas() + 65.0;
    }

    // El tiempo de entrega de una simulacion fisica siempre es de 18 horas.
    @Override
    public double calcularTiempoEntrega() {
        // Devuelve las 18 horas fijas sin depender del horario.
        return 18.0;
    }

    // Una simulacion fisica solo es compatible con nodos que tengan GPU.
    @Override
    public boolean esCompatibleCon(Nodo nodo) {
        // Consulta si el nodo posee GPU.
        return nodo.tieneGPU();
    }

    // Una simulacion fisica permite un unico intento.
    @Override
    public int getMaxIntentos() {
        // Devuelve el maximo de intentos de esta categoria.
        return 1;
    }
}
