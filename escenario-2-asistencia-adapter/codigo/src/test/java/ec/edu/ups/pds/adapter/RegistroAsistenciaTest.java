package ec.edu.ups.pds.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Datos normalizados del Target: horas opcionales y coherentes. */
class RegistroAsistenciaTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 10, 22);
    private static final String COLABORADOR = "0102030405";

    @Test
    @DisplayName("Las horas que faltan se exponen como Optional vacio")
    void horasOpcionales() {
        RegistroAsistencia completo = new RegistroAsistencia(COLABORADOR, FECHA, LocalTime.of(8, 0), LocalTime.of(17, 0));
        RegistroAsistencia sinSalida = new RegistroAsistencia(COLABORADOR, FECHA, LocalTime.of(8, 0), null);
        RegistroAsistencia sinHoras = new RegistroAsistencia(COLABORADOR, FECHA, null, null);

        assertEquals(COLABORADOR, completo.getIdentificacion());
        assertEquals(FECHA, completo.getFecha());
        assertEquals(Optional.of(LocalTime.of(17, 0)), completo.getHoraSalida());
        assertEquals(Optional.of(LocalTime.of(8, 0)), sinSalida.getHoraIngreso());
        assertEquals(Optional.empty(), sinSalida.getHoraSalida());
        assertEquals(Optional.empty(), sinHoras.getHoraIngreso());
        assertEquals(Optional.empty(), sinHoras.getHoraSalida());
    }

    @Test
    @DisplayName("RegistroAsistencia valida la coherencia de los datos")
    void registroCoherente() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAsistencia(COLABORADOR, FECHA, null, LocalTime.NOON));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroAsistencia(COLABORADOR, FECHA, LocalTime.NOON, LocalTime.of(8, 0)));
        assertThrows(IllegalArgumentException.class, () -> new RegistroAsistencia("", FECHA, null, null));
        assertThrows(IllegalArgumentException.class, () -> new RegistroAsistencia(COLABORADOR, null, null, null));
    }
}
