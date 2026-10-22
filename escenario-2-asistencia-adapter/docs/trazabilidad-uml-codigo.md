# Trazabilidad UML–código del escenario 2

Modelo: [`uml/adapterE2_mejorado.puml`](../uml/adapterE2_mejorado.puml). La prueba
[`CorrespondenciaUmlCodigoTest`](../codigo/src/test/java/ec/edu/ups/pds/adapter/CorrespondenciaUmlCodigoTest.java) (abreviada **CUC**) lee ese
archivo y comprueba por reflexión que cada clase, atributo, constructor, método y relación del diagrama
existe en el código con el mismo tipo, visibilidad y firma, y que el código no tiene miembros ni clases
fuera del modelo. Los estereotipos `<<Exception>>` y `<<RuntimeException>>` se verifican contra la
jerarquía de excepciones.

| Rol Adapter | Elemento UML | Implementación Java | Verificación |
|---|---|---|---|
| Target | `interface ServicioAsistencia` | [`ServicioAsistencia.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/ServicioAsistencia.java) | CUC; `SistemaAsistenciaTest.elClienteNoConoceAlFabricante` |
| Target (datos) | `class RegistroAsistencia` | [`RegistroAsistencia.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/RegistroAsistencia.java) | CUC; `RegistroAsistenciaTest.registroCoherente`, `RegistroAsistenciaTest.horasOpcionales` |
| Target (error) | `class ConsultaAsistenciaException <<RuntimeException>>` | [`ConsultaAsistenciaException.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/ConsultaAsistenciaException.java) | CUC; `TerminalHuellaAdapterTest.errorDelSdkTraducido` |
| Implementación existente del Target | `class BiometricoInstitucional` (simulación) | [`BiometricoInstitucional.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/BiometricoInstitucional.java) | CUC; `SistemaAsistenciaTest.mismoResultadoConAmbosProveedores` |
| Adaptee | `class SdkTerminalHuella <<Adaptee>> <<SDK simulado>>` | [`SdkTerminalHuella.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/SdkTerminalHuella.java) | CUC; `TerminalHuellaAdapterTest.sinMarcaciones`, `TerminalHuellaAdapterTest.bordeDeZonaHoraria` |
| Adaptee (datos) | `class MarcacionTerminal <<SDK simulado>>` | [`MarcacionTerminal.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/MarcacionTerminal.java) | CUC |
| Adaptee (error) | `class ErrorTerminalHuella <<SDK simulado>> <<Exception>>` | [`ErrorTerminalHuella.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/ErrorTerminalHuella.java) | CUC; `TerminalHuellaAdapterTest.errorDelSdkTraducido` |
| Adapter | `class TerminalHuellaAdapter` | [`TerminalHuellaAdapter.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/TerminalHuellaAdapter.java) | CUC; todas las pruebas de `TerminalHuellaAdapterTest` |
| Client | `class SistemaAsistencia` | [`SistemaAsistencia.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/SistemaAsistencia.java) | CUC; `SistemaAsistenciaTest` |

## Relaciones

| Relación UML | Cómo se refleja en el código | Verificación |
|---|---|---|
| `ServicioAsistencia <\|.. BiometricoInstitucional` | `implements ServicioAsistencia` | CUC (`getInterfaces`) |
| `ServicioAsistencia <\|.. TerminalHuellaAdapter` | `implements ServicioAsistencia` | CUC (`getInterfaces`) |
| `TerminalHuellaAdapter "*" --> "1" SdkTerminalHuella : adaptado` | atributo `private final SdkTerminalHuella adaptado` (composición de objetos, no herencia) | CUC |
| `SistemaAsistencia --> ServicioAsistencia : servicio` | atributo `private final ServicioAsistencia servicio` | CUC |
| `BiometricoInstitucional --> RegistroAsistencia : registros` | atributo `Map<String, RegistroAsistencia> registros` | CUC |
| `ServicioAsistencia ..> RegistroAsistencia` / `..> ConsultaAsistenciaException` | tipo de retorno y `throws` de `consultarAsistencia` | CUC (tipos en la firma) |
| `SdkTerminalHuella ..> MarcacionTerminal` / `..> ErrorTerminalHuella` | retorno y `throws` de `recuperarMarcaciones` | CUC |
| `TerminalHuellaAdapter ..> MarcacionTerminal` | parámetro `List<MarcacionTerminal>` de `convertir` | CUC |
| `TerminalHuellaAdapter ..> RegistroAsistencia` | retorno de `consultarAsistencia` y `convertir` | CUC |
| `TerminalHuellaAdapter ..> ErrorTerminalHuella` / `..> ConsultaAsistenciaException` | parámetro y retorno de `traducir` | CUC |

## Reglas de transformación

| Regla | Código | Prueba |
|---|---|---|
| Fecha → `[00:00, 24:00)` de la zona, en ms | `consultarAsistencia` | `TerminalHuellaAdapterTest.bordeDeZonaHoraria`, `TerminalHuellaAdapterTest.laZonaHorariaDefineElDia` |
| Ordenar por instante; primera = ingreso, última = salida | `convertir` | `TerminalHuellaAdapterTest.variasMarcacionesDesordenadas` |
| Sin marcaciones → registro sin horas | `convertir` | `TerminalHuellaAdapterTest.sinMarcaciones` |
| Una marcación → ingreso, salida vacía | `convertir` | `TerminalHuellaAdapterTest.unaSolaMarcacion` |
| Error del SDK → `ConsultaAsistenciaException` con causa | `traducir` y `catch (RuntimeException)` | `TerminalHuellaAdapterTest.errorDelSdkTraducido`, `TerminalHuellaAdapterTest.falloInesperadoDelSdkTraducido`, `SistemaAsistenciaTest.soloLlegaLaExcepcionDelDominio` |
| Mismo contrato con ambos proveedores | `SistemaAsistencia` | `SistemaAsistenciaTest.mismoResultadoConAmbosProveedores` |

## Versiones del diagrama

El código y las pruebas corresponden a la **versión 2** (`*_mejorado.puml`). La versión 1
([`uml/adapterE2_original.puml`](../uml/adapterE2_original.puml)) es la primera propuesta del grupo: se conserva como evidencia de la
iteración, no se implementó y `CorrespondenciaUmlCodigoTest` no la lee.

## Fuera del modelo

[`Demo.java`](../codigo/src/main/java/ec/edu/ups/pds/adapter/Demo.java) solo muestra ambos proveedores; no es participante del patrón y está
excluida explícitamente en CUC (`FUERA_DEL_MODELO`). El diagrama
[`adapterE2_tercer_proveedor.puml`](../uml/adapterE2_tercer_proveedor.puml) es una extensión hipotética:
no tiene código y CUC no lo lee.

## Si cambia el código

1. Actualizar `uml/adapterE2_mejorado.puml`.
2. Regenerar `uml/diagrama-clases.png` (ver [`codigo/README.md`](../codigo/README.md#regenerar-el-diagrama)).
3. Actualizar esta tabla y ejecutar `mvn test`.
