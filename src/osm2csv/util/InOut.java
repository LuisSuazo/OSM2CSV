package osm2csv.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.csvreader.CsvReader;

public class InOut {
	public void LecturaCallesOSM(String inputFile, String CarpetaSalida,String output) {
		try {
			String[] puntos, wf;
			String line = "",wkt,w2,w3,w4,w5,tipo,nombre,clase;
			int osm_id, oneway;
			int no = -1;
			double [] inicio = new double[2];
			double [] fin = new double[2];
			double longitud=0;

			// Se elimina el archivo de escritura si es que ya se encuentra creado.
			String Salida="Output/"+CarpetaSalida+"/";
			File folder = new File(Salida);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			Salida=Salida+output;
			// Se crea el archivo de salida con el csv modificado.
			FileWriter w = new FileWriter(Salida);
			BufferedWriter bw = new BufferedWriter( w );

			// Header del archivo csv a crear.
			//String linea1 = "osm_id;xInicio;yInicio;xMedio;yMedio;xFin;yFin;direccion;type;name\n";
			String linea1 = "idarco;idtramo;nombre;longitud;clase;punto_inicio_x;punto_inicio_y;punto_final_x;punto_final_y;sentido;velocidad\n";

			bw.write( linea1 );
			bw.flush( );


			// Leer el arvhivo de cuadrantes y calles
			CsvReader archivo = new CsvReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
			//CsvReader archivo = new CsvReader( args[0] );
			archivo.setDelimiter( ';' );
			int Velocidad;
			// Leer registros
			archivo.readHeaders( );

			// Leer cada linea del archivo.
			int idTramo=1;
			while( archivo.readRecord( ) ){
				wkt = archivo.get( "WKT" ); // Coordenadas
				osm_id = Integer.parseInt( archivo.get( "osm_id" ) ); // id OSM
				tipo = archivo.get( "fclass" ); // Tipo de calle
				nombre = archivo.get( "name" ); //Nombre de calle
				if(archivo.get( "oneway" ).contentEquals("F")) {//Direccion calle
					oneway=1;
				}else if(archivo.get( "oneway" ).contentEquals("T")){
					oneway=-1;
				}else {
					oneway=0;
				}
				clase = archivo.get("class"); //Clase calle
				nombre = nombre.replaceAll(";", "-");

				
				// Filtro de calles por tipo. http://wiki.openstreetmap.org/wiki/Key:highway#Roads
				if( /*tipo.equals("living_street")||*/tipo.equals("motorway")||tipo.equals("motorway_link")
						/*||tipo.equals("pedestrian")*/||tipo.equals("primary")||tipo.equals("primary_link")
						/*||tipo.equals("residential")*/||tipo.equals("secondary")||tipo.equals("secondary_link")
						/*||tipo.equals("service")*/||tipo.equals("tertiary")||tipo.equals("tertiary_link")
						/*||tipo.equals("track")||tipo.equals("unclassified")*/||tipo.equals("trunk")
						||tipo.equals("trunk_link")/*||tipo.equals("road")*/ ){
					Velocidad = 0;
					if( tipo.equals("trunk") )           Velocidad = 100;
					if( tipo.equals("trunk_link") )      Velocidad =  90;
					if( tipo.equals("motorway") )        Velocidad = 120;
					if( tipo.equals("motorway_link") )   Velocidad = 100;
					if( tipo.equals("primary") )         Velocidad = 100;
					if( tipo.equals("primary_link") )    Velocidad =  80;
					if( tipo.equals("secondary") )       Velocidad =  80;
					if( tipo.equals("secondary_link") )  Velocidad =  60;
					if( tipo.equals("tertiary") )        Velocidad =  40; 			
					if( tipo.equals("tertiary_link") )   Velocidad =  30;			
					if( tipo.equals("residential") )     Velocidad =  60;
					if( tipo.equals("living_street") )   Velocidad =  30;
					//if( tipo.equals("pedestrian") )      Velocidad =  30;
					if( tipo.equals("track") )           Velocidad =  20;
					if( tipo.equals("unclassified") )    Velocidad =  30;
					if( tipo.equals("service") )         Velocidad =  40;
					//if( tipo.equals("road") )            Velocidad =  20;
					// Limpiar String para separar coordenadas.
					// Antes : LINESTRING (-70.45 -30.35, -70.23 -30.65, ....)
					// Despues : -70.45 -30.35, -70.23 -30.65, ... 
					w2 = wkt.replace("(", "");
					w3 = w2.replace(")", "");
					w4 = w3.replaceFirst("LINESTRING ","");

					// Separar cada punto.
					// Antes : -70.45 -30.35, -70.23 -30.65, ... 
					// Despues : [-70.45 -30.35] [-70.23 -30.65] [...] 
					puntos = w4.split(",");

					// Se añade cada par de puntos a w5.
					// -70.45 -30.35 -70.23 -30.65 y se separa cada coordenada.
					for (int i=0; i<puntos.length-1; i++){
						w5 = puntos[i]+" "+puntos[i+1];		
						wf = w5.split(" ");

						inicio = Funciones.conv_utm( Double.parseDouble(wf[1]),Double.parseDouble(wf[0]) );
						fin = Funciones.conv_utm( Double.parseDouble(wf[3]),Double.parseDouble(wf[2]) );

						longitud = Funciones.distancia( inicio, fin );

						// Se escribe el id de la calle, sus coordenadas en lat long, direccion, tipo y nombre.
						//line=osm_id+";"+idTramo+";"+nombre+";"+longitud+";"+tipo+";"+inicio[0]+";"+inicio[1]+";"+fin[0]+";"+fin[1]+";"+oneway+";"+Velocidad+"\n";
						line=osm_id+";"+idTramo+";"+nombre+";"+longitud+";"+tipo+";"+ Double.parseDouble(wf[1])+";"+ Double.parseDouble(wf[0])+";"+ Double.parseDouble(wf[3])+";"+ Double.parseDouble(wf[2])+";"+oneway+";"+Velocidad+"\n";
						idTramo++;
						bw.write( line );
						bw.flush( );
					}
				}
			}
			bw.close();
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("Archivo calle.csv generado en la carpeta Output/"+CarpetaSalida);
		System.out.println("-------------------------------------------------------------");
	}

}
