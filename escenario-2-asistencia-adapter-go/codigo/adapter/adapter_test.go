package adapter

import (
	"testing"
	"time"
)

func instante(texto string) int64 {
	t, _ := time.Parse(time.RFC3339, texto)
	return t.UnixMilli()
}

func nuevoSdk() *SdkTerminalHuella {
	return NuevoSdkTerminalHuella(
		MarcacionTerminal{"0607080910", instante("2026-10-21T08:30:00Z")}, // otro dia
		MarcacionTerminal{"0607080910", instante("2026-10-22T08:05:00Z")},
		MarcacionTerminal{"0607080910", instante("2026-10-22T13:00:00Z")},
		MarcacionTerminal{"9999999999", instante("2026-10-22T09:00:00Z")}, // otro usuario
		MarcacionTerminal{"0607080910", instante("2026-10-22T17:10:00Z")},
	)
}

func TestAdapterTraduceMarcacionesARegistro(t *testing.T) {
	adaptador := NuevoTerminalHuellaAdapter(nuevoSdk())
	got := adaptador.ConsultarAsistencia("0607080910", "2026-10-22")
	esperado := RegistroAsistencia{"0607080910", "2026-10-22", "08:05", "17:10"}
	if got != esperado {
		t.Errorf("ConsultarAsistencia = %+v, se esperaba %+v", got, esperado)
	}
}

func TestAdapterSinMarcacionesDejaHorasVacias(t *testing.T) {
	adaptador := NuevoTerminalHuellaAdapter(nuevoSdk())
	got := adaptador.ConsultarAsistencia("0607080910", "2026-10-23")
	esperado := RegistroAsistencia{Identificacion: "0607080910", Fecha: "2026-10-23"}
	if got != esperado {
		t.Errorf("ConsultarAsistencia = %+v, se esperaba %+v", got, esperado)
	}
}

func TestClienteUsaIgualAmbosDispositivos(t *testing.T) {
	anterior := NuevoBiometricoInstitucional(RegistroAsistencia{"0607080910", "2026-10-22", "08:05", "17:10"})
	nuevo := NuevoTerminalHuellaAdapter(nuevoSdk())

	esperado := "0607080910 | 2026-10-22 | ingreso 08:05 | salida 17:10\n"
	for _, servicio := range []ServicioAsistencia{anterior, nuevo} {
		if got := NuevoSistemaAsistencia(servicio).ReporteDiario("0607080910", "2026-10-22"); got != esperado {
			t.Errorf("%T: ReporteDiario = %q, se esperaba %q", servicio, got, esperado)
		}
	}
}
