package adapter

// SdkTerminalHuella es el Adaptee: simula la biblioteca del nuevo fabricante.
// Tiene sus propias operaciones y tipos de datos, y no se modifica.
type SdkTerminalHuella struct {
	marcaciones []MarcacionTerminal
}

// MarcacionTerminal es el tipo de dato propio del fabricante: cada vez que
// el colaborador pone la huella se guarda un instante en milisegundos.
type MarcacionTerminal struct {
	CodigoUsuario   string
	InstanteEpochMs int64
}

func NuevoSdkTerminalHuella(marcaciones ...MarcacionTerminal) *SdkTerminalHuella {
	return &SdkTerminalHuella{marcaciones}
}

// RecuperarMarcaciones devuelve, en orden, las marcaciones del usuario entre
// dos instantes (incluidos).
func (s *SdkTerminalHuella) RecuperarMarcaciones(codigoUsuario string, desdeEpochMs int64, hastaEpochMs int64) []MarcacionTerminal {
	var resultado []MarcacionTerminal
	for _, m := range s.marcaciones {
		if m.CodigoUsuario == codigoUsuario && m.InstanteEpochMs >= desdeEpochMs && m.InstanteEpochMs <= hastaEpochMs {
			resultado = append(resultado, m)
		}
	}
	return resultado
}
