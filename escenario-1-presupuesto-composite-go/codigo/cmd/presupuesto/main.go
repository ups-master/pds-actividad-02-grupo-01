// Programa de demostracion: arma el ejemplo del enunciado e imprime el reporte.
package main

import (
	"fmt"

	"presupuesto-composite/composite"
)

func main() {
	infra := composite.NuevoGrupo("TI-INF", "Infraestructura")
	infra.Agregar(composite.NuevaPartida("TI-INF-01", "Servidores", 45000))
	infra.Agregar(composite.NuevaPartida("TI-INF-02", "Almacenamiento", 18000))
	infra.Agregar(composite.NuevaPartida("TI-INF-03", "Comunicaciones", 9500))

	ti := composite.NuevoGrupo("TI", "Tecnologias de la Informacion")
	ti.Agregar(composite.NuevaPartida("TI-01", "Licenciamiento", 20000))
	ti.Agregar(infra)
	ti.Agregar(composite.NuevaPartida("TI-02", "Servicios", 12500))

	fmt.Print(composite.ConsultaPresupuesto{}.GenerarReporte(ti))
}
