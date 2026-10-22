package ec.edu.ups.pds.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Datos normalizados de asistencia que espera el sistema institucional.
 *
 * <p>La hora de ingreso y la de salida pueden faltar: el constructor acepta {@code null} y los
 * getters devuelven {@link Optional#empty()}. No puede haber salida sin ingreso, y la salida
 * no puede ser anterior al ingreso.</p>
 */
public class RegistroAsistencia {

    private final String identificacion;
    private final LocalDate fecha;
    private final LocalTime horaIngreso;
    private final LocalTime horaSalida;

    /**
     * @param horaIngreso hora de ingreso o {@code null} si no hubo marcaciones
     * @param horaSalida  hora de salida o {@code null} si no se registro
     * @throws IllegalArgumentException si los datos no son coherentes
     */
    public RegistroAsistencia(String identificacion, LocalDate fecha, LocalTime horaIngreso, LocalTime horaSalida) {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificacion no puede ser nula ni vacia");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (horaSalida != null && horaIngreso == null) {
            throw new IllegalArgumentException("No puede haber hora de salida sin hora de ingreso");
        }
        if (horaSalida != null && horaSalida.isBefore(horaIngreso)) {
            throw new IllegalArgumentException("La hora de salida no puede ser anterior a la de ingreso");
        }
        this.identificacion = identificacion;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Optional<LocalTime> getHoraIngreso() {
        return Optional.ofNullable(horaIngreso);
    }

    public Optional<LocalTime> getHoraSalida() {
        return Optional.ofNullable(horaSalida);
    }
}
