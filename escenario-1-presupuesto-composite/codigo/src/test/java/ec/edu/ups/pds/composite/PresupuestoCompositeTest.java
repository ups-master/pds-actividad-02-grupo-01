package ec.edu.ups.pds.composite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PresupuestoCompositeTest {

    private static final String SALTO = System.lineSeparator();

    private GrupoPresupuestario tecnologias;
    private GrupoPresupuestario infraestructura;

    /** Ejemplo del enunciado: TI -> Licenciamiento, Infraestructura (3 partidas), Servicios. */
    @BeforeEach
    void armarPresupuesto() {
        infraestructura = new GrupoPresupuestario("TI.02", "Infraestructura");
        infraestructura.agregar(partida("TI.02.01", "Servidores", "45000.00"));
        infraestructura.agregar(partida("TI.02.02", "Almacenamiento", "18500.00"));
        infraestructura.agregar(partida("TI.02.03", "Comunicaciones", "12300.50"));

        tecnologias = new GrupoPresupuestario("TI", "Tecnologias de la Informacion");
        tecnologias.agregar(partida("TI.01", "Licenciamiento", "30000.00"));
        tecnologias.agregar(infraestructura);
        tecnologias.agregar(partida("TI.03", "Servicios", "9800.00"));
    }

    private static PartidaPresupuestaria partida(String codigo, String descripcion, String valor) {
        return new PartidaPresupuestaria(codigo, descripcion, new BigDecimal(valor));
    }

    private static void assertValor(String esperado, BigDecimal real) {
        assertEquals(0, new BigDecimal(esperado).compareTo(real), "esperado " + esperado + " pero fue " + real);
    }

    @Test
    @DisplayName("El valor total de una partida es su valor asignado")
    void valorTotalDeUnaPartida() {
        PartidaPresupuestaria licencias = partida("TI.01", "Licenciamiento", "30000.00");

        assertValor("30000.00", licencias.calcularValorTotal());
        assertEquals("TI.01", licencias.getCodigo());
        assertEquals("Licenciamiento", licencias.getDescripcion());
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
    @DisplayName("Se rechazan codigos, descripciones y valores invalidos")
    void rechazoDeValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> partida("P", "Partida", "-0.01"));
        assertThrows(IllegalArgumentException.class, () -> new PartidaPresupuestaria("P", "Partida", null));
        assertThrows(IllegalArgumentException.class, () -> partida(null, "Partida", "1"));
        assertThrows(IllegalArgumentException.class, () -> partida("  ", "Partida", "1"));
        assertThrows(IllegalArgumentException.class, () -> new GrupoPresupuestario("G", ""));
        assertThrows(IllegalArgumentException.class, () -> tecnologias.mostrarEstructura(-1));
        assertValor("0", partida("P", "Partida sin fondos", "0").calcularValorTotal());
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

    @Test
    @DisplayName("ConsultaPresupuesto trata igual a una partida y a un grupo")
    void clienteUniformeConPartidaYGrupo() {
        ConsultaPresupuesto consulta = new ConsultaPresupuesto();
        ElementoPresupuestario[] elementos = {partida("TI.01", "Licenciamiento", "30000"), infraestructura};

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
        ConsultaPresupuesto consulta = new ConsultaPresupuesto();
        String antes = consulta.generarReporte(tecnologias);

        GrupoPresupuestario nube = new GrupoPresupuestario("TI.02.04", "Nube");
        GrupoPresupuestario computo = new GrupoPresupuestario("TI.02.04.01", "Computo");
        computo.agregar(partida("TI.02.04.01.01", "Maquinas virtuales", "5000.00"));
        nube.agregar(computo);
        infraestructura.agregar(nube);
        String despues = consulta.generarReporte(tecnologias);

        assertTrue(antes.endsWith("Total TI: 115600.50"));
        assertTrue(despues.endsWith("Total TI: 120600.50"));
        assertTrue(despues.contains(SALTO + "        TI.02.04.01.01 Maquinas virtuales: 5000.00"));
    }
}
