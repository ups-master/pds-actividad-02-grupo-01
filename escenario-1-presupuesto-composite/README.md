# Escenario 1 - Presupuesto jerárquico con Composite

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
| Component | | | |
| Leaf | | | |
| Composite | | | |
| Client | | | |

## Diagrama de clases

Fuente: [`uml/compositeE1.puml`](uml/compositeE1.puml).

![Diagrama de clases del escenario 1](uml/diagrama-clases.png)

| Relación | Notación | Significado |
|---|---|---|
| `ElementoPresupuestario` ← `PartidaPresupuestaria`, `GrupoPresupuestario` | herencia | partidas y grupos comparten la misma abstracción, así el cliente no distingue entre ambos |
| `GrupoPresupuestario` ◆→ `ElementoPresupuestario` (`1` → `0..*`, «elementos») | composición | el grupo contiene partidas u otros grupos y los gestiona; es la relación recursiva que forma el árbol |
| `ConsultaPresupuesto` ⇢ `ElementoPresupuestario` («usa») | dependencia | el cliente recibe un `Component` por parámetro y no sabe si es partida o grupo |

Decidimos que `agregar` y `eliminar` queden solo en `GrupoPresupuestario`: una partida no tiene hijos y no debería ofrecer esas operaciones.

## Código fuente

- Código: [`codigo/src/main/java/ec/edu/ups/pds/composite`](codigo/src/main/java/ec/edu/ups/pds/composite)
- Manual de uso: [`codigo/README.md`](codigo/README.md)
- Trazabilidad UML-código: [`docs/trazabilidad-uml-codigo.md`](docs/trazabilidad-uml-codigo.md)

## Uso crítico de inteligencia artificial

### Prompt principal

*(Pendiente)*

### Recomendación aceptada

*(Pendiente, con justificación)*

### Recomendación modificada o rechazada

*(Pendiente, con justificación)*
