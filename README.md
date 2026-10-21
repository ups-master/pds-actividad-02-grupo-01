# Actividad 02 - Patrones estructurales

**UNIVERSIDAD POLITÉCNICA SALESIANA - ECUADOR · Posgrados | Maestría en Software**

## Datos generales de la actividad

| | |
|---|---|
| **Asignatura** | Patrones de Diseño de Software |
| **Actividad de aprendizaje #** | 02 - Patrones estructurales |
| **Docente** | Mauricio Ortiz Ochoa |
| **Fecha** | 2026/09/24 |
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

- Archivo en PDF con el nombre `PDS_Actividad_02_Grupo01.pdf`.
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

## Conclusiones de la actividad

*(Pendiente: 200 palabras.)*

## Estructura del repositorio

```text
.
├── README.md                              Este archivo
├── CLAUDE.md                              Guía para Claude Code
├── pom.xml                                POM padre: Java 21 y JUnit para los dos módulos
├── docs/
│   └── actividad_02_patrones_estructurales.md   Enunciado y plantilla de la actividad
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
