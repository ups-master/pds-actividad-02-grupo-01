package ec.edu.ups.pds.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Client: el sistema funciona igual con cualquier proveedor y no conoce al fabricante. */
class SistemaAsistenciaTest {

    private static final ZoneId ZONA = ZoneId.of("America/Guayaquil");
    private static final LocalDate FECHA = LocalDate.of(2026, 10, 22);
    private static final String COLABORADOR = "0102030405";

    private static long epochMs(int hora, int minuto) {
        return FECHA.atTime(hora, minuto).atZone(ZONA).toInstant().toEpochMilli();
    }

    @Test
    @DisplayName("SistemaAsistencia da el mismo reporte con el proveedor anterior y con el adaptador")
    void mismoResultadoConAmbosProveedores() {
        BiometricoInstitucional anterior = new BiometricoInstitucional();
        anterior.registrar(new RegistroAsistencia(COLABORADOR, FECHA, LocalTime.of(8, 2), LocalTime.of(17, 5)));
        anterior.registrar(new RegistroAsistencia("0999999999", FECHA, LocalTime.of(9, 30), null));

        SdkTerminalHuella sdk = new SdkTerminalHuella();
        sdk.cargarMarcacion(new MarcacionTerminal(COLABORADOR, epochMs(17, 5), "T-01"));
        sdk.cargarMarcacion(new MarcacionTerminal(COLABORADOR, epochMs(8, 2), "T-02"));
        sdk.cargarMarcacion(new MarcacionTerminal("0999999999", epochMs(9, 30), "T-01"));

        SistemaAsistencia conAnterior = new SistemaAsistencia(anterior);
        SistemaAsistencia conAdaptador = new SistemaAsistencia(new TerminalHuellaAdapter(sdk, ZONA));

        for (String colaborador : List.of(COLABORADOR, "0999999999", "0000000000")) {
            assertEquals(conAnterior.generarReporte(colaborador, FECHA),
                    conAdaptador.generarReporte(colaborador, FECHA));
        }
        assertEquals("Colaborador 0102030405 | 2026-10-22 | ingreso 08:02 | salida 17:05",
                conAdaptador.generarReporte(COLABORADOR, FECHA));
        assertEquals("Colaborador 0000000000 | 2026-10-22 | ingreso sin registro | salida sin registro",
                conAdaptador.generarReporte("0000000000", FECHA));
    }

    @Test
    @DisplayName("Al sistema solo llega la excepcion del dominio")
    void soloLlegaLaExcepcionDelDominio() {
        ErrorTerminalHuella original = new ErrorTerminalHuella("ERR-99");
        SdkTerminalHuella sdkFallido = new SdkTerminalHuella() {
            @Override
            public List<MarcacionTerminal> recuperarMarcaciones(String codigo, long desde, long hasta)
                    throws ErrorTerminalHuella {
                throw original;
            }
        };
        SistemaAsistencia sistema = new SistemaAsistencia(new TerminalHuellaAdapter(sdkFallido, ZONA));

        ConsultaAsistenciaException error = assertThrows(ConsultaAsistenciaException.class,
                () -> sistema.generarReporte(COLABORADOR, FECHA));
        assertSame(original, error.getCause());
    }

    @Test
    @DisplayName("Ni el cliente ni el Target mencionan tipos del fabricante")
    void elClienteNoConoceAlFabricante() {
        Set<Class<?>> fabricante = Set.of(SdkTerminalHuella.class, MarcacionTerminal.class, ErrorTerminalHuella.class);
        List<Type> tipos = new ArrayList<>();
        for (Class<?> clase : List.of(SistemaAsistencia.class, ServicioAsistencia.class, RegistroAsistencia.class)) {
            Arrays.stream(clase.getDeclaredFields()).forEach(f -> tipos.add(f.getGenericType()));
            Stream.concat(Arrays.stream(clase.getDeclaredMethods()), Arrays.stream(clase.getDeclaredConstructors()))
                    .forEach(e -> agregarFirma(e, tipos));
        }

        for (Type tipo : tipos) {
            for (Class<?> ajeno : fabricante) {
                assertTrue(!tipo.getTypeName().contains(ajeno.getName()), "Aparece " + ajeno.getSimpleName());
            }
        }
    }

    private static void agregarFirma(Executable ejecutable, List<Type> tipos) {
        tipos.addAll(Arrays.asList(ejecutable.getGenericParameterTypes()));
        tipos.addAll(Arrays.asList(ejecutable.getGenericExceptionTypes()));
        if (ejecutable instanceof Method metodo) {
            tipos.add(metodo.getGenericReturnType());
        }
    }

    @Test
    @DisplayName("SistemaAsistencia exige un servicio")
    void servicioObligatorio() {
        assertThrows(IllegalArgumentException.class, () -> new SistemaAsistencia(null));
    }
}
