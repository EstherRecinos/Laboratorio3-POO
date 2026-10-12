public class AnimacionPostproduccion extends Animacion {

    private double valorRecursos;

    public AnimacionPostproduccion(String codigo, String estudio, String proyecto, double horasEstimadas, Horario horario, int fotogramas, double valorRecursos) {
        super(codigo, estudio, proyecto, horasEstimadas, horario, fotogramas);
        if (valorRecursos <= 0) {
            throw new IllegalArgumentException("El valor de los recursos licenciados debe ser mayor que cero");
        }
        this.valorRecursos = valorRecursos;
    }

    public double getValorRecursos() {
        return valorRecursos;
    }

    // Calcula el costo: el de una animacion mas Q80 de composicion y 5% de los recursos licenciados.
    @Override
    public double calcularCosto() {
        // Reutiliza el calculo de costo de la clase Animacion.
        double costoAnimacion = super.calcularCosto();
        // Agrega la composicion y el recargo por recursos licenciados.
        return costoAnimacion + 80.0 + 0.05 * valorRecursos;
    }

    // Calcula el tiempo de entrega: el de una animacion mas 12 horas.
    @Override
    public double calcularTiempoEntrega() {
        // Reutiliza el tiempo de Animacion y le suma las 12 horas de postproduccion.
        return super.calcularTiempoEntrega() + 12.0;
    }

    // Una animacion con postproduccion permite hasta 2 intentos.
    @Override
    public int getMaxIntentos() {
        // Devuelve el maximo de intentos de esta categoria.
        return 2;
    }
}
