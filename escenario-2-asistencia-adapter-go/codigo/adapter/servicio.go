// Package adapter implementa la asistencia de colaboradores del escenario 2
// con el patron Adapter (adaptador de objetos, por composicion).
package adapter

// ServicioAsistencia es el Target: el contrato comun que ya usa el sistema
// institucional. La fecha se recibe en formato AAAA-MM-DD.
type ServicioAsistencia interface {
	ConsultarAsistencia(identificacion string, fecha string) RegistroAsistencia
}

// RegistroAsistencia es la informacion normalizada que espera el sistema.
// Las horas van en formato HH:MM y quedan vacias si no hubo marcaciones.
type RegistroAsistencia struct {
	Identificacion string
	Fecha          string
	HoraIngreso    string
	HoraSalida     string
}
