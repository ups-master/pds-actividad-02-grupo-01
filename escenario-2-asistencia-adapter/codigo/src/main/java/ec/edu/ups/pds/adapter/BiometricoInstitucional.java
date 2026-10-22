package ec.edu.ups.pds.adapter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * SIMULACION del proveedor biometrico anterior, que ya cumple el contrato institucional.
 * Guarda los registros en memoria.
 */
public class BiometricoInstitucional implements ServicioAsistencia {

    private final Map<String, RegistroAsistencia> registros = new HashMap<>();

    public BiometricoInstitucional() {
    }

    /** Carga un registro en el dispositivo simulado; reemplaza el del mismo colaborador y fecha. */
    public void registrar(RegistroAsistencia registro) {
        registros.put(clave(registro.getIdentificacion(), registro.getFecha()), registro);
    }

    /** Devuelve el registro cargado o, si no existe, un registro sin horas. */
    @Override
    public RegistroAsistencia consultarAsistencia(String identificacion, LocalDate fecha) {
        RegistroAsistencia registro = registros.get(clave(identificacion, fecha));
        return registro != null ? registro : new RegistroAsistencia(identificacion, fecha, null, null);
    }

    private static String clave(String identificacion, LocalDate fecha) {
        return identificacion + "|" + fecha;
    }
}
