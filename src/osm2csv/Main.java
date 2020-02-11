package osm2csv;

import osm2csv.util.InOut;

public class Main {
	public static void main(String args[]) {
		InOut inout = new InOut();
		inout.LecturaCallesOSM("./Input/chile.csv","Chile","Calles_Chile.csv");
	}
}
