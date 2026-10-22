package ec.edu.ups.pds.adapter;

import java.time.LocalDate;

/**
 * Target del patron Adapter: contrato que ya usa el sistema institucional para consultar
 * la asistencia diaria de un colaborador.
 */
public interface ServicioAsistencia {

    /**
     * @param identificacion identificacion del colaborador; no nula ni vacia
     * @param fecha          dia que se consulta; no nula
     * @return registro normalizado; si no hay marcaciones, un registro sin horas
     * @throws ConsultaAsistenciaException si el proveedor no puede responder la consulta
     */
    RegistroAsistencia consultarAsistencia(String identificacion, LocalDate fecha)
            throws ConsultaAsistenciaException;
}
