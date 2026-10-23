# Actividad 02 - Patrones estructurales

**UNIVERSIDAD POLITÉCNICA SALESIANA - ECUADOR · Posgrados | Maestría en Software**

## Datos generales de la actividad

| | |
|---|---|
| **Asignatura** | Patrones de Diseño de Software |
| **Actividad de aprendizaje #** | 02 - Patrones estructurales |
| **Docente** | Mauricio Ortiz Ochoa |
| **Fecha** | 2026/10/22 |
| **Grupo base** | Grupo 01 |
| **Resultado de aprendizaje** | RA2. Modela soluciones de software aplicando patrones estructurales de acuerdo con el contexto y las necesidades de diseño. |

### Integrantes

| N.º | Integrante (Apellidos Nombres) |
|---|---|
| 1 | Aliatis Ramirez Enzo Paolo |
| 2 | Cadme Vázquez Mauro Emiliano |
| 3 | Murillo Valarezo Jordan Fernando |
| 4 | Suárez Pesantez Galo Fabian |

### Entregables

- Archivo en PDF con el nombre `PDS_Actividad_02_Grupo01.pdf` (lo regenera el grupo).
- Repositorio: <https://github.com/ups-master/pds-actividad-02-grupo-01>
- Aplicación de software almacenada en un repositorio de código (este repositorio).
- La actividad se desarrolla en grupo; la carga al AVAC es individual.

### Requerimientos generales para ambos escenarios

El enunciado completo y la plantilla original están en [`docs/actividad_02_patrones_estructurales.md`](docs/actividad_02_patrones_estructurales.md).

Para cada escenario, el grupo debe:

1. Identificar el problema estructural.
2. Seleccionar y justificar el patrón estructural correspondiente.
3. Elaborar el diagrama de clases UML.
4. Identificar los participantes del patrón.
5. Implementar la solución en un lenguaje de programación.
6. Mantener correspondencia entre el modelo UML y la implementación.
7. Almacenar el código fuente en el repositorio del grupo.
8. Documentar el uso crítico de IA: prompt principal, una recomendación aceptada, una modificada o rechazada, y la justificación de ambas.

### Rúbrica de evaluación

Cada criterio se califica de 0 (no presenta) a 3 (nivel alto):

- Selección, justificación y diagrama UML de Composite.
- Implementación funcional y correspondencia UML-código de Composite.
- Selección, justificación y diagrama UML de Adapter.
- Implementación funcional y correspondencia UML-código de Adapter.
- Uso crítico de IA, justificación y conclusiones.

## Escenarios

| Escenario | Problema | Patrón | Versión oficial (Java) | Versión alternativa (Go) |
|---|---|---|---|---|
| 1. Presupuesto institucional | Tratar de forma uniforme partidas individuales y grupos presupuestarios anidados | Composite | [`escenario-1-presupuesto-composite`](escenario-1-presupuesto-composite/README.md) | [`escenario-1-presupuesto-composite-go`](escenario-1-presupuesto-composite-go/README.md) |
| 2. Asistencia biométrica | Usar la biblioteca de un nuevo fabricante mediante el contrato que ya espera el sistema | Adapter | [`escenario-2-asistencia-adapter`](escenario-2-asistencia-adapter/README.md) | [`escenario-2-asistencia-adapter-go`](escenario-2-asistencia-adapter-go/README.md) |

## Implementaciones: Java (oficial) y Go

El repositorio tiene dos implementaciones de cada escenario.

| | Java | Go |
|---|---|---|
| Estado | **Versión oficial de la entrega**: es la que describen los informes, la trazabilidad, las conclusiones y el PDF | Implementación alternativa del mismo diseño; no reemplaza a la de Java |
| Carpetas | `escenario-1-presupuesto-composite/`, `escenario-2-asistencia-adapter/` | `escenario-1-presupuesto-composite-go/`, `escenario-2-asistencia-adapter-go/` |
| Versión | Compila para Java 21 (`maven.compiler.release` en `pom.xml`); probado con JDK 25 y Maven 3.9 | Go 1.25 (`go.mod`) |
| Diagramas | `uml/*_original.puml` (versión 1) y `uml/*_mejorado.puml` (versión 2, implementada) | `uml/compositeE1_go.puml`, `uml/adapterE2_go.puml` (adaptados a Go: interfaces y structs embebidos en lugar de herencia) |
| Correspondencia UML-código | `CorrespondenciaUmlCodigoTest` en cada módulo (JUnit + reflexión) | `correspondencia_uml_codigo_test.go` en cada paquete |
| Probar | `mvn test` (desde la raíz) | `go test ./...` (desde cada `codigo/` de Go) |
| Ejecutar la demo | `java -cp <escenario>/codigo/target/classes ec.edu.ups.pds.<composite\|adapter>.Demo` | `go run ./cmd/presupuesto` o `go run ./cmd/asistencia` |

Las dos versiones resuelven el mismo problema con el mismo patrón. La de Go cambia lo que el lenguaje
obliga a cambiar (no hay clases ni herencia) y se documenta en el README de su carpeta.

## Probar el proyecto

**Java (oficial).** Requisitos: JDK 21 o superior (probado con JDK 25) y Maven 3.9 o posterior.

```text
mvn test
```

Ejecuta 36 pruebas JUnit (17 del escenario 1 y 19 del escenario 2), incluida en cada módulo
`CorrespondenciaUmlCodigoTest`, que compara el diagrama `uml/*_mejorado.puml` con el código. Para un
solo escenario: `mvn test -pl escenario-1-presupuesto-composite/codigo` o
`mvn test -pl escenario-2-asistencia-adapter/codigo`. Cada manual de uso (`codigo/README.md`) explica
cómo ejecutar la demo.

**Go (alternativa).** Requisito: Go 1.25 o superior. El POM no incluye estos módulos; se prueban por separado:

```text
cd escenario-1-presupuesto-composite-go/codigo && go test ./...
cd escenario-2-asistencia-adapter-go/codigo && go test ./...
```

## Conclusiones de la actividad

En el primer escenario aplicamos Composite porque el presupuesto es una jerarquía parte-todo: partidas y grupos comparten la abstracción `ElementoPresupuestario`, el grupo suma recursivamente a sus elementos y el cliente no distingue entre ambos. En el segundo aplicamos Adapter de objetos porque la biblioteca del fabricante tiene una interfaz incompatible y no puede modificarse; `TerminalHuellaAdapter` implementa el contrato institucional, convierte la fecha en milisegundos, ordena las marcaciones y traduce los errores del SDK.

Una dificultad real fue definir el día de asistencia: una marcación cercana a medianoche cambia de fecha según la zona horaria, así que fijamos el rango [00:00, 24:00) de la zona institucional y lo comprobamos con pruebas de borde. En Composite, evitar ciclos y elementos repetidos exigió una búsqueda recursiva antes de agregar.

Aprendimos que el UML es contrato solo si algo lo verifica. La prueba de correspondencia, que lee el diagrama y lo compara por reflexión con el código, detectó cada diferencia introducida a propósito y obligó a actualizar juntos diagrama, imagen y trazabilidad.

Observamos que la IA tiende a proponer listas de alternativas o cambios genéricos, como reemplazar la composición por agregación, sin conocer nuestras decisiones; sus sugerencias sirvieron solo al contrastarlas con el enunciado.

## Estructura del repositorio

```text
.
├── README.md                              Este archivo
├── AGENTS.md                              Guía para agentes de código
├── pom.xml                                POM padre: Java 21 y JUnit para los dos módulos
├── docs/
│   └── actividad_02_patrones_estructurales.md   Enunciado y plantilla de la actividad
├── escenario-1-presupuesto-composite/
│   ├── README.md                          Informe del escenario 1
│   ├── uml/
│   │   ├── compositeE1_original.puml      Versión 1: primera propuesta (no implementada)
│   │   ├── diagrama-clases-original.png
│   │   ├── compositeE1_mejorado.puml      Versión 2: la que implementa el código
│   │   └── diagrama-clases.png
│   ├── codigo/
│   │   ├── README.md                      Manual: estructura, compilar, probar, demo
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/ec/edu/ups/pds/composite/   Clases del patrón y Demo
│   │       └── test/java/ec/edu/ups/pds/composite/   Una prueba por clase + correspondencia UML
│   └── docs/trazabilidad-uml-codigo.md    Rol, elemento UML, clase y prueba
├── escenario-2-asistencia-adapter/
│   ├── README.md                          Informe del escenario 2
│   ├── uml/
│   │   ├── adapterE2_original.puml        Versión 1: primera propuesta (no implementada)
│   │   ├── diagrama-clases-original.png
│   │   ├── adapterE2_mejorado.puml        Versión 2: la que implementa el código
│   │   ├── diagrama-clases.png
│   │   ├── adapterE2_tercer_proveedor.puml  Extensión hipotética (no implementada)
│   │   └── diagrama-tercer-proveedor.png
│   ├── codigo/
│   │   ├── README.md                      Manual: estructura, compilar, probar, demo
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/ec/edu/ups/pds/adapter/     Clases del patrón, simulaciones y Demo
│   │       └── test/java/ec/edu/ups/pds/adapter/     Una prueba por clase + correspondencia UML
│   └── docs/trazabilidad-uml-codigo.md    Rol, elemento UML, clase y prueba
├── escenario-1-presupuesto-composite-go/  Versión alternativa en Go del escenario 1
│   ├── README.md                          Informe y adaptación a Go
│   ├── uml/compositeE1_go.puml            Diagrama adaptado a Go (+ PNG)
│   └── codigo/                            Módulo Go: composite/ y cmd/presupuesto/
└── escenario-2-asistencia-adapter-go/     Versión alternativa en Go del escenario 2
    ├── README.md                          Informe y adaptación a Go
    ├── uml/adapterE2_go.puml              Diagrama adaptado a Go (+ PNG)
    └── codigo/                            Módulo Go: adapter/ y cmd/asistencia/
```
