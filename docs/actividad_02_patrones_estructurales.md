# UNIVERSIDAD POLITÉCNICA SALESIANA - ECUADOR
## POSGRADOS | Maestría en SOFTWARE

* **Asignatura:** Patrones de Diseño de Software
* **Actividad #:** 02 - Patrones estructurales
* **Docente:** Mauricio Ortiz Ochoa

---

## Datos Generales de la Actividad

* **Actividad de aprendizaje #:** 02 - Patrones estructurales
* **Fecha:** 2026/09/24
* **Grupo base:** Grupo XX
* **Integrantes:**
  * Apellido 1 Apellido 2 Nombre 1 Nombre 2
  * Apellido 1 Apellido 2 Nombre 1 Nombre 2
  * Apellido 1 Apellido 2 Nombre 1 Nombre 2
  * Apellido 1 Apellido 2 Nombre 1 Nombre 2
* **Resultado de aprendizaje:** RA2. Modela soluciones de software aplicando patrones estructurales de acuerdo con el contexto y las necesidades de diseño.

### Entregables:
* Archivo en PDF con el siguiente nombre: `PDS_Actividad_02_GrupoXX.pdf`
* Aplicación de Software almacenado en repositorio de código.
* La actividad se desarrolla en grupo; la carga al AVAC es individual.

### Tareas específicas:
1. Participar en la clase de la Unidad 02, secciones 01, 02 y 03.
2. Leer detenidamente el material de apoyo relacionado.
3. Crear un repositorio de código para almacenar la aplicación solicitada.
4. Seguir las instrucciones del ejercicio.

---

## Instrucciones

### Escenario 01:
Una institución necesita representar la estructura de su presupuesto mediante diferentes niveles jerárquicos.

Existen partidas presupuestarias individuales, que contienen información como código, descripción y valor asignado. Estas partidas representan los elementos básicos sobre los cuales se registran los recursos disponibles.

Sin embargo, para organizar el presupuesto también existen grupos presupuestarios. Un grupo puede contener varias partidas individuales, pero también puede contener otros grupos presupuestarios, formando una estructura jerárquica de varios niveles.

Por ejemplo, un grupo denominado **Tecnologías de la Información** podría contener partidas relacionadas con licenciamiento, infraestructura y servicios. A su vez, el grupo **Infraestructura** podría contener otros grupos o partidas correspondientes a servidores, almacenamiento y comunicaciones.

Tanto las partidas individuales como los grupos deben permitir realizar operaciones comunes, entre ellas:
* obtener su código y descripción;
* calcular su valor total;
* mostrar su estructura.

En una partida individual, el valor total corresponde directamente al valor asignado. En un grupo presupuestario, el valor total corresponde a la suma de los valores de todos los elementos que contiene, incluyendo aquellos que formen parte de grupos internos.

Actualmente, el código cliente tendría que identificar constantemente si está trabajando con una partida individual o con un grupo para decidir cómo calcular su valor o recorrer sus componentes. Se desea evitar esta distinción y permitir que ambos tipos de elementos puedan tratarse mediante una misma abstracción.

Además, la estructura debe poder crecer incorporando nuevos niveles de agrupación sin modificar la lógica principal que consulta o procesa el presupuesto.

Diseñar una solución orientada a objetos que permita representar la estructura jerárquica, realizar operaciones comunes sobre elementos individuales y agrupaciones y calcular recursivamente los valores correspondientes.

### Escenario 02:
Una Universidad dispone de un sistema institucional para registrar y consultar la asistencia de sus colaboradores.

El sistema interno utiliza un contrato común mediante el cual solicita los registros de una persona para una fecha determinada. Como resultado espera obtener información normalizada, por ejemplo:
* identificación del colaborador;
* fecha;
* hora de ingreso;
* hora de salida.

Durante varios años se han utilizado dispositivos biométricos de un proveedor que ya trabaja de acuerdo con esta estructura.

Sin embargo, la universidad adquiere dispositivos de otro fabricante. Estos equipos incluyen una biblioteca propia que no puede modificarse y cuya interfaz es diferente. Por ejemplo, en lugar de consultar directamente la asistencia diaria, la biblioteca permite recuperar una colección de marcaciones mediante operaciones y tipos de datos propios del fabricante.

Los nombres de las operaciones, los parámetros requeridos y las estructuras devueltas no coinciden con la interfaz que utiliza el sistema institucional.

Modificar todo el sistema para trabajar directamente con la nueva biblioteca produciría dependencia respecto del proveedor. Tampoco resulta conveniente modificar el software entregado por el fabricante, porque podría perderse compatibilidad con futuras versiones.

Además, en el futuro podrían adquirirse equipos de otros proveedores con interfaces nuevamente diferentes.

La universidad necesita mantener estable el contrato utilizado por su sistema y permitir que los nuevos dispositivos puedan incorporarse sin que el resto de la aplicación conozca sus particularidades.

Diseñar una solución orientada a objetos que permita utilizar el nuevo dispositivo mediante la interfaz que ya espera el sistema institucional, realizando las transformaciones necesarias entre ambos componentes.

### Requerimientos generales para ambos escenarios:
Para ambos escenarios, cada grupo deberá:
1. Identificar el problema estructural que presenta el escenario.
2. Seleccionar y justificar el patrón estructural correspondiente.
3. Elaborar el diagrama de clases UML.
4. Identificar los participantes del patrón dentro de la solución propuesta.
5. Implementar la solución en un lenguaje de programación.
6. Mantener correspondencia entre el modelo UML y la implementación.
7. Almacenar el código fuente en el repositorio del grupo.
8. Para cada escenario, documentar el uso crítico de IA mediante:
   * Prompt principal.
   * Una recomendación aceptada.
   * Una recomendación modificada o rechazada.
   * Justificación de ambas decisiones.

---

## Desarrollo de la Actividad

### Escenario 01:

#### Justificación
*(Escribir aquí la justificación)*

#### Diagrama de clases
*(Insertar aquí el diagrama de clases)*

#### Código fuente
*(Insertar enlace o código fuente)*

#### Uso crítico de inteligencia artificial
* **Prompt principal:** 
* **Recomendación aceptada (con justificación):** 
* **Recomendación modificada o rechazada (con justificación):** 

---

### Escenario 02:

#### Justificación
*(Escribir aquí la justificación)*

#### Diagrama de clases
*(Insertar aquí el diagrama de clases)*

#### Código fuente
*(Insertar enlace o código fuente)*

#### Uso crítico de inteligencia artificial
* **Prompt principal:** 
* **Recomendación aceptada (con justificación):** 
* **Recomendación modificada o rechazada (con justificación):** 

---

### Conclusiones de la actividad (200 palabras)
* **Conclusiones:** *(Escribir aquí las conclusiones)*

---

## Rúbrica de evaluación

| Criterios | No Presenta | Nivel Bajo | Nivel Medio | Nivel Alto |
| :--- | :---: | :---: | :---: | :---: |
| Selección, justificación y diagrama UML de Composite | 0 | 1 | 2 | 3 |
| Implementación funcional y correspondencia UML–código de Composite | 0 | 1 | 2 | 3 |
| Selección, justificación y diagrama UML de Adapter | 0 | 1 | 2 | 3 |
| Implementación funcional y correspondencia UML–código de Adapter | 0 | 1 | 2 | 3 |
| Uso crítico de IA, justificación y conclusiones | 0 | 1 | 2 | 3 |

## Documentos relacionados:
* `Unidad_01_01_PDS.pdf`
* `Unidad_01_02_PDS.pdf`
* `Unidad_01_05_PDS.pdf`
* `Unidad_02_01_PDS.pdf`