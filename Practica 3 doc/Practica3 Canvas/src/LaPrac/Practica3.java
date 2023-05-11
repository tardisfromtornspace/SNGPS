package LaPrac;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class Practica3  {
	public static void main(String[] args) {

		Scanner scan;
	    File file = new File("MapaVelocidadesINSIA.txt");
	    try {
	    	System.out.println("Leyendo fichero");
	        scan = new Scanner(file);
	        
	        int cuenta = 0;
	        ArrayList<Double> utmNorte = new ArrayList(50);
	        ArrayList<Double> utmEste = new ArrayList(50);
	        ArrayList<Double> velocidad = new ArrayList(50);
	        while(scan.hasNext()) {
	        	Double lectura = Double.parseDouble(scan.next()); 
	        	System.out.println(lectura);
	        	switch(cuenta) {
	        	case 0: utmNorte.add(lectura);
	        	        break;
	        	case 1: utmEste.add(lectura);
	                    break;
	        	case 2: velocidad.add(lectura);
    	                break;
	        	}
	        	if (cuenta >= 2) {
	        		cuenta = 0;
	        	} else cuenta++;
	        }
	        scan.close();
	        
	        System.out.println("Fin fichero");
	        
	        System.out.println(utmNorte.toString());
	        System.out.println(utmEste.toString());
	        System.out.println(velocidad.toString());
	        
	        System.out.println("Procedo a guardar el mapa");
	        MiMapaInsia mapita = new MiMapaInsia(utmNorte, utmEste, velocidad);
	        System.out.println( mapita.encontrar(4470838.47,  446052.0919));
	        
	        System.out.println( mapita.encontrar(4470838.47002605019,  446052.091949845632));
	       
	        
			Subject losPuertos = new Subject();
			
			MyCanvas m = new MyCanvas();
			JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.add(m);
			f.setSize(955 +200, 870);
			f.setLocation(300, 0);
			f.setVisible(true);
			
			System.out.println("Iniciando sistema");
			
			Observer calculadora = new Observer(losPuertos, 30, "GPS", m);
			
			// Mapa INSIA
			double ajuste = 1.0/60.0;
			calculadora.getCanvas().setImageName("ImagenINSIA.PNG");
			calculadora.establecimientoCoordIniciales(338.0166666666666 + 1.0/60.0, "W", 4023.25, "N", 337.8333333333, "W", 4023.1333333333,"N", ajuste,  0.0, 30, 30);
			calculadora.setPrimeraVez(false);
			calculadora.setMapita(mapita);
			losPuertos.start();

	    } catch (FileNotFoundException e1) {
	            e1.printStackTrace();
	    }

	}
}

