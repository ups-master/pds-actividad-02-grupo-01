package ec.edu.ups.pds.adapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Usa el proveedor anterior y el nuevo terminal de huella a traves del mismo contrato.
 * Los datos son simulados.
 */
public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        ZoneId zona = ZoneId.of("America/Guayaquil");
        LocalDate fecha = LocalDate.of(2026, 10, 22);

        BiometricoInstitucional anterior = new BiometricoInstitucional();
        anterior.registrar(new RegistroAsistencia("0102030405", fecha, LocalTime.of(8, 2), LocalTime.of(17, 5)));

        SdkTerminalHuella sdk = new SdkTerminalHuella();
        // Marcaciones cargadas desordenadas, como las podria devolver el terminal
        sdk.cargarMarcacion(new MarcacionTerminal("0102030405", epochMs(fecha, 17, 5, zona), "T-01"));
        sdk.cargarMarcacion(new MarcacionTerminal("0102030405", epochMs(fecha, 8, 2, zona), "T-01"));
        sdk.cargarMarcacion(new MarcacionTerminal("0102030405", epochMs(fecha, 13, 0, zona), "T-02"));
        sdk.cargarMarcacion(new MarcacionTerminal("0999999999", epochMs(fecha, 9, 30, zona), "T-02"));

        ServicioAsistencia[] proveedores = {anterior, new TerminalHuellaAdapter(sdk, zona)};
        for (ServicioAsistencia proveedor : proveedores) {
            SistemaAsistencia sistema = new SistemaAsistencia(proveedor);
            System.out.println(proveedor.getClass().getSimpleName());
            System.out.println("  " + sistema.generarReporte("0102030405", fecha));
            System.out.println("  " + sistema.generarReporte("0999999999", fecha));
            System.out.println("  " + sistema.generarReporte("0102030405", fecha.plusDays(1)));
        }
    }

    private static long epochMs(LocalDate fecha, int hora, int minuto, ZoneId zona) {
        return fecha.atTime(hora, minuto).atZone(zona).toInstant().toEpochMilli();
    }
}
