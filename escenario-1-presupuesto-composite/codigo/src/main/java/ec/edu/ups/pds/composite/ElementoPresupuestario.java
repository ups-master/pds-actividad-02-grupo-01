package ec.edu.ups.pds.composite;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Component del patron Composite: abstraccion comun de partidas y grupos presupuestarios.
 *
 * <p>El cliente trabaja siempre con este tipo, sin distinguir si el elemento es una partida
 * individual o un grupo que contiene otros elementos.</p>
 */
public abstract class ElementoPresupuestario {

    private final String codigo;
    private final String descripcion;

    /**
     * @param codigo      codigo del elemento; no nulo ni vacio
     * @param descripcion descripcion del elemento; no nula ni vacia
     * @throws IllegalArgumentException si algun dato es nulo o vacio
     */
    protected ElementoPresupuestario(String codigo, String descripcion) {
        this.codigo = validarTexto(codigo, "codigo");
        this.descripcion = validarTexto(descripcion, "descripcion");
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Valor total del elemento: el valor asignado en una partida, la suma recursiva en un grupo. */
    public abstract BigDecimal calcularValorTotal();

    /**
     * Representacion textual del elemento y, si los tiene, de sus elementos internos.
     *
     * @param nivel profundidad en el arbol; cada nivel agrega dos espacios de sangria
     * @throws IllegalArgumentException si el nivel es negativo
     */
    public abstract String mostrarEstructura(int nivel);

    /** Linea "codigo descripcion: total" con la sangria del nivel indicado. */
    protected String formatearLinea(int nivel) {
        if (nivel < 0) {
            throw new IllegalArgumentException("El nivel no puede ser negativo");
        }
        BigDecimal total = calcularValorTotal().setScale(2, RoundingMode.HALF_UP);
        return "  ".repeat(nivel) + codigo + " " + descripcion + ": " + total.toPlainString();
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " no puede ser nulo ni vacio");
        }
        return valor;
    }
}
