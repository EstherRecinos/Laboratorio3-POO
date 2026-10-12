import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private RenderGT renderGT;
    private Scanner scanner;

    public Main() {
        renderGT = new RenderGT();
        scanner = new Scanner(System.in);
        cargarDatosIniciales();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.iniciar();
    }

    // Carga los nodos y trabajos iniciales de la demostracion, sin registrar ingresos.
    private void cargarDatosIniciales() {
        // Registra tres nodos: dos con GPU y uno sin GPU.
        renderGT.registrarNodo(new Nodo("ND01", 10.0, true));
        renderGT.registrarNodo(new Nodo("ND02", 8.0, false));
        renderGT.registrarNodo(new Nodo("ND03", 12.0, true));

        // Registra dos imagenes fijas, una diurna y una nocturna.
        renderGT.registrarTrabajo(new ImagenFija("IF01", "Estudio Maya", "Logo Animado", 1.5, Horario.Diurno));
        renderGT.registrarTrabajo(new ImagenFija("IF02", "Estudio Antigua", "Render Producto", 1.0, Horario.Nocturno));

        // Registra dos animaciones; AN01 tiene mas horas por fotogramas (12.2) que estimadas (9.5).
        renderGT.registrarTrabajo(new Animacion("AN01", "Estudio Maya", "Corto Animado", 9.5, Horario.Diurno, 610));
        renderGT.registrarTrabajo(new Animacion("AN02", "Estudio Xela", "Serie Capitulo 1", 20.0, Horario.Nocturno, 300));

        // Registra dos animaciones con postproduccion.
        renderGT.registrarTrabajo(new AnimacionPostproduccion("AP01", "Estudio Maya", "Corto con VFX", 9.5, Horario.Diurno, 610, 1800.0));
        renderGT.registrarTrabajo(new AnimacionPostproduccion("AP02", "Estudio Xela", "Comercial Postproducido", 15.0, Horario.Nocturno, 400, 2500.0));

        // Registra dos simulaciones fisicas, ambas nocturnas.
        renderGT.registrarTrabajo(new SimulacionFisica("SF01", "Estudio Antigua", "Simulacion de Humo", 5.0, Horario.Nocturno, 25.0));
        renderGT.registrarTrabajo(new SimulacionFisica("SF02", "Estudio Xela", "Simulacion de Fluidos", 8.0, Horario.Nocturno, 40.0));
    }

    // Muestra el menu en ciclo hasta que el usuario elige salir.
    private void iniciar() {
        boolean salir = false;
        // Repite el menu mientras el usuario no elija salir.
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion: ");
            // Ejecuta la accion correspondiente a la opcion elegida.
            switch (opcion) {
                case 1:
                    cotizarTrabajo();
                    break;
                case 2:
                    registrarTrabajo();
                    break;
                case 3:
                    registrarNodo();
                    break;
                case 4:
                    crearLote();
                    break;
                case 5:
                    asignarTrabajoALote();
                    break;
                case 6:
                    retirarTrabajoDeLote();
                    break;
                case 7:
                    iniciarLote();
                    break;
                case 8:
                    registrarResultadoTrabajo();
                    break;
                case 9:
                    consultarTrabajos();
                    break;
                case 10:
                    consultarNodos();
                    break;
                case 11:
                    System.out.println(renderGT.generarReporteTrabajos());
                    break;
                case 12:
                    System.out.println(renderGT.generarReporteIngresos());
                    break;
                case 13:
                    System.out.println(renderGT.generarReporteLotes());
                    break;
                case 14:
                    salir = true;
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opcion invalida, intente nuevamente");
            }
        }
        // Libera el Scanner al terminar el programa.
        scanner.close();
    }

    // Imprime las opciones del menu principal.
    private void mostrarMenu() {
        // Escribe cada opcion en consola.
        System.out.println();
        System.out.println("===== RENDERGT =====");
        System.out.println("1. Cotizar trabajo");
        System.out.println("2. Registrar trabajo");
        System.out.println("3. Registrar nodo");
        System.out.println("4. Crear lote");
        System.out.println("5. Asignar trabajo a lote");
        System.out.println("6. Retirar trabajo de lote");
        System.out.println("7. Iniciar lote");
        System.out.println("8. Registrar resultado de trabajo");
        System.out.println("9. Consultar trabajos");
        System.out.println("10. Consultar nodos");
        System.out.println("11. Generar reporte de trabajos");
        System.out.println("12. Generar reporte de ingresos");
        System.out.println("13. Generar reporte de lotes");
        System.out.println("14. Salir");
    }

    // Pide los datos del usuario y construye el trabajo de la categoria elegida.
    private Trabajo construirTrabajo() {
        // Muestra las categorias disponibles.
        System.out.println("Seleccione la categoria del trabajo:");
        System.out.println("1. Imagen fija");
        System.out.println("2. Animacion");
        System.out.println("3. Animacion con postproduccion");
        System.out.println("4. Simulacion fisica");
        int categoria = leerEntero("Categoria: ");
        // Solicita los datos comunes a todas las categorias.
        String codigo = leerTexto("Codigo del trabajo: ");
        String estudio = leerTexto("Estudio: ");
        String proyecto = leerTexto("Proyecto: ");
        double horasEstimadas = leerDouble("Horas estimadas: ");
        Horario horario = leerHorario();
        // Construye una imagen fija; su constructor valida el maximo de 2 horas.
        if (categoria == 1) {
            return new ImagenFija(codigo, estudio, proyecto, horasEstimadas, horario);
        } else if (categoria == 2) {
            // Pide los fotogramas y construye una animacion.
            int fotogramas = leerEntero("Cantidad de fotogramas: ");
            return new Animacion(codigo, estudio, proyecto, horasEstimadas, horario, fotogramas);
        } else if (categoria == 3) {
            // Pide fotogramas y valor de recursos, y construye una animacion con postproduccion.
            int fotogramas = leerEntero("Cantidad de fotogramas: ");
            double valorRecursos = leerDouble("Valor de los recursos licenciados: ");
            return new AnimacionPostproduccion(codigo, estudio, proyecto, horasEstimadas, horario, fotogramas, valorRecursos);
        } else if (categoria == 4) {
            // Pide la resolucion y construye una simulacion fisica; su constructor valida rango y horario.
            double resolucionMalla = leerDouble("Resolucion de la malla (millones de celdas): ");
            return new SimulacionFisica(codigo, estudio, proyecto, horasEstimadas, horario, resolucionMalla);
        }
        // Rechaza cualquier categoria que no exista.
        throw new IllegalArgumentException("Categoria invalida");
    }

    // Pide el horario al usuario y repite la pregunta hasta recibir una opcion valida.
    private Horario leerHorario() {
        // Repite hasta obtener una opcion valida.
        while (true) {
            System.out.println("Horario:");
            System.out.println("1. Diurno");
            System.out.println("2. Nocturno");
            int opcion = leerEntero("Seleccione una opcion: ");
            // Devuelve el horario segun la opcion elegida.
            if (opcion == 1) {
                return Horario.Diurno;
            } else if (opcion == 2) {
                return Horario.Nocturno;
            }
            System.out.println("Opcion invalida, intente nuevamente");
        }
    }

    // Cotiza un trabajo sin registrarlo ni generar ingresos.
    private void cotizarTrabajo() {
        try {
            // Construye el trabajo candidato y muestra su cotizacion.
            Trabajo trabajo = construirTrabajo();
            System.out.println(renderGT.cotizarTrabajo(trabajo));
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Cotiza un trabajo y, si el usuario acepta, lo registra y cobra su monto completo.
    private void registrarTrabajo() {
        try {
            // Construye el trabajo candidato y muestra su cotizacion.
            Trabajo trabajo = construirTrabajo();
            System.out.println(renderGT.cotizarTrabajo(trabajo));
            boolean aceptar = leerSiNo("Desea registrar este trabajo (s/n): ");
            if (aceptar) {
                // Registra el trabajo y luego registra su cobro.
                renderGT.registrarTrabajo(trabajo);
                renderGT.registrarIngresoRegistro(trabajo);
                System.out.println("Trabajo registrado correctamente");
            } else {
                // Si no acepta, no se registra nada ni se cobra.
                System.out.println("Registro cancelado, no se realizaron cambios");
            }
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Pide los datos de un nodo y lo registra.
    private void registrarNodo() {
        try {
            // Solicita los datos del nodo.
            String codigo = leerTexto("Codigo del nodo: ");
            double capacidadMaxima = leerDouble("Capacidad maxima (horas): ");
            boolean tieneGPU = leerSiNo("Posee GPU (s/n): ");
            // Construye el nodo y lo registra en el sistema.
            Nodo nodo = new Nodo(codigo, capacidadMaxima, tieneGPU);
            renderGT.registrarNodo(nodo);
            System.out.println("Nodo registrado correctamente");
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Crea un lote asociado al nodo indicado.
    private void crearLote() {
        try {
            // Pide el nodo y crea su lote.
            String codigoNodo = leerTexto("Codigo del nodo: ");
            renderGT.crearLote(codigoNodo);
            System.out.println("Lote creado correctamente");
        } catch (IllegalArgumentException e) {
            // Informa que el nodo no existe o los datos son invalidos.
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Informa que el nodo ya tiene un lote sin cerrar.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Asigna un trabajo en cola al lote activo de un nodo.
    private void asignarTrabajoALote() {
        try {
            // Localiza el lote activo del nodo indicado.
            String codigoNodo = leerTexto("Codigo del nodo del lote: ");
            Lote lote = renderGT.buscarLotePorNodo(codigoNodo);
            if (lote == null) {
                System.out.println("No existe un lote sin cerrar para ese nodo");
                return;
            }
            // Localiza el trabajo indicado.
            String codigoTrabajo = leerTexto("Codigo del trabajo a asignar: ");
            Trabajo trabajo = renderGT.buscarTrabajo(codigoTrabajo);
            if (trabajo == null) {
                System.out.println("No existe un trabajo registrado con ese codigo");
                return;
            }
            // Asigna el trabajo; el lote valida estado, compatibilidad y capacidad.
            lote.agregarTrabajo(trabajo);
            System.out.println("Trabajo asignado correctamente");
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Retira un trabajo del lote activo de un nodo, devolviendolo a la cola.
    private void retirarTrabajoDeLote() {
        try {
            // Localiza el lote activo del nodo indicado.
            String codigoNodo = leerTexto("Codigo del nodo del lote: ");
            Lote lote = renderGT.buscarLotePorNodo(codigoNodo);
            if (lote == null) {
                System.out.println("No existe un lote sin cerrar para ese nodo");
                return;
            }
            // Localiza el trabajo indicado.
            String codigoTrabajo = leerTexto("Codigo del trabajo a retirar: ");
            Trabajo trabajo = renderGT.buscarTrabajo(codigoTrabajo);
            if (trabajo == null) {
                System.out.println("No existe un trabajo registrado con ese codigo");
                return;
            }
            // Retira el trabajo; no se modifica ningun ingreso.
            lote.retirarTrabajo(trabajo);
            System.out.println("Trabajo retirado correctamente");
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Inicia el lote de un nodo y cobra Q25 por cada trabajo que ya tenia intentos previos.
    private void iniciarLote() {
        try {
            // Localiza el lote activo del nodo indicado.
            String codigoNodo = leerTexto("Codigo del nodo del lote: ");
            Lote lote = renderGT.buscarLotePorNodo(codigoNodo);
            if (lote == null) {
                System.out.println("No existe un lote sin cerrar para ese nodo");
                return;
            }
            // Guarda los trabajos del lote antes de iniciarlo.
            ArrayList<Trabajo> trabajosDelLote = lote.getTrabajos();
            // Inicia el lote; si esta vacio o ya inicio, se lanza una excepcion y no se cobra nada.
            lote.iniciar();
            // Recorre los trabajos para identificar los reintentos.
            for (int i = 0; i < trabajosDelLote.size(); i++) {
                Trabajo trabajo = trabajosDelLote.get(i);
                // Un trabajo con intentos previos es un reintento y genera el cargo adicional.
                if (trabajo.getIntentos() > 0) {
                    renderGT.registrarIngresoReintento(trabajo);
                }
            }
            System.out.println("Lote iniciado correctamente");
        } catch (IllegalStateException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Registra si un trabajo del lote se completo o fallo.
    private void registrarResultadoTrabajo() {
        try {
            // Localiza el lote activo del nodo indicado.
            String codigoNodo = leerTexto("Codigo del nodo del lote: ");
            Lote lote = renderGT.buscarLotePorNodo(codigoNodo);
            if (lote == null) {
                System.out.println("No existe un lote sin cerrar para ese nodo");
                return;
            }
            // Localiza el trabajo indicado.
            String codigoTrabajo = leerTexto("Codigo del trabajo: ");
            Trabajo trabajo = renderGT.buscarTrabajo(codigoTrabajo);
            if (trabajo == null) {
                System.out.println("No existe un trabajo registrado con ese codigo");
                return;
            }
            // Pregunta el resultado y lo registra en el lote.
            boolean completado = leerSiNo("El trabajo se completo correctamente (s/n): ");
            lote.registrarResultado(trabajo, completado);
            System.out.println("Resultado registrado correctamente");
        } catch (IllegalArgumentException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Informa el motivo del rechazo sin detener el programa.
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Muestra todos los trabajos registrados.
    private void consultarTrabajos() {
        ArrayList<Trabajo> trabajos = renderGT.getTrabajos();
        // Informa si todavia no hay trabajos.
        if (trabajos.isEmpty()) {
            System.out.println("No hay trabajos registrados");
            return;
        }
        // Imprime la informacion de cada trabajo.
        for (int i = 0; i < trabajos.size(); i++) {
            System.out.println(trabajos.get(i).toString());
        }
    }

    // Muestra todos los nodos registrados.
    private void consultarNodos() {
        ArrayList<Nodo> nodos = renderGT.getNodos();
        // Informa si todavia no hay nodos.
        if (nodos.isEmpty()) {
            System.out.println("No hay nodos registrados");
            return;
        }
        // Imprime la informacion de cada nodo.
        for (int i = 0; i < nodos.size(); i++) {
            System.out.println(nodos.get(i).toString());
        }
    }

    // Lee un entero; si el formato es incorrecto limpia la entrada y vuelve a preguntar.
    private int leerEntero(String mensaje) {
        int valor;
        // Repite hasta recibir un entero valido.
        while (true) {
            System.out.print(mensaje);
            try {
                valor = scanner.nextInt();
                // Consume el salto de linea pendiente.
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida, debe ingresar un numero entero");
                // Descarta la entrada incorrecta para poder continuar.
                scanner.nextLine();
            }
        }
    }

    // Lee un decimal; si el formato es incorrecto limpia la entrada y vuelve a preguntar.
    private double leerDouble(String mensaje) {
        double valor;
        // Repite hasta recibir un numero valido.
        while (true) {
            System.out.print(mensaje);
            try {
                valor = scanner.nextDouble();
                // Consume el salto de linea pendiente.
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida, debe ingresar un numero");
                // Descarta la entrada incorrecta para poder continuar.
                scanner.nextLine();
            }
        }
    }

    // Muestra un mensaje y lee una linea de texto.
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        // Devuelve la linea completa escrita por el usuario.
        return scanner.nextLine();
    }

    // Lee una respuesta s o n y repite la pregunta hasta recibir una valida.
    private boolean leerSiNo(String mensaje) {
        // Repite hasta recibir s o n.
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim().toLowerCase();
            // Devuelve true si la respuesta es afirmativa.
            if (respuesta.equals("s")) {
                return true;
            }
            // Devuelve false si la respuesta es negativa.
            if (respuesta.equals("n")) {
                return false;
            }
            System.out.println("Respuesta invalida, ingrese s o n");
        }
    }
}
