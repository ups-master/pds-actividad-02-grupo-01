package ec.edu.ups.pds.composite;

/**
 * Client del patron Composite: genera reportes de cualquier elemento presupuestario.
 *
 * <p>Solo conoce {@link ElementoPresupuestario}; no pregunta si recibe una partida o un grupo.</p>
 */
public class ConsultaPresupuesto {

    /** Estructura del elemento seguida de una linea con su valor total. */
    public String generarReporte(ElementoPresupuestario elemento) {
        return elemento.mostrarEstructura(0) + System.lineSeparator()
                + "Total " + elemento.getCodigo() + ": " + elemento.calcularValorTotal().toPlainString();
    }
}
