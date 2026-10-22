package ec.edu.ups.pds.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TerminalHuellaAdapterTest {

    private static final ZoneId GUAYAQUIL = ZoneId.of("America/Guayaquil");
    private static final LocalDate FECHA = LocalDate.of(2026, 10, 22);
    private static final String COLABORADOR = "0102030405";

    private SdkTerminalHuella sdk;
    private TerminalHuellaAdapter adaptador;

    @BeforeEach
    void crearAdaptador() {
        sdk = new SdkTerminalHuella();
        adaptador = new TerminalHuellaAdapter(sdk, GUAYAQUIL);
    }

    private void marcar(LocalDate fecha, int hora, int minuto) {
        long instante = fecha.atTime(hora, minuto).atZone(GUAYAQUIL).toInstant().toEpochMilli();
        sdk.cargarMarcacion(new MarcacionTerminal(COLABORADOR, instante, "T-01"));
    }

    @Test
    @DisplayName("Varias marcaciones desordenadas: primera = ingreso, ultima = salida")
    void variasMarcacionesDesordenadas() {
        marcar(FECHA, 12, 30);
        marcar(FECHA, 17, 5);
        marcar(FECHA, 8, 2);
        marcar(FECHA, 13, 15);

        RegistroAsistencia registro = adaptador.consultarAsistencia(COLABORADOR, FECHA);

        assertEquals(COLABORADOR, registro.getIdentificacion());
        assertEquals(FECHA, registro.getFecha());
        assertEquals(Optional.of(LocalTime.of(8, 2)), registro.getHoraIngreso());
        assertEquals(Optional.of(LocalTime.of(17, 5)), registro.getHoraSalida());
    }

    @Test
    @DisplayName("Una sola marcacion: ingreso con salida vacia")
    void unaSolaMarcacion() {
        marcar(FECHA, 8, 2);

        RegistroAsistencia registro = adaptador.consultarAsistencia(COLABORADOR, FECHA);

        assertEquals(Optional.of(LocalTime.of(8, 2)), registro.getHoraIngreso());
        assertEquals(Optional.empty(), registro.getHoraSalida());
    }

    @Test
    @DisplayName("Sin marcaciones: registro sin horas")
    void sinMarcaciones() {
        marcar(FECHA.minusDays(1), 8, 0);
        sdk.cargarMarcacion(new MarcacionTerminal("0999999999",
                FECHA.atTime(9, 0).atZone(GUAYAQUIL).toInstant().toEpochMilli(), "T-01"));

        RegistroAsistencia registro = adaptador.consultarAsistencia(COLABORADOR, FECHA);

        assertEquals(COLABORADOR, registro.getIdentificacion());
        assertEquals(FECHA, registro.getFecha());
        assertTrue(registro.getHoraIngreso().isEmpty());
        assertTrue(registro.getHoraSalida().isEmpty());
    }

    @Test
    @DisplayName("Borde de medianoche: el dia es [00:00, 24:00) en la zona del adaptador")
    void bordeDeZonaHoraria() {
        marcar(FECHA, 0, 0);
        marcar(FECHA, 23, 59);
        marcar(FECHA.plusDays(1), 0, 0);

        RegistroAsistencia delDia = adaptador.consultarAsistencia(COLABORADOR, FECHA);
        RegistroAsistencia siguiente = adaptador.consultarAsistencia(COLABORADOR, FECHA.plusDays(1));

        assertEquals(Optional.of(LocalTime.MIDNIGHT), delDia.getHoraIngreso());
        assertEquals(Optional.of(LocalTime.of(23, 59)), delDia.getHoraSalida());
        assertEquals(Optional.of(LocalTime.MIDNIGHT), siguiente.getHoraIngreso());
        assertEquals(Optional.empty(), siguiente.getHoraSalida());
    }

    @Test
    @DisplayName("La misma marcacion cae en otro dia si cambia la zona horaria")
    void laZonaHorariaDefineElDia() {
        marcar(FECHA, 20, 0); // 2026-10-23T01:00Z

        TerminalHuellaAdapter enUtc = new TerminalHuellaAdapter(sdk, ZoneOffset.UTC);

        assertEquals(Optional.of(LocalTime.of(20, 0)), adaptador.consultarAsistencia(COLABORADOR, FECHA).getHoraIngreso());
        assertTrue(enUtc.consultarAsistencia(COLABORADOR, FECHA).getHoraIngreso().isEmpty());
        assertEquals(Optional.of(LocalTime.of(1, 0)),
                enUtc.consultarAsistencia(COLABORADOR, FECHA.plusDays(1)).getHoraIngreso());
    }

    @Test
    @DisplayName("El error del SDK se traduce a ConsultaAsistenciaException conservando la causa")
    void errorDelSdkTraducido() {
        ErrorTerminalHuella original = new ErrorTerminalHuella("ERR-99 terminal desconectado");
        SdkTerminalHuella sdkFallido = new SdkTerminalHuella() {
            @Override
            public List<MarcacionTerminal> recuperarMarcaciones(String codigo, long desde, long hasta)
                    throws ErrorTerminalHuella {
                throw original;
            }
        };
        ServicioAsistencia servicio = new TerminalHuellaAdapter(sdkFallido, GUAYAQUIL);

        ConsultaAsistenciaException error = assertThrows(ConsultaAsistenciaException.class,
                () -> servicio.consultarAsistencia(COLABORADOR, FECHA));

        assertSame(original, error.getCause());
        assertTrue(error.getMessage().contains("ERR-99"));
    }

    @Test
    @DisplayName("Un fallo inesperado del SDK tambien se traduce")
    void falloInesperadoDelSdkTraducido() {
        IllegalStateException original = new IllegalStateException("puerto cerrado");
        SdkTerminalHuella sdkFallido = new SdkTerminalHuella() {
            @Override
            public List<MarcacionTerminal> recuperarMarcaciones(String codigo, long desde, long hasta) {
                throw original;
            }
        };

        ConsultaAsistenciaException error = assertThrows(ConsultaAsistenciaException.class,
                () -> new TerminalHuellaAdapter(sdkFallido, GUAYAQUIL).consultarAsistencia(COLABORADOR, FECHA));

        assertSame(original, error.getCause());
    }

    @Test
    @DisplayName("Se rechazan argumentos invalidos sin llamar al SDK")
    void argumentosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> adaptador.consultarAsistencia(" ", FECHA));
        assertThrows(IllegalArgumentException.class, () -> adaptador.consultarAsistencia(COLABORADOR, null));
        assertThrows(IllegalArgumentException.class, () -> new TerminalHuellaAdapter(null, GUAYAQUIL));
        assertThrows(IllegalArgumentException.class, () -> new TerminalHuellaAdapter(sdk, null));
    }

    @Test
    @DisplayName("RegistroAsistencia valida la coherencia de las horas")
    void registroCoherente() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAsistencia(COLABORADOR, FECHA, null, LocalTime.NOON));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAsistencia(COLABORADOR, FECHA, LocalTime.NOON, LocalTime.of(8, 0)));
        assertThrows(IllegalArgumentException.class, () -> new RegistroAsistencia("", FECHA, null, null));
        assertThrows(IllegalArgumentException.class, () -> new RegistroAsistencia(COLABORADOR, null, null, null));
    }
}
