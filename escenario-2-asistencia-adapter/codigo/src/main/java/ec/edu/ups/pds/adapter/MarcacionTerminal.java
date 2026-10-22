package ec.edu.ups.pds.adapter;

/**
 * SIMULACION de un tipo propio de la biblioteca del fabricante: una marcacion individual
 * registrada por un terminal de huella. No se modifica.
 */
public class MarcacionTerminal {

    private final String codigoUsuario;
    private final long instanteEpochMs;
    private final String idTerminal;

    public MarcacionTerminal(String codigoUsuario, long instanteEpochMs, String idTerminal) {
        this.codigoUsuario = codigoUsuario;
        this.instanteEpochMs = instanteEpochMs;
        this.idTerminal = idTerminal;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    /** Instante de la marcacion en milisegundos desde 1970-01-01T00:00Z. */
    public long getInstanteEpochMs() {
        return instanteEpochMs;
    }

    public String getIdTerminal() {
        return idTerminal;
    }
}
