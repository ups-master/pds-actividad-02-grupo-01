package ec.edu.ups.pds.composite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

/** Datos de prueba compartidos: el ejemplo del enunciado y utilidades de comparacion. */
final class PresupuestoDeEjemplo {

    static final String SALTO = System.lineSeparator();

    final GrupoPresupuestario tecnologias;
    final GrupoPresupuestario infraestructura;

    private PresupuestoDeEjemplo(GrupoPresupuestario tecnologias, GrupoPresupuestario infraestructura) {
        this.tecnologias = tecnologias;
        this.infraestructura = infraestructura;
    }

    /** TI -> Licenciamiento, Infraestructura (Servidores, Almacenamiento, Comunicaciones), Servicios. */
    static PresupuestoDeEjemplo crear() {
        GrupoPresupuestario infraestructura = new GrupoPresupuestario("TI.02", "Infraestructura");
        infraestructura.agregar(partida("TI.02.01", "Servidores", "45000.00"));
        infraestructura.agregar(partida("TI.02.02", "Almacenamiento", "18500.00"));
        infraestructura.agregar(partida("TI.02.03", "Comunicaciones", "12300.50"));

        GrupoPresupuestario tecnologias = new GrupoPresupuestario("TI", "Tecnologias de la Informacion");
        tecnologias.agregar(partida("TI.01", "Licenciamiento", "30000.00"));
        tecnologias.agregar(infraestructura);
        tecnologias.agregar(partida("TI.03", "Servicios", "9800.00"));
        return new PresupuestoDeEjemplo(tecnologias, infraestructura);
    }

    static PartidaPresupuestaria partida(String codigo, String descripcion, String valor) {
        return new PartidaPresupuestaria(codigo, descripcion, new BigDecimal(valor));
    }

    /** Compara montos por valor, sin depender de la escala de BigDecimal. */
    static void assertValor(String esperado, BigDecimal real) {
        assertEquals(0, new BigDecimal(esperado).compareTo(real), "esperado " + esperado + " pero fue " + real);
    }
}
