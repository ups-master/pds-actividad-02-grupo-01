package ec.edu.ups.pds.adapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Adapter de objetos: implementa {@link ServicioAsistencia} y delega en {@link SdkTerminalHuella},
 * que recibe por composicion (no hereda de el).
 *
 * <p>Reglas de transformacion:</p>
 * <ol>
 *   <li>La fecha se convierte al rango [00:00, 24:00) de {@code zonaHoraria} en milisegundos:
 *       desde {@code fecha.atStartOfDay(zona)} hasta el inicio del dia siguiente (cubre cambios
 *       de horario).</li>
 *   <li>Las marcaciones se ordenan por instante.</li>
 *   <li>La primera marcacion es la hora de ingreso y la ultima la hora de salida.</li>
 *   <li>Sin marcaciones se devuelve un registro sin horas.</li>
 *   <li>Con una sola marcacion se devuelve el ingreso y la salida queda vacia.</li>
 *   <li>Los errores del SDK se traducen a {@link ConsultaAsistenciaException} conservando la
 *       causa; ningun tipo del fabricante llega al cliente.</li>
 * </ol>
 */
public class TerminalHuellaAdapter implements ServicioAsistencia {

    private final SdkTerminalHuella adaptado;
    private final ZoneId zonaHoraria;

    /**
     * @param adaptado    biblioteca del fabricante que se adapta; no nula
     * @param zonaHoraria zona en la que se interpreta la fecha consultada; no nula
     */
    public TerminalHuellaAdapter(SdkTerminalHuella adaptado, ZoneId zonaHoraria) {
        if (adaptado == null) {
            throw new IllegalArgumentException("El SDK del terminal no puede ser nulo");
        }
        if (zonaHoraria == null) {
            throw new IllegalArgumentException("La zona horaria no puede ser nula");
        }
        this.adaptado = adaptado;
        this.zonaHoraria = zonaHoraria;
    }

    @Override
    public RegistroAsistencia consultarAsistencia(String identificacion, LocalDate fecha)
            throws ConsultaAsistenciaException {
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificacion no puede ser nula ni vacia");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        long desde = fecha.atStartOfDay(zonaHoraria).toInstant().toEpochMilli();
        long hasta = fecha.plusDays(1).atStartOfDay(zonaHoraria).toInstant().toEpochMilli();
        List<MarcacionTerminal> marcaciones;
        try {
            marcaciones = adaptado.recuperarMarcaciones(identificacion, desde, hasta);
        } catch (ErrorTerminalHuella error) {
            throw traducir(error);
        } catch (RuntimeException error) {
            throw new ConsultaAsistenciaException("Fallo inesperado del SDK del terminal de huella", error);
        }
        return convertir(identificacion, fecha, marcaciones);
    }

    private RegistroAsistencia convertir(String identificacion, LocalDate fecha, List<MarcacionTerminal> marcaciones) {
        if (marcaciones == null || marcaciones.isEmpty()) {
            return new RegistroAsistencia(identificacion, fecha, null, null);
        }
        List<MarcacionTerminal> ordenadas = new ArrayList<>(marcaciones);
        ordenadas.sort(Comparator.comparingLong(MarcacionTerminal::getInstanteEpochMs));
        LocalTime ingreso = Instant.ofEpochMilli(ordenadas.getFirst().getInstanteEpochMs())
                .atZone(zonaHoraria).toLocalTime();
        LocalTime salida = ordenadas.size() > 1
                ? Instant.ofEpochMilli(ordenadas.getLast().getInstanteEpochMs()).atZone(zonaHoraria).toLocalTime()
                : null;
        return new RegistroAsistencia(identificacion, fecha, ingreso, salida);
    }

    private ConsultaAsistenciaException traducir(ErrorTerminalHuella error) {
        return new ConsultaAsistenciaException(
                "No se pudo consultar el terminal de huella: " + error.getMessage(), error);
    }
}
