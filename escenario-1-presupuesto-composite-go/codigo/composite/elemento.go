// Package composite implementa el presupuesto institucional del escenario 1
// con el patron Composite, usando interfaces y composicion en lugar de herencia.
package composite

// ElementoPresupuestario es el Component: la abstraccion comun que permite
// al cliente tratar igual a partidas y grupos.
type ElementoPresupuestario interface {
	Codigo() string
	Descripcion() string
	CalcularValorTotal() float64
	MostrarEstructura(nivel int) string
}

// datosElemento guarda los datos comunes de partidas y grupos.
// Se embebe en ambos (composicion) en vez de usar una clase base.
type datosElemento struct {
	codigo      string
	descripcion string
}

func (d datosElemento) Codigo() string      { return d.codigo }
func (d datosElemento) Descripcion() string { return d.descripcion }
