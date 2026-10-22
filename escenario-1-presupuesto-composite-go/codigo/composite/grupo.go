package composite

import (
	"fmt"
	"strings"
)

// GrupoPresupuestario es el Composite: contiene partidas u otros grupos y
// delega en ellos las operaciones comunes.
type GrupoPresupuestario struct {
	datosElemento
	elementos []ElementoPresupuestario
}

func NuevoGrupo(codigo string, descripcion string) *GrupoPresupuestario {
	return &GrupoPresupuestario{datosElemento: datosElemento{codigo, descripcion}}
}

func (g *GrupoPresupuestario) Agregar(e ElementoPresupuestario) {
	g.elementos = append(g.elementos, e)
}

func (g *GrupoPresupuestario) Eliminar(e ElementoPresupuestario) {
	for i, actual := range g.elementos {
		if actual == e {
			g.elementos = append(g.elementos[:i], g.elementos[i+1:]...)
			return
		}
	}
}

// CalcularValorTotal suma el valor de cada elemento, sea partida o grupo.
func (g *GrupoPresupuestario) CalcularValorTotal() float64 {
	total := 0.0
	for _, e := range g.elementos {
		total += e.CalcularValorTotal()
	}
	return total
}

// MostrarEstructura muestra el grupo y debajo cada elemento con un nivel mas.
func (g *GrupoPresupuestario) MostrarEstructura(nivel int) string {
	s := fmt.Sprintf("%s[%s] %s: %.2f\n", strings.Repeat("  ", nivel), g.codigo, g.descripcion, g.CalcularValorTotal())
	for _, e := range g.elementos {
		s += e.MostrarEstructura(nivel + 1)
	}
	return s
}
