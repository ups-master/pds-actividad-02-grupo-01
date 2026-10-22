package adapter

import "time"

// TerminalHuellaAdapter es el Adapter: cumple el contrato ServicioAsistencia
// y traduce cada consulta a la biblioteca del fabricante, que guarda dentro
// (adaptador de objetos: composicion, no herencia).
type TerminalHuellaAdapter struct {
	sdk *SdkTerminalHuella
}

func NuevoTerminalHuellaAdapter(sdk *SdkTerminalHuella) *TerminalHuellaAdapter {
	return &TerminalHuellaAdapter{sdk}
}

// ConsultarAsistencia convierte la fecha en un rango de milisegundos, pide las
// marcaciones al SDK y toma la primera como ingreso y la ultima como salida.
// Para simplificar, la fecha se asume valida y las horas se expresan en UTC.
func (a *TerminalHuellaAdapter) ConsultarAsistencia(identificacion string, fecha string) RegistroAsistencia {
	inicioDia, _ := time.Parse("2006-01-02", fecha)
	desde := inicioDia.UnixMilli()
	hasta := inicioDia.Add(24*time.Hour).UnixMilli() - 1

	marcaciones := a.sdk.RecuperarMarcaciones(identificacion, desde, hasta)

	registro := RegistroAsistencia{Identificacion: identificacion, Fecha: fecha}
	if len(marcaciones) > 0 {
		primera := marcaciones[0].InstanteEpochMs
		ultima := marcaciones[len(marcaciones)-1].InstanteEpochMs
		registro.HoraIngreso = time.UnixMilli(primera).UTC().Format("15:04")
		registro.HoraSalida = time.UnixMilli(ultima).UTC().Format("15:04")
	}
	return registro
}
