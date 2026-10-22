package ec.edu.ups.pds.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Client del patron Adapter: sistema institucional de asistencia.
 *
 * <p>Depende solo de {@link ServicioAsistencia}; no conoce el SDK ni los tipos del fabricante.</p>
 */
public class SistemaAsistencia {

    private final ServicioAsistencia servicio;

    public SistemaAsistencia(ServicioAsistencia servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio de asistencia no puede ser nulo");
        }
        this.servicio = servicio;
    }

    /**
     * Reporte de una linea: identificacion, fecha, ingreso y salida.
     *
     * @throws ConsultaAsistenciaException si el proveedor no puede responder
     */
    public String generarReporte(String identificacion, LocalDate fecha) {
        RegistroAsistencia registro = servicio.consultarAsistencia(identificacion, fecha);
        return "Colaborador " + registro.getIdentificacion()
                + " | " + registro.getFecha()
                + " | ingreso " + formatear(registro.getHoraIngreso())
                + " | salida " + formatear(registro.getHoraSalida());
    }

    private static String formatear(Optional<LocalTime> hora) {
        return hora.map(h -> h.format(DateTimeFormatter.ofPattern("HH:mm"))).orElse("sin registro");
    }
}
