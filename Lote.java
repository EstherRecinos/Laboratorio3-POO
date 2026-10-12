import java.util.ArrayList;

public class Lote {

    private Nodo nodo;
    private ArrayList<Trabajo> trabajos;
    private EstadoLote estado;

    public Lote(Nodo nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("El nodo no puede ser nulo");
        }
        this.nodo = nodo;
        this.trabajos = new ArrayList<Trabajo>();
        this.estado = EstadoLote.EnPreparacion;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public EstadoLote getEstado() {
        return estado;
    }

    public ArrayList<Trabajo> getTrabajos() {
        return new ArrayList<Trabajo>(trabajos);
    }

    // Asigna un trabajo al lote validando estado del lote, estado del trabajo, compatibilidad y capacidad.
    public boolean agregarTrabajo(Trabajo trabajo) {
        // Rechaza un trabajo nulo.
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo");
        }
        // Solo se puede asignar mientras el lote esta en preparacion.
        if (estado != EstadoLote.EnPreparacion) {
            throw new IllegalStateException("Solo se pueden asignar trabajos mientras el lote este en preparacion");
        }
        // El trabajo debe estar en cola; esto tambien evita que este en otro lote o cancelado.
        if (trabajo.getEstado() != EstadoTrabajo.EnCola) {
            throw new IllegalStateException("El trabajo debe encontrarse en cola para poder asignarse");
        }
        // Consulta al propio trabajo si es compatible con el nodo del lote.
        if (!trabajo.esCompatibleCon(nodo)) {
            throw new IllegalStateException("El trabajo no es compatible con las caracteristicas del nodo");
        }
        // Verifica que las horas estimadas acumuladas no superen la capacidad del nodo.
        if (calcularHorasAsignadas() + trabajo.getHorasEstimadas() > nodo.getCapacidadMaxima()) {
            throw new IllegalStateException("No hay capacidad suficiente en el nodo para asignar este trabajo");
        }
        // Agrega el trabajo al lote solo despues de pasar todas las validaciones.
        trabajos.add(trabajo);
        // Marca el trabajo como asignado.
        trabajo.cambiarEstado(EstadoTrabajo.Asignado);
        return true;
    }

    // Retira un trabajo del lote y lo regresa a la cola sin afectar los ingresos.
    public boolean retirarTrabajo(Trabajo trabajo) {
        // Rechaza un trabajo nulo.
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo");
        }
        // Solo se puede retirar mientras el lote esta en preparacion.
        if (estado != EstadoLote.EnPreparacion) {
            throw new IllegalStateException("Solo se pueden retirar trabajos mientras el lote este en preparacion");
        }
        // El trabajo debe pertenecer a este lote.
        if (!trabajos.contains(trabajo)) {
            throw new IllegalStateException("El trabajo no se encuentra asignado a este lote");
        }
        // Quita el trabajo de la lista del lote.
        trabajos.remove(trabajo);
        // Devuelve el trabajo a la cola sin consumir intentos.
        trabajo.cambiarEstado(EstadoTrabajo.EnCola);
        return true;
    }

    // Suma las horas estimadas de los trabajos asignados al lote.
    public double calcularHorasAsignadas() {
        double total = 0.0;
        // Recorre los trabajos del lote.
        for (int i = 0; i < trabajos.size(); i++) {
            // Acumula las horas estimadas de cada trabajo.
            total += trabajos.get(i).getHorasEstimadas();
        }
        return total;
    }

    // Calcula las horas del nodo que todavia no han sido asignadas.
    public double calcularCapacidadDisponible() {
        // Resta las horas asignadas a la capacidad maxima del nodo.
        return nodo.getCapacidadMaxima() - calcularHorasAsignadas();
    }

    // Inicia la ejecucion del lote y pasa sus trabajos a en ejecucion.
    public void iniciar() {
        // Un lote solo puede iniciarse una vez, desde preparacion.
        if (estado != EstadoLote.EnPreparacion) {
            throw new IllegalStateException("El lote ya fue iniciado anteriormente");
        }
        // No se permite iniciar un lote vacio.
        if (trabajos.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar un lote vacio");
        }
        // Recorre los trabajos del lote.
        for (int i = 0; i < trabajos.size(); i++) {
            // Cambia cada trabajo al estado en ejecucion.
            trabajos.get(i).cambiarEstado(EstadoTrabajo.EnEjecucion);
        }
        // Cambia el estado del lote a en ejecucion.
        estado = EstadoLote.EnEjecucion;
    }

    // Registra el resultado de un trabajo y actualiza su estado segun el resultado e intentos.
    public void registrarResultado(Trabajo trabajo, boolean completado) {
        // Rechaza un trabajo nulo.
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo");
        }
        // Solo se registran resultados con el lote en ejecucion.
        if (estado != EstadoLote.EnEjecucion) {
            throw new IllegalStateException("Solo se pueden registrar resultados mientras el lote este en ejecucion");
        }
        // El trabajo debe pertenecer a este lote.
        if (!trabajos.contains(trabajo)) {
            throw new IllegalStateException("El trabajo no pertenece a este lote");
        }
        // El trabajo debe seguir en ejecucion, es decir, sin resultado registrado.
        if (trabajo.getEstado() != EstadoTrabajo.EnEjecucion) {
            throw new IllegalStateException("El trabajo no se encuentra en ejecucion");
        }
        // Si se completo, pasa al estado final completado.
        if (completado) {
            trabajo.cambiarEstado(EstadoTrabajo.Completado);
        } else {
            // Si fallo, consume un intento.
            trabajo.incrementarIntentos();
            // Si aun dispone de intentos, regresa a la cola.
            if (trabajo.puedeReintentarse()) {
                trabajo.cambiarEstado(EstadoTrabajo.EnCola);
            } else {
                // Si agoto los intentos, queda cancelado.
                trabajo.cambiarEstado(EstadoTrabajo.Cancelado);
            }
        }
        // Revisa si con este resultado el lote ya puede cerrarse.
        verificarCierre();
    }

    // Cierra el lote cuando todos sus trabajos ya tienen un resultado registrado.
    public void verificarCierre() {
        boolean todosResueltos = true;
        // Recorre los trabajos del lote.
        for (int i = 0; i < trabajos.size(); i++) {
            // Un trabajo aun en ejecucion significa que le falta resultado.
            if (trabajos.get(i).getEstado() == EstadoTrabajo.EnEjecucion) {
                todosResueltos = false;
                break;
            }
        }
        // Si todos tienen resultado, el lote se cierra y el nodo queda libre.
        if (todosResueltos) {
            estado = EstadoLote.Cerrado;
        }
    }

    // Devuelve la informacion del lote como texto.
    @Override
    public String toString() {
        // Une los datos principales del lote en una sola cadena.
        return "Nodo: " + nodo.getCodigo() +
                ", Estado: " + estado +
                ", Horas asignadas: " + calcularHorasAsignadas() +
                ", Capacidad disponible: " + calcularCapacidadDisponible() +
                ", Cantidad de trabajos: " + trabajos.size();
    }
}
