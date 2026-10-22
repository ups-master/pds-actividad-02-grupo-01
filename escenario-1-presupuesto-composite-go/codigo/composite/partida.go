package composite

import (
	"fmt"
	"strings"
)

// PartidaPresupuestaria es el Leaf: un elemento individual sin hijos.
type PartidaPresupuestaria struct {
	datosElemento
	valorAsignado float64
}

func NuevaPartida(codigo string, descripcion string, valorAsignado float64) *PartidaPresupuestaria {
	return &PartidaPresupuestaria{datosElemento{codigo, descripcion}, valorAsignado}
}

// CalcularValorTotal de una partida es su valor asignado.
func (p *PartidaPresupuestaria) CalcularValorTotal() float64 {
	return p.valorAsignado
}

func (p *PartidaPresupuestaria) MostrarEstructura(nivel int) string {
	return fmt.Sprintf("%s[%s] %s: %.2f\n", strings.Repeat("  ", nivel), p.codigo, p.descripcion, p.CalcularValorTotal())
}
