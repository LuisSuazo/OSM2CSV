# OSM2CSV

Código que convierte el archivo obtenido de https://download.geofabrik.de.	
Para obtener el archivo csv se debe convertir de shp a csv por medio de ogr2ogr
como en el siguiente comando: 

ogr2ogr -f CSV -lco GEOMETRY=AS_WKT -lco SEPARATOR=SEMICOLON "salida.csv" "entrada.shp"


## Ejecución

Contruir mediante ant, para ejecutar ant run
