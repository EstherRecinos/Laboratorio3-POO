public class Animacion extends Trabajo {

    private int fotogramas;

    public Animacion(String codigo, String estudio, String proyecto, double horasEstimadas, Horario horario, int fotogramas) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);
        if (fotogramas <= 0) {
            throw new IllegalArgumentException("La cantidad de fotogramas debe ser un entero positivo");
        }
        this.fotogramas = fotogramas;
    }

    public int getFotogramas() {
        return fotogramas;
    }

    // Calcula las horas que representan los fotogramas de la animacion.
    public double calcularHorasFotogramas() {
        // Cada fotograma equivale a 0.02 horas.
        return fotogramas * 0.02;
    }

    // Calcula las horas que se facturan por la animacion.
    public double calcularHorasFacturables() {
        // Toma el mayor valor entre las horas estimadas y las horas por fotogramas.
        double mayor = Math.max(getHorasEstimadas(), calcularHorasFotogramas());
        // Redondea hacia arriba porque se cobra por hora o fraccion.
        return Math.ceil(mayor);
    }

    // Calcula el costo de la animacion: tarifa base segun horario mas Q15 por hora facturable.
    @Override
    public double calcularCosto() {
        double tarifaBase;
        // Define la tarifa base de Q60 en horario diurno.
        if (getHorario() == Horario.Diurno) {
            tarifaBase = 60.0;
        } else {
            // Define la tarifa base de Q40 en horario nocturno.
            tarifaBase = 40.0;
        }
        // Suma la tarifa base y el cobro de Q15 por cada hora facturable.
        return tarifaBase + 15.0 * calcularHorasFacturables();
    }

    // Calcula el tiempo de entrega de la animacion segun el horario.
    @Override
    public double calcularTiempoEntrega() {
        // En horario diurno se entrega en 24 horas.
        if (getHorario() == Horario.Diurno) {
            return 24.0;
        }
        // En horario nocturno se entrega en 36 horas.
        return 36.0;
    }

    // Una animacion puede ejecutarse en cualquier nodo.
    @Override
    public boolean esCompatibleCon(Nodo nodo) {
        // No tiene restricciones de GPU, por lo que siempre es compatible.
        return true;
    }

    // Una animacion permite hasta 3 intentos.
    @Override
    public int getMaxIntentos() {
        // Devuelve el maximo de intentos de esta categoria.
        return 3;
    }
}
