package ec.edu.ups.pds.composite;

import java.math.BigDecimal;

/**
 * Arma el ejemplo del enunciado e imprime su estructura y totales.
 * Los valores son ilustrativos.
 */
public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        GrupoPresupuestario infraestructura = new GrupoPresupuestario("TI.02", "Infraestructura");
        infraestructura.agregar(new PartidaPresupuestaria("TI.02.01", "Servidores", new BigDecimal("45000.00")));
        infraestructura.agregar(new PartidaPresupuestaria("TI.02.02", "Almacenamiento", new BigDecimal("18500.00")));
        infraestructura.agregar(new PartidaPresupuestaria("TI.02.03", "Comunicaciones", new BigDecimal("12300.50")));

        GrupoPresupuestario tecnologias = new GrupoPresupuestario("TI", "Tecnologias de la Informacion");
        tecnologias.agregar(new PartidaPresupuestaria("TI.01", "Licenciamiento", new BigDecimal("30000.00")));
        tecnologias.agregar(infraestructura);
        tecnologias.agregar(new PartidaPresupuestaria("TI.03", "Servicios", new BigDecimal("9800.00")));

        ConsultaPresupuesto consulta = new ConsultaPresupuesto();
        System.out.println(consulta.generarReporte(tecnologias));
        System.out.println();
        System.out.println(consulta.generarReporte(infraestructura));
    }
}
