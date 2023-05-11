package LaPrac;

import java.util.ArrayList;

public class MiMapaInsia {
	Double[] utmNorte;
	Double[] utmEste;
	Double[] velocidad;
	
	public MiMapaInsia(Double[] utmNorte, Double[] utmEste, Double[] velocidad) {
		this.utmNorte = utmNorte;
		this.utmEste = utmEste;
		this.velocidad = velocidad;
	}
	
	public MiMapaInsia(ArrayList<Double> utmNorte, ArrayList<Double> utmEste, ArrayList<Double> velocidad) {
		
		this.utmNorte = utmNorte.toArray(new Double[0]);
		this.utmEste = utmEste.toArray(new Double[0]);
		this.velocidad = velocidad.toArray(new Double[0]);
	}
	
	public double encontrar(double utmNorte, double utmEste) {
		int i;

		for(i = 0; i < this.utmNorte.length; i++) {
			if( this.utmNorte[i] == utmNorte && this.utmEste[i] == utmEste) {
				return velocidad[i];
			}
		}
		
		int indexMasCarcano = -1;
		double distanciaMasCercana = Double.MAX_VALUE;
		double velRec = -1;
		
		for(i = 0; i < this.utmNorte.length; i++) {
			double distanciaActual = Math.sqrt(Math.pow(this.utmNorte[i]-utmNorte, 2) + Math.pow(this.utmEste[i]-utmEste, 2));
			if(distanciaActual < distanciaMasCercana) {
				distanciaMasCercana = distanciaActual;
				velRec = this.velocidad[i];
			}
		}
		
		return velRec;
	}
}
