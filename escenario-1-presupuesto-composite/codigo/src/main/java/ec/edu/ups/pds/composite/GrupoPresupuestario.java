package ec.edu.ups.pds.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite del patron: grupo presupuestario que contiene partidas u otros grupos.
 *
 * <p>Variante segura: {@link #agregar} y {@link #eliminar} existen solo aqui, porque una
 * partida no tiene hijos. La relacion con sus elementos es de composicion: cada elemento
 * pertenece a un unico grupo y forma parte del presupuesto solo a traves de el.</p>
 */
public class GrupoPresupuestario extends ElementoPresupuestario {

    private final List<ElementoPresupuestario> elementos = new ArrayList<>();

    public GrupoPresupuestario(String codigo, String descripcion) {
        super(codigo, descripcion);
    }

    /** Suma recursiva del valor total de cada elemento; un grupo vacio vale cero. */
    @Override
    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ElementoPresupuestario elemento : elementos) {
            total = total.add(elemento.calcularValorTotal());
        }
        return total;
    }

    /** Linea del grupo seguida de la estructura de cada elemento en el nivel siguiente. */
    @Override
    public String mostrarEstructura(int nivel) {
        StringBuilder estructura = new StringBuilder(formatearLinea(nivel));
        for (ElementoPresupuestario elemento : elementos) {
            estructura.append(System.lineSeparator()).append(elemento.mostrarEstructura(nivel + 1));
        }
        return estructura.toString();
    }

    /**
     * Agrega un elemento al grupo.
     *
     * @throws IllegalArgumentException si el elemento es nulo, es este mismo grupo, ya esta en
     *                                  el arbol de este grupo o es un grupo que contiene a este
     *                                  (lo que formaria un ciclo)
     */
    public void agregar(ElementoPresupuestario elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser nulo");
        }
        if (elemento == this) {
            throw new IllegalArgumentException("Un grupo no puede agregarse a si mismo");
        }
        if (contieneDescendiente(elemento)) {
            throw new IllegalArgumentException("El elemento ya forma parte del grupo");
        }
        if (elemento instanceof GrupoPresupuestario grupo && grupo.contieneDescendiente(this)) {
            throw new IllegalArgumentException("No se puede agregar un grupo que contiene a este grupo");
        }
        elementos.add(elemento);
    }

    /**
     * Quita un elemento que pertenece directamente al grupo.
     *
     * @throws IllegalArgumentException si el elemento no es un hijo directo del grupo
     */
    public void eliminar(ElementoPresupuestario elemento) {
        if (!elementos.remove(elemento)) {
            throw new IllegalArgumentException("El elemento no pertenece al grupo");
        }
    }

    /** Vista no modificable de los elementos directos del grupo. */
    public List<ElementoPresupuestario> getElementos() {
        return Collections.unmodifiableList(elementos);
    }

    private boolean contieneDescendiente(ElementoPresupuestario elemento) {
        for (ElementoPresupuestario hijo : elementos) {
            if (hijo == elemento
                    || hijo instanceof GrupoPresupuestario grupo && grupo.contieneDescendiente(elemento)) {
                return true;
            }
        }
        return false;
    }
}
