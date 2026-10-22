package ec.edu.ups.pds.composite;

import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.assertValor;
import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.partida;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Leaf: partida presupuestaria individual. */
class PartidaPresupuestariaTest {

    @Test
    @DisplayName("El valor total de una partida es su valor asignado")
    void valorTotalDeUnaPartida() {
        PartidaPresupuestaria licencias = partida("TI.01", "Licenciamiento", "30000.00");

        assertValor("30000.00", licencias.calcularValorTotal());
        assertValor("30000.00", licencias.getValorAsignado());
        assertEquals("TI.01", licencias.getCodigo());
        assertEquals("Licenciamiento", licencias.getDescripcion());
        assertEquals("  TI.01 Licenciamiento: 30000.00", licencias.mostrarEstructura(1));
    }

    @Test
    @DisplayName("Se rechazan codigos, descripciones y valores invalidos; el cero es valido")
    void rechazoDeValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> partida("P", "Partida", "-0.01"));
        assertThrows(IllegalArgumentException.class, () -> new PartidaPresupuestaria("P", "Partida", null));
        assertThrows(IllegalArgumentException.class, () -> partida(null, "Partida", "1"));
        assertThrows(IllegalArgumentException.class, () -> partida("  ", "Partida", "1"));
        assertThrows(IllegalArgumentException.class, () -> partida("P", "", "1"));
        assertThrows(IllegalArgumentException.class, () -> partida("P", "Partida", "1").mostrarEstructura(-1));
        assertValor("0", partida("P", "Partida sin fondos", "0").calcularValorTotal());
    }
}
