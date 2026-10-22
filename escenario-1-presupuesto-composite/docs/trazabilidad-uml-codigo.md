# Trazabilidad UML–código del escenario 1

Modelo: [`uml/compositeE1_mejorado.puml`](../uml/compositeE1_mejorado.puml). La prueba
[`CorrespondenciaUmlCodigoTest`](../codigo/src/test/java/ec/edu/ups/pds/composite/CorrespondenciaUmlCodigoTest.java) (abreviada **CUC**) lee ese
archivo y comprueba por reflexión que cada clase, atributo, constructor, método y relación del diagrama
existe en el código con el mismo tipo, visibilidad y firma, y que el código no tiene miembros ni clases
fuera del modelo.

| Rol Composite | Elemento UML | Implementación Java | Verificación |
|---|---|---|---|
| Component | `abstract class ElementoPresupuestario` | [`ElementoPresupuestario.java`](../codigo/src/main/java/ec/edu/ups/pds/composite/ElementoPresupuestario.java) | CUC; `PartidaPresupuestariaTest.rechazoDeValoresInvalidos`, `GrupoPresupuestarioTest.rechazoDeValoresInvalidos` |
| Leaf | `class PartidaPresupuestaria` | [`PartidaPresupuestaria.java`](../codigo/src/main/java/ec/edu/ups/pds/composite/PartidaPresupuestaria.java) | CUC; `PartidaPresupuestariaTest.valorTotalDeUnaPartida`, `PartidaPresupuestariaTest.rechazoDeValoresInvalidos` |
| Composite | `class GrupoPresupuestario` | [`GrupoPresupuestario.java`](../codigo/src/main/java/ec/edu/ups/pds/composite/GrupoPresupuestario.java) | CUC; `GrupoPresupuestarioTest.grupoConPartidas`, `GrupoPresupuestarioTest.gruposAnidadosAVariosNiveles`, `GrupoPresupuestarioTest.grupoVacio`, `GrupoPresupuestarioTest.agregarYEliminar`, `GrupoPresupuestarioTest.elementosNoModificables`, `GrupoPresupuestarioTest.rechazoDeCiclos`, `GrupoPresupuestarioTest.estructuraConSangria` |
| Client | `class ConsultaPresupuesto` | [`ConsultaPresupuesto.java`](../codigo/src/main/java/ec/edu/ups/pds/composite/ConsultaPresupuesto.java) | CUC; `ConsultaPresupuestoTest.clienteUniformeConPartidaYGrupo`, `ConsultaPresupuestoTest.nuevoNivelSinModificarElCliente` |

## Relaciones

| Relación UML | Cómo se refleja en el código | Verificación |
|---|---|---|
| `ElementoPresupuestario <\|-- PartidaPresupuestaria` | `extends ElementoPresupuestario` | CUC (`getSuperclass`) |
| `ElementoPresupuestario <\|-- GrupoPresupuestario` | `extends ElementoPresupuestario` | CUC (`getSuperclass`) |
| `GrupoPresupuestario "1" *--> "0..*" ElementoPresupuestario : elementos` | atributo `private final List<ElementoPresupuestario> elementos` | CUC (atributo con ese nombre y tipo) |
| `ConsultaPresupuesto ..> ElementoPresupuestario : usa` | parámetro de `generarReporte(ElementoPresupuestario)` | CUC (tipo en la firma) |

## Reglas de las notas del diagrama

| Regla | Código | Prueba |
|---|---|---|
| Código y descripción no nulos ni vacíos | `ElementoPresupuestario.validarTexto` | `PartidaPresupuestariaTest.rechazoDeValoresInvalidos`, `GrupoPresupuestarioTest.rechazoDeValoresInvalidos` |
| Valor asignado no nulo y `>= 0` | constructor de `PartidaPresupuestaria` | `PartidaPresupuestariaTest.rechazoDeValoresInvalidos` |
| Total recursivo con `BigDecimal`; grupo vacío = 0 | `GrupoPresupuestario.calcularValorTotal` | `GrupoPresupuestarioTest.gruposAnidadosAVariosNiveles`, `GrupoPresupuestarioTest.grupoVacio` |
| `getElementos()` no modificable | `Collections.unmodifiableList` | `GrupoPresupuestarioTest.elementosNoModificables` |
| Sin ciclos ni repetidos | `agregar` + `contieneDescendiente` | `GrupoPresupuestarioTest.rechazoDeCiclos` |
| Sangría de 2 espacios por nivel | `formatearLinea`, `mostrarEstructura` | `GrupoPresupuestarioTest.estructuraConSangria` |

## Versiones del diagrama

El código y las pruebas corresponden a la **versión 2** (`*_mejorado.puml`). La versión 1
([`uml/compositeE1_original.puml`](../uml/compositeE1_original.puml)) es la primera propuesta del grupo: se conserva como evidencia de la
iteración, no se implementó y `CorrespondenciaUmlCodigoTest` no la lee.

## Fuera del modelo

[`Demo.java`](../codigo/src/main/java/ec/edu/ups/pds/composite/Demo.java) solo arma el ejemplo del enunciado; no es participante del patrón y está
excluida explícitamente en CUC (`FUERA_DEL_MODELO`).

## Si cambia el código

1. Actualizar `uml/compositeE1_mejorado.puml`.
2. Regenerar `uml/diagrama-clases.png` (ver [`codigo/README.md`](../codigo/README.md#regenerar-el-diagrama)).
3. Actualizar esta tabla y ejecutar `mvn test`.
