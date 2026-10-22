package ec.edu.ups.pds.adapter;

/**
 * Excepcion del dominio institucional cuando un proveedor no puede responder una consulta.
 * Conserva como causa el error original para diagnostico, sin que el cliente dependa de su tipo.
 */
public class ConsultaAsistenciaException extends RuntimeException {

    public ConsultaAsistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
