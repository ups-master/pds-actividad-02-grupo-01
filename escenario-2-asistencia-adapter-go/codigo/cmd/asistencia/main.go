// Programa de demostracion: el mismo sistema de asistencia consulta el
// biometrico anterior y el nuevo dispositivo a traves del adaptador.
package main

import (
	"fmt"
	"time"

	"asistencia-adapter/adapter"
)

func main() {
	anterior := adapter.NuevoBiometricoInstitucional(
		adapter.RegistroAsistencia{Identificacion: "0102030405", Fecha: "2026-10-22", HoraIngreso: "08:00", HoraSalida: "17:00"},
	)

	// Marcaciones que guarda el SDK del nuevo fabricante: ingreso, almuerzo y salida.
	sdk := adapter.NuevoSdkTerminalHuella(
		adapter.MarcacionTerminal{CodigoUsuario: "0607080910", InstanteEpochMs: instante("2026-10-22T08:05:00Z")},
		adapter.MarcacionTerminal{CodigoUsuario: "0607080910", InstanteEpochMs: instante("2026-10-22T13:00:00Z")},
		adapter.MarcacionTerminal{CodigoUsuario: "0607080910", InstanteEpochMs: instante("2026-10-22T17:10:00Z")},
	)
	nuevo := adapter.NuevoTerminalHuellaAdapter(sdk)

	fmt.Print("Dispositivo anterior: ", adapter.NuevoSistemaAsistencia(anterior).ReporteDiario("0102030405", "2026-10-22"))
	fmt.Print("Dispositivo nuevo:    ", adapter.NuevoSistemaAsistencia(nuevo).ReporteDiario("0607080910", "2026-10-22"))
}

func instante(texto string) int64 {
	t, _ := time.Parse(time.RFC3339, texto)
	return t.UnixMilli()
}
