package osm2csv.util;
import java.io.*;
import java.util.*;
import java.awt.geom.*;
import geotransform.coords.Gdc_Coord_3d;
import geotransform.coords.Utm_Coord_3d;
import geotransform.transforms.Gdc_To_Utm_Converter;
import geotransform.transforms.Utm_To_Gdc_Converter;
import geotransform.ellipsoids.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;



public final class Funciones{
	public static double[ ] conv_utm( Double latitud, Double longitud ){
		double[] utmXY = new double[2];
		Gdc_Coord_3d puntoLatLon = new Gdc_Coord_3d( latitud, longitud, 100.0 );
		Utm_Coord_3d puntoUtm    = new Utm_Coord_3d( );

		Gdc_To_Utm_Converter.Init( new WE_Ellipsoid( ) );
		Gdc_To_Utm_Converter.Convert( puntoLatLon, puntoUtm );

		//System.out.print( "X: " + puntoUtm.x );
		//System.out.println( " Y: " + puntoUtm.y );

		utmXY[0] = puntoUtm.x;
		utmXY[1] = puntoUtm.y;

		return utmXY;
	}

	public static double distancia( double[] inicio, double[] fin ){
		double distancia = 0;
		distancia = sqrt( pow( inicio[0] - fin[0] , 2) +
				pow( inicio[1] - fin[1], 2) );
		return distancia;
	}

}