# Checklist para regenerar `PDS_Actividad_02_Grupo01.pdf`

El PDF lo regenera el grupo desde su documento de Word. Esta lista compara la versión revisada por el
docente (11 páginas, creada el 22/10/2026) con el estado actual del repositorio.

## Datos generales (página 2)

- [ ] **Entregables**: cambiar `PDS_Actividad_02_GrupoXX.pdf` por `PDS_Actividad_02_Grupo01.pdf`.
- [ ] **Integrantes**: cambiar «Suarez Pesante Galo Fabian» por **«Suárez Pesantez Galo Fabian»** (apellido
  confirmado por el grupo; así figura en el `README.md`).
- [ ] **Fecha**: dejar una sola fecha, **2026/10/22**, igual que en el `README.md`. Revisar que no quede
  otra fecha en portada, encabezados o pies.
- [ ] Revisar tildes del resto de nombres para que coincidan con el `README.md`.

## Escenario 01

- [ ] Incluir **dos diagramas**: versión 1 `uml/diagrama-clases-original.png` (el que ya tenía el PDF, ahora a
  200 dpi) y versión 2 `escenario-1-presupuesto-composite/uml/diagrama-clases.png` (3848 × 1462 px), seguidos
  de la tabla «Cambios de la versión 1 a la 2» del `README.md`.
  Insertarlos a todo el ancho de la página o en una página horizontal para que se lea.
- [ ] Añadir las secciones nuevas del informe: variante segura (`agregar`/`eliminar` solo en el grupo),
  justificación de la composición, validaciones, supuestos y **consecuencias del patrón**.
- [ ] Código fuente: además del enlace a la carpeta, citar el manual
  `escenario-1-presupuesto-composite/codigo/README.md` y la trazabilidad
  `escenario-1-presupuesto-composite/docs/trazabilidad-uml-codigo.md`.
- [ ] Uso crítico de IA: reemplazar el prompt que pide elegir el patrón y la «recomendación rechazada» que
  hoy es una lista de patrones alternativos, por las secciones nuevas del `README.md` una vez que el
  grupo complete los `[COMPLETAR POR EL GRUPO]`.

## Escenario 02

- [ ] Añadir la sección **Patrón seleccionado** (hoy falta: el PDF pasa de «Problema estructural» a
  «Participantes»).
- [ ] Incluir **dos diagramas**: versión 1 `uml/diagrama-clases-original.png` y versión 2
  `escenario-2-asistencia-adapter/uml/diagrama-clases.png` (4520 × 1801 px), con la tabla «Cambios de la
  versión 1 a la 2»;
  preferiblemente en página horizontal.
- [ ] Añadir el diagrama del tercer proveedor `uml/diagrama-tercer-proveedor.png` con su explicación.
- [ ] Añadir adaptador de objetos frente a adaptador de clase, **reglas de transformación** (incluida la
  decisión de la marcación única), supuestos y **consecuencias del patrón**.
- [ ] Código fuente: enlace, manual (`codigo/README.md`) y trazabilidad (`docs/trazabilidad-uml-codigo.md`).
- [ ] Uso crítico de IA: igual que en el escenario 1; la «recomendación modificada o rechazada» no puede
  ser una lista de patrones (Facade, Bridge, Decorator).

## Conclusiones

- [ ] Reemplazar por el texto de la sección «Conclusiones de la actividad» del `README.md` (200 palabras
  exactas) después de que el grupo lo revise. Si se edita, volver a contar las palabras.

## Verificación final

- [ ] `mvn test` pasa desde la raíz (34 pruebas).
- [ ] El enlace al repositorio (`https://github.com/ups-master/pds-actividad-02-grupo-01`) abre y la rama
  `main` contiene los últimos commits.
- [ ] No queda ningún `[COMPLETAR POR EL GRUPO]` en el PDF.
