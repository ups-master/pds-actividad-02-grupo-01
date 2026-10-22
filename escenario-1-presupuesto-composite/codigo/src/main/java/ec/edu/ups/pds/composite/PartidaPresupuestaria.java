package ec.edu.ups.pds.composite;

import java.math.BigDecimal;

/**
 * Leaf del patron Composite: partida presupuestaria individual, sin elementos internos.
 */
public class PartidaPresupuestaria extends ElementoPresupuestario {

    private final BigDecimal valorAsignado;

    /**
     * @param valorAsignado valor de la partida; no nulo ni negativo
     * @throws IllegalArgumentException si algun dato no es valido
     */
    public PartidaPresupuestaria(String codigo, String descripcion, BigDecimal valorAsignado) {
        super(codigo, descripcion);
        if (valorAsignado == null) {
            throw new IllegalArgumentException("El valor asignado no puede ser nulo");
        }
        if (valorAsignado.signum() < 0) {
            throw new IllegalArgumentException("El valor asignado no puede ser negativo");
        }
        this.valorAsignado = valorAsignado;
    }

    public BigDecimal getValorAsignado() {
        return valorAsignado;
    }

    /** En una partida el valor total es directamente el valor asignado. */
    @Override
    public BigDecimal calcularValorTotal() {
        return valorAsignado;
    }

    @Override
    public String mostrarEstructura(int nivel) {
        return formatearLinea(nivel);
    }
}
