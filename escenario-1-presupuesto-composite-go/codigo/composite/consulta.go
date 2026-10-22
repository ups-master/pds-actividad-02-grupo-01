package composite

import "fmt"

// ConsultaPresupuesto es el Client: trabaja solo con la interfaz
// ElementoPresupuestario, sin preguntar si recibe una partida o un grupo.
type ConsultaPresupuesto struct{}

func (ConsultaPresupuesto) GenerarReporte(e ElementoPresupuestario) string {
	return e.MostrarEstructura(0) + fmt.Sprintf("Total: %.2f\n", e.CalcularValorTotal())
}
