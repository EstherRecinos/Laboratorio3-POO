import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenderGT {

    private ArrayList<Trabajo> trabajos;
    private ArrayList<Nodo> nodos;
    private ArrayList<Lote> lotes;
    private double ingresosRegistro;
    private double ingresosReintentos;
    private Map<String, Double> ingresosRegistroPorCategoria;
    private Map<String, Double> ingresosReintentoPorCategoria;

    public RenderGT() {
        trabajos = new ArrayList<Trabajo>();
        nodos = new ArrayList<Nodo>();
        lotes = new ArrayList<Lote>();
        ingresosRegistro = 0.0;
        ingresosReintentos = 0.0;
        ingresosRegistroPorCategoria = new HashMap<String, Double>();
        ingresosReintentoPorCategoria = new HashMap<String, Double>();
    }

    // Registra un trabajo nuevo sin cobrarlo; el cobro se hace aparte con registrarIngresoRegistro.
    public boolean registrarTrabajo(Trabajo trabajo) {
        // Rechaza un trabajo nulo.
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo");
        }
        // Rechaza el trabajo si su codigo ya existe.
        if (buscarTrabajo(trabajo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un trabajo registrado con ese codigo");
        }
        // Agrega el trabajo a la lista del sistema.
        trabajos.add(trabajo);
        return true;
    }

    // Registra un nodo nuevo validando que su codigo sea unico.
    public boolean registrarNodo(Nodo nodo) {
        // Rechaza un nodo nulo.
        if (nodo == null) {
            throw new IllegalArgumentException("El nodo no puede ser nulo");
        }
        // Rechaza el nodo si su codigo ya existe.
        if (buscarNodo(nodo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un nodo registrado con ese codigo");
        }
        // Agrega el nodo a la lista del sistema.
        nodos.add(nodo);
        return true;
    }

    // Crea un lote para un nodo existente que no tenga otro lote sin cerrar.
    public Lote crearLote(String codigoNodo) {
        // Localiza el nodo por su codigo.
        Nodo nodo = buscarNodo(codigoNodo);
        // Rechaza la operacion si el nodo no existe.
        if (nodo == null) {
            throw new IllegalArgumentException("No existe un nodo registrado con ese codigo");
        }
        // Rechaza la operacion si el nodo ya tiene un lote sin cerrar.
        if (buscarLotePorNodo(codigoNodo) != null) {
            throw new IllegalStateException("El nodo ya tiene un lote sin cerrar");
        }
        // Crea el lote y lo guarda en la lista de lotes.
        Lote lote = new Lote(nodo);
        lotes.add(lote);
        return lote;
    }

    // Busca un trabajo por codigo; devuelve null si no existe.
    public Trabajo buscarTrabajo(String codigo) {
        // Recorre los trabajos registrados.
        for (int i = 0; i < trabajos.size(); i++) {
            // Devuelve el trabajo cuyo codigo coincide.
            if (trabajos.get(i).getCodigo().equals(codigo)) {
                return trabajos.get(i);
            }
        }
        return null;
    }

    // Busca un nodo por codigo; devuelve null si no existe.
    public Nodo buscarNodo(String codigo) {
        // Recorre los nodos registrados.
        for (int i = 0; i < nodos.size(); i++) {
            // Devuelve el nodo cuyo codigo coincide.
            if (nodos.get(i).getCodigo().equals(codigo)) {
                return nodos.get(i);
            }
        }
        return null;
    }

    // Busca el lote sin cerrar de un nodo; un nodo nunca tiene mas de uno a la vez.
    public Lote buscarLotePorNodo(String codigoNodo) {
        // Recorre los lotes creados.
        for (int i = 0; i < lotes.size(); i++) {
            Lote lote = lotes.get(i);
            // Devuelve el lote del nodo indicado que todavia no esta cerrado.
            if (lote.getNodo().getCodigo().equals(codigoNodo) && lote.getEstado() != EstadoLote.Cerrado) {
                return lote;
            }
        }
        return null;
    }

    // Cotiza un trabajo mostrando costo y tiempo, sin registrarlo ni generar ingresos.
    public String cotizarTrabajo(Trabajo trabajo) {
        // Rechaza un trabajo nulo.
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo");
        }
        // Pide al propio trabajo su costo y su tiempo de entrega.
        return "Costo estimado: Q" + String.format("%.2f", trabajo.calcularCosto()) +
                ", Tiempo estimado de entrega: " + trabajo.calcularTiempoEntrega() + " horas";
    }

    // Registra el ingreso por el cobro de un trabajo, en el total y en su categoria.
    public void registrarIngresoRegistro(Trabajo trabajo) {
        // Pide al trabajo su costo.
        double costo = trabajo.calcularCosto();
        // Suma el costo al ingreso total por registro.
        ingresosRegistro += costo;
        // Obtiene el nombre de la categoria a partir de la clase del trabajo.
        String categoria = trabajo.getClass().getSimpleName();
        double acumuladoCategoria = 0.0;
        // Recupera lo acumulado previamente en esa categoria, si existe.
        if (ingresosRegistroPorCategoria.containsKey(categoria)) {
            acumuladoCategoria = ingresosRegistroPorCategoria.get(categoria);
        }
        // Guarda el nuevo acumulado de la categoria.
        ingresosRegistroPorCategoria.put(categoria, acumuladoCategoria + costo);
    }

    // Registra el cargo de Q25 por un reintento, en el total y en la categoria del trabajo.
    public void registrarIngresoReintento(Trabajo trabajo) {
        double cargo = 25.0;
        // Suma el cargo al ingreso total por reintentos.
        ingresosReintentos += cargo;
        // Obtiene el nombre de la categoria a partir de la clase del trabajo.
        String categoria = trabajo.getClass().getSimpleName();
        double acumuladoCategoria = 0.0;
        // Recupera lo acumulado previamente en esa categoria, si existe.
        if (ingresosReintentoPorCategoria.containsKey(categoria)) {
            acumuladoCategoria = ingresosReintentoPorCategoria.get(categoria);
        }
        // Guarda el nuevo acumulado de la categoria.
        ingresosReintentoPorCategoria.put(categoria, acumuladoCategoria + cargo);
    }

    // Calcula el ingreso total sumando registro y reintentos.
    public double getIngresoTotal() {
        // Suma ambos tipos de ingreso.
        return ingresosRegistro + ingresosReintentos;
    }

    public ArrayList<Trabajo> getTrabajos() {
        return new ArrayList<Trabajo>(trabajos);
    }

    public ArrayList<Nodo> getNodos() {
        return new ArrayList<Nodo>(nodos);
    }

    // Genera el reporte de cantidad de trabajos por categoria y por estado.
    public String generarReporteTrabajos() {
        Map<String, Map<String, Integer>> conteo = new HashMap<String, Map<String, Integer>>();
        // Recorre todos los trabajos para contarlos.
        for (int i = 0; i < trabajos.size(); i++) {
            Trabajo trabajo = trabajos.get(i);
            String categoria = trabajo.getClass().getSimpleName();
            String estadoActual = trabajo.getEstado().toString();
            // Crea el conteo de la categoria si todavia no existe.
            if (!conteo.containsKey(categoria)) {
                conteo.put(categoria, new HashMap<String, Integer>());
            }
            Map<String, Integer> porEstado = conteo.get(categoria);
            int actual = 0;
            // Recupera la cantidad previa de ese estado, si existe.
            if (porEstado.containsKey(estadoActual)) {
                actual = porEstado.get(estadoActual);
            }
            // Suma uno al conteo de esa categoria y estado.
            porEstado.put(estadoActual, actual + 1);
        }
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== REPORTE DE TRABAJOS =====\n");
        // Recorre las categorias contadas para escribirlas en el reporte.
        for (String categoria : conteo.keySet()) {
            reporte.append("Categoria: ").append(categoria).append("\n");
            Map<String, Integer> porEstado = conteo.get(categoria);
            // Escribe la cantidad de trabajos por cada estado de la categoria.
            for (String estadoActual : porEstado.keySet()) {
                reporte.append("  ").append(estadoActual).append(": ").append(porEstado.get(estadoActual)).append("\n");
            }
        }
        return reporte.toString();
    }

    // Genera el reporte de ingresos totales, por registro, por reintentos y por categoria.
    public String generarReporteIngresos() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== REPORTE DE INGRESOS =====\n");
        // Escribe el ingreso total y su desglose por origen.
        reporte.append("Ingreso total: Q").append(String.format("%.2f", getIngresoTotal())).append("\n");
        reporte.append("Ingreso por registro: Q").append(String.format("%.2f", ingresosRegistro)).append("\n");
        reporte.append("Ingreso por reintentos: Q").append(String.format("%.2f", ingresosReintentos)).append("\n");
        reporte.append("Ingreso por registro segun categoria:\n");
        // Escribe el ingreso por registro de cada categoria.
        for (String categoria : ingresosRegistroPorCategoria.keySet()) {
            reporte.append("  ").append(categoria).append(": Q")
                    .append(String.format("%.2f", ingresosRegistroPorCategoria.get(categoria))).append("\n");
        }
        reporte.append("Ingreso por reintentos segun categoria:\n");
        // Escribe el ingreso por reintentos de cada categoria.
        for (String categoria : ingresosReintentoPorCategoria.keySet()) {
            reporte.append("  ").append(categoria).append(": Q")
                    .append(String.format("%.2f", ingresosReintentoPorCategoria.get(categoria))).append("\n");
        }
        return reporte.toString();
    }

    // Genera el reporte de lotes no cerrados con su nodo, horas asignadas y capacidad disponible.
    public String generarReporteLotes() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("===== REPORTE DE LOTES ABIERTOS =====\n");
        boolean hayLotesAbiertos = false;
        // Recorre todos los lotes creados.
        for (int i = 0; i < lotes.size(); i++) {
            Lote lote = lotes.get(i);
            // Incluye unicamente los lotes que no estan cerrados.
            if (lote.getEstado() != EstadoLote.Cerrado) {
                hayLotesAbiertos = true;
                reporte.append("Nodo: ").append(lote.getNodo().getCodigo())
                        .append(", Estado: ").append(lote.getEstado())
                        .append(", Horas asignadas: ").append(lote.calcularHorasAsignadas())
                        .append(", Capacidad disponible: ").append(lote.calcularCapacidadDisponible())
                        .append("\n");
            }
        }
        // Informa cuando no existe ningun lote abierto.
        if (!hayLotesAbiertos) {
            reporte.append("No hay lotes abiertos actualmente\n");
        }
        return reporte.toString();
    }
}
