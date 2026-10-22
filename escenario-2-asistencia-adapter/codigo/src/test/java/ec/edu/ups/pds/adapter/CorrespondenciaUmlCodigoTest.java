package ec.edu.ups.pds.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verifica que el diagrama {@code ../uml/*_mejorado.puml} y el codigo coincidan: clases e
 * interfaces, atributos, constructores y metodos (en ambos sentidos), herencia, realizacion,
 * asociaciones (por atributo) y dependencias (por firma).
 */
class CorrespondenciaUmlCodigoTest {

    private static final String PAQUETE = "ec.edu.ups.pds.adapter";
    /** Clases auxiliares que no son participantes del patron y no se modelan. */
    private static final Set<String> FUERA_DEL_MODELO = Set.of("Demo");

    private static final Pattern CABECERA = Pattern.compile(
            "^(abstract\\s+class|class|interface)\\s+(\\w+)((?:\\s*<<[^>]+>>)*)\\s*(?:#\\w+)?\\s*(\\{)?$");
    private static final Pattern MIEMBRO = Pattern.compile(
            "^(?:\\{(abstract|static)\\}\\s*)?([+\\-#~])\\s*(?:\\{(abstract|static)\\}\\s*)?(\\w+)\\s*(\\((.*)\\))?\\s*(?::\\s*(.+))?$");
    private static final Pattern RELACION = Pattern.compile(
            "^(\\w+)\\s*(?:\"[^\"]*\"\\s*)?(<\\|--|<\\|\\.\\.|\\*-->|o-->|-->|\\.\\.>)\\s*(?:\"[^\"]*\"\\s*)?(\\w+)\\s*(?::\\s*(.*))?$");
    private static final Pattern ESTEREOTIPO = Pattern.compile("<<([^>]+)>>");

    private record MiembroUml(char visibilidad, String nombre, boolean esOperacion, boolean abstracto,
                              boolean estatico, List<String> parametros, String tipo) {
    }

    private record ClaseUml(String tipo, String nombre, List<String> estereotipos, List<MiembroUml> miembros) {
    }

    private record RelacionUml(String origen, String operador, String destino, String etiqueta) {
    }

    private static final Map<String, ClaseUml> clases = new LinkedHashMap<>();
    private static final List<RelacionUml> relaciones = new ArrayList<>();

    @BeforeAll
    static void leerUml() throws IOException {
        List<Path> archivos;
        try (Stream<Path> listado = Files.list(Path.of("../uml"))) {
            archivos = listado.filter(p -> p.getFileName().toString().endsWith("_mejorado.puml")).toList();
        }
        assertEquals(1, archivos.size(), "Debe existir exactamente un ../uml/*_mejorado.puml");
        ClaseUml actual = null;
        boolean enBloque = false;
        for (String bruta : Files.readAllLines(archivos.getFirst())) {
            String linea = bruta.strip();
            if (enBloque) {
                enBloque = !linea.equals("end note") && !linea.equals("endlegend");
                continue;
            }
            if (linea.isEmpty() || linea.startsWith("'") || linea.equals("--")) {
                continue;
            }
            if (actual != null) {
                if (linea.equals("}")) {
                    actual = null;
                } else {
                    actual.miembros().add(leerMiembro(linea));
                }
                continue;
            }
            if ((linea.startsWith("note ") && !linea.contains(":")) || linea.startsWith("legend")) {
                enBloque = true;
                continue;
            }
            Matcher cabecera = CABECERA.matcher(linea);
            if (cabecera.matches()) {
                List<String> estereotipos = new ArrayList<>();
                Matcher e = ESTEREOTIPO.matcher(cabecera.group(3));
                while (e.find()) {
                    estereotipos.add(e.group(1));
                }
                ClaseUml clase = new ClaseUml(cabecera.group(1).replaceAll("\\s+", " "), cabecera.group(2),
                        estereotipos, new ArrayList<>());
                clases.put(clase.nombre(), clase);
                actual = cabecera.group(4) != null ? clase : null;
                continue;
            }
            Matcher relacion = RELACION.matcher(linea);
            if (relacion.matches()) {
                relaciones.add(new RelacionUml(relacion.group(1), relacion.group(2), relacion.group(3),
                        relacion.group(4) == null ? null : relacion.group(4).strip()));
            }
        }
        assertTrue(!clases.isEmpty(), "No se encontraron clases en el UML");
    }

    private static MiembroUml leerMiembro(String linea) {
        Matcher m = MIEMBRO.matcher(linea);
        if (!m.matches()) {
            throw new IllegalStateException("Miembro UML no reconocido: " + linea);
        }
        boolean abstracto = "abstract".equals(m.group(1)) || "abstract".equals(m.group(3));
        boolean estatico = "static".equals(m.group(1)) || "static".equals(m.group(3));
        boolean esOperacion = m.group(5) != null;
        List<String> parametros = new ArrayList<>();
        if (esOperacion && !m.group(6).isBlank()) {
            for (String parametro : separarParametros(m.group(6))) {
                parametros.add(normalizar(parametro.substring(parametro.indexOf(':') + 1)));
            }
        }
        String tipo = m.group(7) == null ? null : normalizar(m.group(7));
        return new MiembroUml(m.group(2).charAt(0), m.group(4), esOperacion, abstracto, estatico, parametros, tipo);
    }

    /** Separa por comas que no esten dentro de un tipo generico. */
    private static List<String> separarParametros(String texto) {
        List<String> partes = new ArrayList<>();
        int profundidad = 0;
        int inicio = 0;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '<') {
                profundidad++;
            } else if (c == '>') {
                profundidad--;
            } else if (c == ',' && profundidad == 0) {
                partes.add(texto.substring(inicio, i));
                inicio = i + 1;
            }
        }
        partes.add(texto.substring(inicio));
        return partes;
    }

    private static String normalizar(String tipo) {
        return tipo.replaceAll("\\s+", "");
    }

    /** Nombre simple de un tipo Java, sin paquetes: java.util.List&lt;a.b.X&gt; -&gt; List&lt;X&gt;. */
    private static String simple(Type tipo) {
        return normalizar(tipo.getTypeName().replaceAll("(?:[a-z_][\\w]*\\.)+", ""));
    }

    private static Class<?> cargar(String nombre) {
        try {
            return Class.forName(PAQUETE + "." + nombre);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static char visibilidad(int modificadores) {
        if (Modifier.isPublic(modificadores)) {
            return '+';
        }
        if (Modifier.isProtected(modificadores)) {
            return '#';
        }
        if (Modifier.isPrivate(modificadores)) {
            return '-';
        }
        return '~';
    }

    private static List<String> parametros(Executable ejecutable) {
        return Arrays.stream(ejecutable.getGenericParameterTypes()).map(CorrespondenciaUmlCodigoTest::simple).toList();
    }

    private static boolean coincide(MiembroUml miembro, Field campo) {
        return !miembro.esOperacion() && miembro.nombre().equals(campo.getName())
                && miembro.visibilidad() == visibilidad(campo.getModifiers())
                && miembro.estatico() == Modifier.isStatic(campo.getModifiers())
                && miembro.tipo().equals(simple(campo.getGenericType()));
    }

    private static boolean coincide(MiembroUml miembro, Method metodo, boolean enInterfaz) {
        return miembro.esOperacion() && miembro.nombre().equals(metodo.getName())
                && miembro.visibilidad() == visibilidad(metodo.getModifiers())
                && miembro.estatico() == Modifier.isStatic(metodo.getModifiers())
                && (enInterfaz || miembro.abstracto() == Modifier.isAbstract(metodo.getModifiers()))
                && miembro.parametros().equals(parametros(metodo))
                && miembro.tipo() != null && miembro.tipo().equals(simple(metodo.getGenericReturnType()));
    }

    private static boolean coincide(MiembroUml miembro, Constructor<?> constructor) {
        return miembro.esOperacion() && miembro.tipo() == null
                && miembro.nombre().equals(constructor.getDeclaringClass().getSimpleName())
                && miembro.visibilidad() == visibilidad(constructor.getModifiers())
                && miembro.parametros().equals(parametros(constructor));
    }

    private static List<Field> campos(Class<?> clase) {
        return Arrays.stream(clase.getDeclaredFields()).filter(f -> !f.isSynthetic()).toList();
    }

    private static List<Method> metodos(Class<?> clase) {
        return Arrays.stream(clase.getDeclaredMethods()).filter(m -> !m.isSynthetic() && !m.isBridge()).toList();
    }

    private static void sinErrores(List<String> errores) {
        assertTrue(errores.isEmpty(), "Diferencias UML-codigo:\n  " + String.join("\n  ", errores));
    }

    @Test
    void cadaClaseDelUmlExisteConElMismoTipo() {
        List<String> errores = new ArrayList<>();
        for (ClaseUml uml : clases.values()) {
            Class<?> clase = cargar(uml.nombre());
            if (clase == null) {
                errores.add("No existe la clase " + uml.nombre());
                continue;
            }
            boolean abstracta = Modifier.isAbstract(clase.getModifiers());
            boolean correcto = switch (uml.tipo()) {
                case "interface" -> clase.isInterface();
                case "abstract class" -> abstracta && !clase.isInterface();
                default -> !abstracta && !clase.isInterface();
            };
            if (!correcto) {
                errores.add(uml.nombre() + " no es " + uml.tipo());
            }
            if (uml.estereotipos().contains("Exception")
                    && (!Exception.class.isAssignableFrom(clase) || RuntimeException.class.isAssignableFrom(clase))) {
                errores.add(uml.nombre() + " debe ser una excepcion verificada (<<Exception>>)");
            }
            if (uml.estereotipos().contains("RuntimeException") && !RuntimeException.class.isAssignableFrom(clase)) {
                errores.add(uml.nombre() + " debe extender RuntimeException");
            }
        }
        sinErrores(errores);
    }

    @Test
    void elPaqueteNoTieneClasesFueraDelUml() throws IOException, URISyntaxException {
        Path raiz = Path.of(SistemaAsistencia.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Path carpeta = raiz.resolve(PAQUETE.replace('.', '/'));
        List<String> sobrantes;
        try (Stream<Path> listado = Files.list(carpeta)) {
            sobrantes = listado.map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".class") && !n.contains("$"))
                    .map(n -> n.substring(0, n.length() - ".class".length()))
                    .filter(n -> !clases.containsKey(n) && !FUERA_DEL_MODELO.contains(n))
                    .toList();
        }
        assertTrue(sobrantes.isEmpty(), "Clases sin modelar en el UML: " + sobrantes);
    }

    @Test
    void cadaMiembroDelUmlExisteEnElCodigo() {
        List<String> errores = new ArrayList<>();
        for (ClaseUml uml : clases.values()) {
            Class<?> clase = cargar(uml.nombre());
            if (clase == null) {
                continue;
            }
            for (MiembroUml miembro : uml.miembros()) {
                boolean existe = !miembro.esOperacion()
                        ? campos(clase).stream().anyMatch(f -> coincide(miembro, f))
                        : miembro.tipo() == null
                        ? Arrays.stream(clase.getDeclaredConstructors()).anyMatch(c -> coincide(miembro, c))
                        : metodos(clase).stream().anyMatch(m -> coincide(miembro, m, clase.isInterface()));
                if (!existe) {
                    errores.add(uml.nombre() + ": falta o no coincide " + miembro.visibilidad() + miembro.nombre()
                            + (miembro.esOperacion() ? miembro.parametros() : "") + " : " + miembro.tipo());
                }
            }
        }
        sinErrores(errores);
    }

    @Test
    void cadaMiembroDelCodigoEstaEnElUml() {
        List<String> errores = new ArrayList<>();
        for (ClaseUml uml : clases.values()) {
            Class<?> clase = cargar(uml.nombre());
            if (clase == null) {
                continue;
            }
            for (Field campo : campos(clase)) {
                if (uml.miembros().stream().noneMatch(m -> coincide(m, campo))) {
                    errores.add(uml.nombre() + ": atributo sin modelar " + campo.getName());
                }
            }
            for (Method metodo : metodos(clase)) {
                if (uml.miembros().stream().noneMatch(m -> coincide(m, metodo, clase.isInterface()))) {
                    errores.add(uml.nombre() + ": metodo sin modelar " + metodo.getName() + parametros(metodo));
                }
            }
            boolean sinConstructoresEnUml = uml.miembros().stream()
                    .noneMatch(m -> m.esOperacion() && m.tipo() == null);
            for (Constructor<?> constructor : clase.getDeclaredConstructors()) {
                boolean implicito = sinConstructoresEnUml && constructor.getParameterCount() == 0
                        && Modifier.isPublic(constructor.getModifiers());
                if (!implicito && uml.miembros().stream().noneMatch(m -> coincide(m, constructor))) {
                    errores.add(uml.nombre() + ": constructor sin modelar " + parametros(constructor));
                }
            }
        }
        sinErrores(errores);
    }

    @Test
    void lasRelacionesDelUmlExistenEnElCodigo() {
        List<String> errores = new ArrayList<>();
        for (RelacionUml relacion : relaciones) {
            Class<?> origen = cargar(relacion.origen());
            Class<?> destino = cargar(relacion.destino());
            if (origen == null || destino == null) {
                errores.add("Relacion con clase inexistente: " + relacion);
                continue;
            }
            Pattern menciona = Pattern.compile("\\b" + destino.getSimpleName() + "\\b");
            boolean existe = switch (relacion.operador()) {
                case "<|--" -> destino.getSuperclass() == origen;
                case "<|.." -> Arrays.asList(destino.getInterfaces()).contains(origen);
                case "..>" -> Stream.concat(metodos(origen).stream(), Arrays.stream(origen.getDeclaredConstructors()))
                        .anyMatch(e -> Stream.concat(
                                        Stream.concat(Arrays.stream(e.getGenericParameterTypes()),
                                                Arrays.stream(e.getGenericExceptionTypes())),
                                        e instanceof Method m ? Stream.of(m.getGenericReturnType()) : Stream.empty())
                                .anyMatch(t -> menciona.matcher(simple(t)).find()));
                default -> campos(origen).stream().anyMatch(f -> f.getName().equals(relacion.etiqueta())
                        && menciona.matcher(simple(f.getGenericType())).find());
            };
            if (!existe) {
                errores.add("No se refleja en el codigo: " + relacion.origen() + " " + relacion.operador() + " "
                        + relacion.destino() + (relacion.etiqueta() == null ? "" : " : " + relacion.etiqueta()));
            }
        }
        assertTrue(!relaciones.isEmpty(), "No se encontraron relaciones en el UML");
        sinErrores(errores);
    }
}
