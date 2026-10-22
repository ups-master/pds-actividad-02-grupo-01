# Manual de uso - Escenario 1 (Presupuesto jerárquico con Composite)

Requisitos: JDK 21 o superior y Maven 3.9 o posterior. Los comandos de esta sección se lanzan desde
esta carpeta (`escenario-1-presupuesto-composite/codigo`); al final están los equivalentes desde la raíz del repositorio.

## Compilar

```text
mvn compile
```

## Probar

```text
mvn test
```

Ejecuta 16 pruebas: 11 de comportamiento (`PresupuestoCompositeTest`) y 5 de correspondencia UML-código. La prueba `CorrespondenciaUmlCodigoTest` lee `../uml/compositeE1_mejorado.puml`, así que hay que lanzarla desde
esta carpeta o desde la raíz con `-pl` (Maven usa la carpeta del módulo como directorio de trabajo).

Una sola clase o un solo método:

```text
mvn test -Dtest=CorrespondenciaUmlCodigoTest
mvn test -Dtest=NombreDeLaClase#nombreDelMetodo
```

## Ejecutar la demo

```text
mvn package
java -cp target/classes ec.edu.ups.pds.composite.Demo
```

`mvn package` compila, prueba y genera `target/presupuesto-composite-1.0.0-SNAPSHOT.jar`; la demo también puede
ejecutarse con `java -cp target/presupuesto-composite-1.0.0-SNAPSHOT.jar ec.edu.ups.pds.composite.Demo`.

## Desde la raíz del repositorio

```text
mvn test -pl escenario-1-presupuesto-composite/codigo
mvn package -pl escenario-1-presupuesto-composite/codigo
java -cp escenario-1-presupuesto-composite/codigo/target/classes ec.edu.ups.pds.composite.Demo
```

## Regenerar el diagrama

Si cambia el código, hay que actualizar `../uml/compositeE1_mejorado.puml` y regenerar su imagen. Con PlantUML 1.2024.7 y
Graphviz instalados, desde `../uml`:

```text
mvn dependency:get -Dartifact=net.sourceforge.plantuml:plantuml:1.2024.7
java -DPLANTUML_LIMIT_SIZE=16384 -jar ~/.m2/repository/net/sourceforge/plantuml/plantuml/1.2024.7/plantuml-1.2024.7.jar -tpng -pipe < compositeE1_mejorado.puml > diagrama-clases.png
```

El diagrama original (versión 1) no se edita; si hiciera falta volver a generar su imagen:

```text
java -jar ~/.m2/repository/net/sourceforge/plantuml/plantuml/1.2024.7/plantuml-1.2024.7.jar -Sdpi=200 -tpng -pipe < compositeE1_original.puml > diagrama-clases-original.png
```

