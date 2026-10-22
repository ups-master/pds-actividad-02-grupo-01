package ec.edu.ups.pds.adapter;

/**
 * SIMULACION de la excepcion propia de la biblioteca del fabricante. No debe llegar al
 * sistema institucional: el adaptador la traduce a {@link ConsultaAsistenciaException}.
 */
public class ErrorTerminalHuella extends Exception {

    public ErrorTerminalHuella(String mensaje) {
        super(mensaje);
    }
}
