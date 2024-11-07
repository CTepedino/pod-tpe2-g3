# POD-TPE2-G3

## Autores

- Bloise, Luca
- Mendonca, Juana
- Tepedino, Cristian

## Compilación

Para compilar el proyecto, se deben ejecutar el siguientes comandos desde la carpeta raiz del proyecto:

```bash
mvn clean install
```

Esto generara los directorios "target" en client y en server, y dentro se encontrara el archivo "tpe2-g3-{directorio}-1.0-SNAPSHOT-bin.tar.gz". 

Descomprimir:

```bash
tar -xvzf tpe2-g3-{directorio}-1.0-SNAPSHOT-bin.tar.gz
```
Dentro, se encuentran los scripts necesarios para correr los clientes y el servidor respectivamente. Es necesario darles permisos de ejecución, por ejemplo con

```bash
chmod +x run-node.sh
```

## Uso

### Servidor

Para iniciar un nodo del servidor, ejecutar el siguiente comando:

```bash
./run-node.sh -Daddresses='10.6.0.1;127.0.0.*' -Dport=8080
```
-Daddresses se refiere a las direcciones IP de los nodos en los que Hazelcast se vinculara. Por defecto es 127.0.0.*

-Dport indica el puerto en que Hazelcast escuchara. Por defecto es 5701.

### Clientes

Dentro el archivo previamente mencionado para client se encuntran los scripts para realizar las distintas querys.

Todas las queries requieren indicar un codigo de ciudad, y 2 path a directorios: uno que contenga los datos a procesar y otro en donde se escribiran los archivos de salida

Se espera que el directorio con los datos contenga los siguientes archivos:

- tickets{CODIGO}.csv
- infractions{CODIGO}.csv
- agencies{CODIGO}.csv

En donde CODIGO es el codigo de ciudad indicado

#### Query 1:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701;127.0.0.1' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio}
```

Lista el total de multas por infracción y agencia.

-Daddresses se refiere a las direcciones IP de los nodos, opcionalmente con sus puertos (por defecto, usa 5701), si hay mas de una se separan con ;

-Dcity indica la ciudad, NYC para Nueva York y CHI para Chicago

-DinPath indica el path donde están los archivos de entrada de multas, infracciones y agencias

-DoutPath indica el path donde estarán ambos archivos de salida query1.csv y time1.txt. 

#### Query 2:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio}
```

Lista la recaudación YTD (Year To Date) por agencia

#### Query 3:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio} -Dn=2 -Dfrom=01/01/2021 -Dto=31/12/2021
```

Lista el porcentaje de patentes reincidentes por barrio en un plazo de tiempo

-Dn se refiere a la minima cantidad de multas para considerar que la patente es reincidente en un barrio

-Dfrom y -Dto para indicar el rango de fechas

#### Query 4:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio} -Dn=3 -Dagency=DEPARTMENT_OF_TRANSPORTATION
```

Lista las infracciones con mayor diferencia entre maximo y minimo monto para una agencia

-Dn indica para listar las primeras n infracciones

-Dagency para indicar la agencia
