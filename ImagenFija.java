public class ImagenFija extends Trabajo {

    public ImagenFija(String codigo, String estudio, String proyecto, double horasEstimadas, Horario horario) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);
        if (horasEstimadas > 2) {
            throw new IllegalArgumentException("Una imagen fija no puede tener mas de 2 horas estimadas");
        }
    }

    // Calcula el costo de una imagen fija, que depende unicamente del horario.
    @Override
    public double calcularCosto() {
        // En horario diurno el costo fijo es Q45.
        if (getHorario() == Horario.Diurno) {
            return 45.0;
        }
        // En horario nocturno el costo fijo es Q30.
        return 30.0;
    }

    // Calcula el tiempo de entrega de una imagen fija segun el horario.
    @Override
    public double calcularTiempoEntrega() {
        // En horario diurno se entrega en 4 horas.
        if (getHorario() == Horario.Diurno) {
            return 4.0;
        }
        // En horario nocturno se entrega en 10 horas.
        return 10.0;
    }

    // Una imagen fija puede ejecutarse en cualquier nodo.
    @Override
    public boolean esCompatibleCon(Nodo nodo) {
        // No tiene restricciones de GPU, por lo que siempre es compatible.
        return true;
    }

    // Una imagen fija permite hasta 3 intentos.
    @Override
    public int getMaxIntentos() {
        // Devuelve el maximo de intentos de esta categoria.
        return 3;
    }
}
