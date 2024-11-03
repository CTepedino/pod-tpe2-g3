# POD-TPE2-G3

## Autores

- Bloise, Luca
- Mendonca, Juana
- Tepedino, Cristian

## Compilación

Para compilar el proyecto, se deben ejecutar el siguientes comando desde la carpeta raiz del proyecto:

```bash
mvn clean install
```

Esto generara los directorios "target" en client y en server, y dentro se encontrara el archivo "tpe2-g3-{directorio}-1.0-SNAPSHOT-bin.tar.gz". 

Descomprimir:

```bash
tar -xvzf tpe2-g3-{directorio}-1.0-SNAPSHOT-bin.tar.gz
```
Dentro, se encuentran los scripts necesarios para correr los clientes y el servidor respectivamente.

## Uso

### Servidor

Para iniciar el servidor, ejecutar el siguiente comando:

```bash
./run-node.sh
```

### Clientes

Dentro el archivo previamente mencionado para client se encuntran los scripts para realizar las distintas querys. 

Query 1:

Para correr la primera query, efecutar el siguinete comando:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio}
```

-Daddresses se refiere a las direcciones IP de los nodos con sus puertos, si hay mas de una se separan con ;
-Dcity indica la ciudad, NYC para Nueva York y CHI para Chicago
-DinPath indica el path donde están los archivos de entrada de multas, infracciones y agencias
-DoutPath indica el path donde estarán ambos archivos de salida query1.csv y time1.txt. 

Query 2:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio}
```

Query 3:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio} -Dn=2 -Dfrom=01/01/2021 -Dto=31/12/2021
```
-Dn se refiere a la minima cantidad de mutlas para considerar que la patente es reincidente en un barrio
-Dfrom y -Dto para indicar el rango de fechas


Query 4:

```bash
sh query1.sh -Daddresses='10.6.0.1:5701' -Dcity=NYC  -DinPath={path al directorio} -DoutPath={path al directorio} -Dn=3 -Dagency=DEPARTMENT_OF_TRANSPORTATION
```

-Dn indica para listar las primeras n infracciones
-Dagency para indicar la agencia
