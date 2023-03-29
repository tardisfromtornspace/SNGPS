package LaPrac;

import javax.swing.JFrame;

/*
 * Esta es la prueba con el Canvas
 */
public class Prueba2 {
	public static void main(String[] args) {
		/* ODIO COMO EL WINDOWS DECIDE SIMPLEMENTE SOBREESCRIBIR PRUEBA EN UN ÚNICO ARCHIVO SIN NADA :( */
		Subject losPuertos = new Subject(0);
		
		MyCanvas m = new MyCanvas();
		//m.setImageName("ImagenINSIA.PNG");
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955, 870);
		f.setLocation(300, 0);
		f.setVisible(true);
		
		System.out.println("Iniciando sistema");
		
		Observer calculadora = new Observer(losPuertos, 30, "GPS", m);
		
		// Mapa UPM
		double ajuste = 0.25/60.0;
		calculadora.getCanvas().setImageName("ImagenUPMeINSIA.PNG");
		calculadora.establecimientoCoordIniciales(338.067, "W", 4023.550, "N", 337.300, "W", 4023.033,"N", ajuste, 0.0, 30, 30);
		// Mapa INSIA
		//double ajuste = 0.5/60.0;
		//calculadora.getCanvas().setImageName("ImagenINSIA.PNG");
		//calculadora.establecimientoCoordIniciales(338.0166666666666, "W", 4023.25, "N", 337.8333333333, "W", 4023.1333333333,"N", ajuste, 0.0, 30, 30);
		losPuertos.start();
	}
}