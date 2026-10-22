# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Qué es

Actividad 02 de Patrones de Diseño de Software (UPS, Maestría en Software): dos escenarios de patrones
estructurales. Trabajo académico evaluado con rúbrica: la **correspondencia UML ↔ código** y la
documentación (informes, uso crítico de IA) pesan tanto como el código. Idioma del repo: español.

| Escenario | Patrón | Carpeta |
|---|---|---|
| 1 Presupuesto institucional | Composite | `escenario-1-presupuesto-composite/` |
| 2 Asistencia biométrica | Adapter | `escenario-2-asistencia-adapter/` |

El enunciado oficial está en `docs/actividad_02_patrones_estructurales.md`. Cada escenario tiene su
informe (`README.md`), diagramas (`uml/`), código (`codigo/`) y `docs/trazabilidad-uml-codigo.md`.

## Comandos

Java 21 y Maven 3.9+. El POM padre de la raíz agrega los dos módulos `*/codigo`.

```text
mvn test
mvn test -pl escenario-1-presupuesto-composite/codigo
mvn test -Dtest=NombreDeLaClase#metodo
```

## Convenciones

- Código, comentarios y mensajes en español; ASCII sin tildes en mensajes de excepción.
- Paquetes Java: `ec.edu.ups.pds.composite` y `ec.edu.ups.pds.adapter`.
- El UML es contrato: cada escenario tiene una prueba `CorrespondenciaUmlCodigoTest` que lee
  `../uml/*_mejorado.puml` y lo compara con el código; si cambia una clase, actualizar el `.puml`,
  su imagen y la trazabilidad.
- Clases auxiliares que no son participantes (`Demo`) se excluyen en `FUERA_DEL_MODELO` de esa prueba.
- PNG: PlantUML 1.2024.7 + Graphviz; comando en `codigo/README.md` de cada escenario.
- Texto de uso de IA y decisiones del grupo: no inventar evidencia; marcar `[COMPLETAR POR EL GRUPO]`.
- El PDF de entrega lo regenera el grupo; `docs/checklist-pdf.md` lista lo que debe recoger.
