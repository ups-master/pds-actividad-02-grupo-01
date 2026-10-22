package ec.edu.ups.pds.composite;

import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.SALTO;
import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.assertValor;
import static ec.edu.ups.pds.composite.PresupuestoDeEjemplo.partida;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Composite: grupo presupuestario con partidas u otros grupos. */
class GrupoPresupuestarioTest {

    private GrupoPresupuestario tecnologias;
    private GrupoPresupuestario infraestructura;

    @BeforeEach
    void armarPresupuesto() {
        PresupuestoDeEjemplo ejemplo = PresupuestoDeEjemplo.crear();
        tecnologias = ejemplo.tecnologias;
        infraestructura = ejemplo.infraestructura;
    }

    @Test
    @DisplayName("El total de un grupo con partidas es la suma de ellas")
    void grupoConPartidas() {
        assertValor("75800.50", infraestructura.calcularValorTotal());
    }

    @Test
    @DisplayName("El total de grupos anidados se calcula recursivamente en varios niveles")
    void gruposAnidadosAVariosNiveles() {
        GrupoPresupuestario institucion = new GrupoPresupuestario("INS", "Presupuesto institucional");
        GrupoPresupuestario gastos = new GrupoPresupuestario("G", "Gastos");
        gastos.agregar(tecnologias);
        institucion.agregar(gastos);
        institucion.agregar(partida("A", "Administracion", "1000.00"));

        assertValor("115600.50", tecnologias.calcularValorTotal());
        assertValor("115600.50", gastos.calcularValorTotal());
        assertValor("116600.50", institucion.calcularValorTotal());
    }

    @Test
    @DisplayName("Un grupo vacio vale cero")
    void grupoVacio() {
        assertValor("0", new GrupoPresupuestario("V", "Vacio").calcularValorTotal());
    }

    @Test
    @DisplayName("Agregar y eliminar cambian los elementos y el total")
    void agregarYEliminar() {
        PartidaPresupuestaria capacitacion = partida("TI.04", "Capacitacion", "200.00");

        tecnologias.agregar(capacitacion);
        assertEquals(4, tecnologias.getElementos().size());
        assertValor("115800.50", tecnologias.calcularValorTotal());

        tecnologias.eliminar(capacitacion);
        tecnologias.eliminar(infraestructura);
        assertEquals(2, tecnologias.getElementos().size());
        assertValor("39800.00", tecnologias.calcularValorTotal());
        assertThrows(IllegalArgumentException.class, () -> tecnologias.eliminar(capacitacion));
    }

    @Test
    @DisplayName("getElementos devuelve una vista no modificable")
    void elementosNoModificables() {
        List<ElementoPresupuestario> elementos = tecnologias.getElementos();

        assertThrows(UnsupportedOperationException.class, () -> elementos.add(partida("X", "X", "1")));
        assertThrows(UnsupportedOperationException.class, () -> elementos.remove(0));
    }

    @Test
    @DisplayName("Se rechazan los ciclos y los elementos repetidos")
    void rechazoDeCiclos() {
        GrupoPresupuestario servidores = new GrupoPresupuestario("TI.02.04", "Servidores virtuales");
        infraestructura.agregar(servidores);

        assertThrows(IllegalArgumentException.class, () -> tecnologias.agregar(tecnologias));
        assertThrows(IllegalArgumentException.class, () -> infraestructura.agregar(tecnologias));
        assertThrows(IllegalArgumentException.class, () -> servidores.agregar(tecnologias));
        assertThrows(IllegalArgumentException.class, () -> tecnologias.agregar(servidores));
        assertThrows(IllegalArgumentException.class, () -> tecnologias.agregar(null));
        assertValor("115600.50", tecnologias.calcularValorTotal());
    }

    @Test
    @DisplayName("Se rechazan codigo y descripcion vacios y niveles negativos")
    void rechazoDeValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new GrupoPresupuestario("G", ""));
        assertThrows(IllegalArgumentException.class, () -> new GrupoPresupuestario(null, "Grupo"));
        assertThrows(IllegalArgumentException.class, () -> tecnologias.mostrarEstructura(-1));
    }

    @Test
    @DisplayName("mostrarEstructura usa dos espacios de sangria por nivel")
    void estructuraConSangria() {
        String esperado = String.join(SALTO,
                "TI Tecnologias de la Informacion: 115600.50",
                "  TI.01 Licenciamiento: 30000.00",
                "  TI.02 Infraestructura: 75800.50",
                "    TI.02.01 Servidores: 45000.00",
                "    TI.02.02 Almacenamiento: 18500.00",
                "    TI.02.03 Comunicaciones: 12300.50",
                "  TI.03 Servicios: 9800.00");

        assertEquals(esperado, tecnologias.mostrarEstructura(0));
    }
}
