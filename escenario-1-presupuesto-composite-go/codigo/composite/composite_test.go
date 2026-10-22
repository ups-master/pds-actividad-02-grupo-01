package composite

import "testing"

// arbolTI arma el ejemplo del enunciado.
func arbolTI() (ti, infra *GrupoPresupuestario) {
	infra = NuevoGrupo("TI-INF", "Infraestructura")
	infra.Agregar(NuevaPartida("TI-INF-01", "Servidores", 45000))
	infra.Agregar(NuevaPartida("TI-INF-02", "Almacenamiento", 18000))
	infra.Agregar(NuevaPartida("TI-INF-03", "Comunicaciones", 9500))

	ti = NuevoGrupo("TI", "Tecnologias de la Informacion")
	ti.Agregar(NuevaPartida("TI-01", "Licenciamiento", 20000))
	ti.Agregar(infra)
	ti.Agregar(NuevaPartida("TI-02", "Servicios", 12500))
	return ti, infra
}

func TestPartidaDevuelveSuValorAsignado(t *testing.T) {
	p := NuevaPartida("P-01", "Licencias", 1500)
	if p.CalcularValorTotal() != 1500 {
		t.Errorf("CalcularValorTotal() = %.2f, se esperaba 1500", p.CalcularValorTotal())
	}
}

func TestGrupoSumaTambienLosGruposInternos(t *testing.T) {
	ti, infra := arbolTI()
	if infra.CalcularValorTotal() != 72500 {
		t.Errorf("Infraestructura = %.2f, se esperaba 72500", infra.CalcularValorTotal())
	}
	if ti.CalcularValorTotal() != 105000 {
		t.Errorf("TI = %.2f, se esperaba 105000", ti.CalcularValorTotal())
	}
}

func TestEliminar(t *testing.T) {
	ti, infra := arbolTI()
	ti.Eliminar(infra)
	if ti.CalcularValorTotal() != 32500 {
		t.Errorf("TI sin Infraestructura = %.2f, se esperaba 32500", ti.CalcularValorTotal())
	}
}

func TestReporte(t *testing.T) {
	ti, _ := arbolTI()
	esperado := "" +
		"[TI] Tecnologias de la Informacion: 105000.00\n" +
		"  [TI-01] Licenciamiento: 20000.00\n" +
		"  [TI-INF] Infraestructura: 72500.00\n" +
		"    [TI-INF-01] Servidores: 45000.00\n" +
		"    [TI-INF-02] Almacenamiento: 18000.00\n" +
		"    [TI-INF-03] Comunicaciones: 9500.00\n" +
		"  [TI-02] Servicios: 12500.00\n" +
		"Total: 105000.00\n"
	if got := (ConsultaPresupuesto{}).GenerarReporte(ti); got != esperado {
		t.Errorf("reporte:\n%s\nse esperaba:\n%s", got, esperado)
	}
}
