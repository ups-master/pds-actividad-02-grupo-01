package ec.edu.ups.pds.composite;

import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.SALTO;
import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.partida;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Client: la consulta trata igual a partidas y grupos. */
class ConsultaPresupuestoTest {

    private final ConsultaPresupuesto consulta = new ConsultaPresupuesto();

    @Test
    @DisplayName("ConsultaPresupuesto trata igual a una partida y a un grupo")
    void clienteUniformeConPartidaYGrupo() {
        ElementoPresupuestario[] elementos = {
                partida("TI.01", "Licenciamiento", "30000"), PresupuestoDeEjemplo.crear().infraestructura};

        for (ElementoPresupuestario elemento : elementos) {
            String reporte = consulta.generarReporte(elemento);
            assertTrue(reporte.startsWith(elemento.mostrarEstructura(0) + SALTO));
            assertTrue(reporte.endsWith("Total " + elemento.getCodigo() + ": "
                    + elemento.calcularValorTotal().toPlainString()));
        }
    }

    @Test
    @DisplayName("Un nuevo nivel de agrupacion no requiere cambiar el cliente")
    void nuevoNivelSinModificarElCliente() {
        PresupuestoDeEjemplo ejemplo = PresupuestoDeEjemplo.crear();
        String antes = consulta.generarReporte(ejemplo.tecnologias);

        GrupoPresupuestario nube = new GrupoPresupuestario("TI.02.04", "Nube");
        GrupoPresupuestario computo = new GrupoPresupuestario("TI.02.04.01", "Computo");
        computo.agregar(partida("TI.02.04.01.01", "Maquinas virtuales", "5000.00"));
        nube.agregar(computo);
        ejemplo.infraestructura.agregar(nube);
        String despues = consulta.generarReporte(ejemplo.tecnologias);

        assertTrue(antes.endsWith("Total TI: 115600.50"));
        assertTrue(despues.endsWith("Total TI: 120600.50"));
        assertTrue(despues.contains(SALTO + "        TI.02.04.01.01 Maquinas virtuales: 5000.00"));
    }
}
