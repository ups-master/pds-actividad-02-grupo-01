# Escenario 2 - Asistencia biométrica con Adapter

## Contenido

1. [El problema estructural](#el-problema-estructural)
2. [Justificación](#justificación)
3. [Diagrama de clases](#diagrama-de-clases)
4. [Código fuente](#código-fuente)
5. [Uso crítico de inteligencia artificial](#uso-crítico-de-inteligencia-artificial)

## El problema estructural

*(Pendiente)*

## Justificación

*(Pendiente)*

### Participantes del patrón

| Participante | Clase o interfaz | Responsabilidad |
|---|---|---|
| Target | | | |
| Adaptee | | | |
| Adapter | | | |
| Client | | | |

## Diagrama de clases

Fuente: [`uml/adapterE2.puml`](uml/adapterE2.puml).

![Diagrama de clases del escenario 2](uml/diagrama-clases.png)

| Relación | Notación | Significado |
|---|---|---|
| `ServicioAsistencia` ← `BiometricoInstitucional`, `TerminalHuellaAdapter` | realización | el proveedor anterior y el adaptador cumplen el mismo contrato del sistema |
| `TerminalHuellaAdapter` → `SdkTerminalHuella` (`*` → `1`, «adapta») | asociación | el adaptador guarda el SDK del fabricante (atributo `adaptado`) y le delega las llamadas |
| `SdkTerminalHuella` ⇢ `MarcacionTerminal` («devuelve») | dependencia | tipo propio del fabricante que el adaptador debe convertir |
| `ServicioAsistencia` ⇢ `RegistroAsistencia` («devuelve») | dependencia | datos normalizados que espera el sistema |
| `SistemaAsistencia` → `ServicioAsistencia` («usa») | asociación | el cliente solo conoce el Target, nunca el SDK |

`SdkTerminalHuella` es la biblioteca del fabricante: no la modificamos, el adaptador la usa tal cual.

## Código fuente

- Código: [`codigo/src/main/java/ec/edu/ups/pds/adapter`](codigo/src/main/java/ec/edu/ups/pds/adapter)
- Manual de uso: [`codigo/README.md`](codigo/README.md)
- Trazabilidad UML-código: [`docs/trazabilidad-uml-codigo.md`](docs/trazabilidad-uml-codigo.md)

## Uso crítico de inteligencia artificial

### Prompt principal

*(Pendiente)*

### Recomendación aceptada

*(Pendiente, con justificación)*

### Recomendación modificada o rechazada

*(Pendiente, con justificación)*
