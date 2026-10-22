package adapter

// BiometricoInstitucional representa los dispositivos del proveedor anterior,
// que ya cumplen el contrato ServicioAsistencia y no necesitan adaptador.
type BiometricoInstitucional struct {
	registros []RegistroAsistencia
}

func NuevoBiometricoInstitucional(registros ...RegistroAsistencia) *BiometricoInstitucional {
	return &BiometricoInstitucional{registros}
}

func (b *BiometricoInstitucional) ConsultarAsistencia(identificacion string, fecha string) RegistroAsistencia {
	for _, r := range b.registros {
		if r.Identificacion == identificacion && r.Fecha == fecha {
			return r
		}
	}
	return RegistroAsistencia{Identificacion: identificacion, Fecha: fecha}
}
