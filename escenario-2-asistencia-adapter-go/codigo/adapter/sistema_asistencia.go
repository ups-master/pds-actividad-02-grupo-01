package adapter

import "fmt"

// SistemaAsistencia es el Client: trabaja solo con el contrato
// ServicioAsistencia y no sabe que dispositivo hay detras.
type SistemaAsistencia struct {
	servicio ServicioAsistencia
}

func NuevoSistemaAsistencia(servicio ServicioAsistencia) *SistemaAsistencia {
	return &SistemaAsistencia{servicio}
}

func (s *SistemaAsistencia) ReporteDiario(identificacion string, fecha string) string {
	r := s.servicio.ConsultarAsistencia(identificacion, fecha)
	return fmt.Sprintf("%s | %s | ingreso %s | salida %s\n", r.Identificacion, r.Fecha, r.HoraIngreso, r.HoraSalida)
}
