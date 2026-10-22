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

- Archivo en PDF con el nombre `PDS_Actividad_02_Grupo01.pdf` (lo regenera el grupo; ver
  [`docs/checklist-pdf.md`](docs/checklist-pdf.md)).
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

| Escenario | Problema | Patrón | Informe |
|---|---|---|---|
| 1. Presupuesto institucional | Tratar de forma uniforme partidas individuales y grupos presupuestarios anidados | Composite | [`escenario-1-presupuesto-composite`](escenario-1-presupuesto-composite/README.md) |
| 2. Asistencia biométrica | Usar la biblioteca de un nuevo fabricante mediante el contrato que ya espera el sistema | Adapter | [`escenario-2-asistencia-adapter`](escenario-2-asistencia-adapter/README.md) |

## Probar el proyecto

Requisitos: JDK 21 o superior y Maven 3.9 o posterior.

```text
mvn test
```

Ejecuta 34 pruebas JUnit (16 del escenario 1 y 18 del escenario 2), incluida en cada módulo
`CorrespondenciaUmlCodigoTest`, que compara el diagrama `uml/*_mejorado.puml` con el código. Para un
solo escenario: `mvn test -pl escenario-1-presupuesto-composite/codigo` o
`mvn test -pl escenario-2-asistencia-adapter/codigo`. Cada manual de uso (`codigo/README.md`) explica
cómo ejecutar la demo.

## Conclusiones de la actividad

> Borrador de exactamente 200 palabras (sin contar esta nota).
> `[COMPLETAR POR EL GRUPO: revisar que la dificultad y la limitación de la IA reflejen lo que el grupo vivió]`

En el primer escenario aplicamos Composite porque el presupuesto es una jerarquía parte-todo: partidas y grupos comparten la abstracción `ElementoPresupuestario`, el grupo suma recursivamente a sus elementos y el cliente no distingue entre ambos. En el segundo aplicamos Adapter de objetos porque la biblioteca del fabricante tiene una interfaz incompatible y no puede modificarse; `TerminalHuellaAdapter` implementa el contrato institucional, convierte la fecha en milisegundos, ordena las marcaciones y traduce los errores del SDK.

Una dificultad real fue definir el día de asistencia: una marcación cercana a medianoche cambia de fecha según la zona horaria, así que fijamos el rango [00:00, 24:00) de la zona institucional y lo comprobamos con pruebas de borde. En Composite, evitar ciclos y elementos repetidos exigió una búsqueda recursiva antes de agregar.

Aprendimos que el UML es contrato solo si algo lo verifica. La prueba de correspondencia, que lee el diagrama y lo compara por reflexión con el código, detectó cada diferencia introducida a propósito y obligó a actualizar juntos diagrama, imagen y trazabilidad.

Observamos que la IA tiende a proponer listas de alternativas o cambios genéricos, como reemplazar la composición por agregación, sin conocer nuestras decisiones; sus sugerencias sirvieron solo al contrastarlas con el enunciado.

## Estructura del repositorio

```text
.
├── README.md                              Este archivo
├── CLAUDE.md                              Guía para Claude Code
├── pom.xml                                POM padre: Java 21 y JUnit para los dos módulos
├── docs/
│   ├── actividad_02_patrones_estructurales.md   Enunciado y plantilla de la actividad
│   └── checklist-pdf.md                   Cambios que debe recoger el PDF
├── escenario-1-presupuesto-composite/
│   ├── README.md                          Informe del escenario 1
│   ├── uml/                               Diagramas PlantUML e imágenes
│   ├── codigo/                            Proyecto Maven y manual de uso
│   └── docs/                              Trazabilidad UML-código
└── escenario-2-asistencia-adapter/
    ├── README.md                          Informe del escenario 2
    ├── uml/
    ├── codigo/
    └── docs/
```
