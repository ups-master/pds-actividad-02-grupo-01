# Manual de uso - Escenario 2 (Asistencia biométrica con Adapter)

Requisitos: JDK 21 o superior y Maven 3.9 o posterior. Los comandos de esta sección se lanzan desde
esta carpeta (`escenario-2-asistencia-adapter/codigo`); al final están los equivalentes desde la raíz del repositorio.

## Compilar

```text
mvn compile
```

## Probar

```text
mvn test
```

Ejecuta 18 pruebas: 9 del adaptador (`TerminalHuellaAdapterTest`), 4 del cliente (`SistemaAsistenciaTest`) y 5 de correspondencia UML-código. La prueba `CorrespondenciaUmlCodigoTest` lee `../uml/adapterE2_mejorado.puml`, así que hay que lanzarla desde
esta carpeta o desde la raíz con `-pl` (Maven usa la carpeta del módulo como directorio de trabajo).

Una sola clase o un solo método:

```text
mvn test -Dtest=CorrespondenciaUmlCodigoTest
mvn test -Dtest=NombreDeLaClase#nombreDelMetodo
```

## Ejecutar la demo

```text
mvn package
java -cp target/classes ec.edu.ups.pds.adapter.Demo
```

`mvn package` compila, prueba y genera `target/asistencia-adapter-1.0.0-SNAPSHOT.jar`; la demo también puede
ejecutarse con `java -cp target/asistencia-adapter-1.0.0-SNAPSHOT.jar ec.edu.ups.pds.adapter.Demo`.

## Desde la raíz del repositorio

```text
mvn test -pl escenario-2-asistencia-adapter/codigo
mvn package -pl escenario-2-asistencia-adapter/codigo
java -cp escenario-2-asistencia-adapter/codigo/target/classes ec.edu.ups.pds.adapter.Demo
```

## Regenerar el diagrama

Si cambia el código, hay que actualizar `../uml/adapterE2_mejorado.puml` y regenerar su imagen. Con PlantUML 1.2024.7 y
Graphviz instalados, desde `../uml`:

```text
mvn dependency:get -Dartifact=net.sourceforge.plantuml:plantuml:1.2024.7
java -DPLANTUML_LIMIT_SIZE=16384 -jar ~/.m2/repository/net/sourceforge/plantuml/plantuml/1.2024.7/plantuml-1.2024.7.jar -tpng -pipe < adapterE2_mejorado.puml > diagrama-clases.png
```

El diagrama original (versión 1) no se edita; si hiciera falta volver a generar su imagen:

```text
java -jar ~/.m2/repository/net/sourceforge/plantuml/plantuml/1.2024.7/plantuml-1.2024.7.jar -Sdpi=200 -tpng -pipe < adapterE2_original.puml > diagrama-clases-original.png
```

El diagrama del tercer proveedor se genera igual, con `adapterE2_tercer_proveedor.puml` y salida
`diagrama-tercer-proveedor.png`.
