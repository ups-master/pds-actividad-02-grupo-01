package ec.edu.ups.pds.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptee del patron Adapter: SIMULACION de la biblioteca del nuevo fabricante.
 *
 * <p>Representa codigo de terceros que no se modifica. Su interfaz no coincide con
 * {@link ServicioAsistencia}: recibe un rango en milisegundos y devuelve marcaciones sueltas,
 * en el orden en que se cargaron.</p>
 */
public class SdkTerminalHuella {

    private final List<MarcacionTerminal> marcaciones = new ArrayList<>();

    public SdkTerminalHuella() {
    }

    /** Simula una marcacion leida por el terminal. */
    public void cargarMarcacion(MarcacionTerminal marcacion) {
        marcaciones.add(marcacion);
    }

    /**
     * Marcaciones del usuario con instante en [desdeEpochMs, hastaEpochMs), sin ordenar.
     *
     * @throws ErrorTerminalHuella si el codigo es nulo o el rango no es valido
     */
    public List<MarcacionTerminal> recuperarMarcaciones(String codigoUsuario, long desdeEpochMs, long hastaEpochMs)
            throws ErrorTerminalHuella {
        if (codigoUsuario == null) {
            throw new ErrorTerminalHuella("ERR-01 codigo de usuario nulo");
        }
        if (desdeEpochMs >= hastaEpochMs) {
            throw new ErrorTerminalHuella("ERR-02 rango de consulta invalido");
        }
        List<MarcacionTerminal> resultado = new ArrayList<>();
        for (MarcacionTerminal marcacion : marcaciones) {
            long instante = marcacion.getInstanteEpochMs();
            if (marcacion.getCodigoUsuario().equals(codigoUsuario)
                    && instante >= desdeEpochMs && instante < hastaEpochMs) {
                resultado.add(marcacion);
            }
        }
        return resultado;
    }
}
