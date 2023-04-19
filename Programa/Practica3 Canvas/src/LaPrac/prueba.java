package LaPrac;

import javax.swing.JFrame;

//import java.util.EventListener;

// TO-DO añadir el plugin de lectura de COM
import com.fazecast.jSerialComm.*;

public class prueba {
	
	public static void main(String[] args) {
		Subject losPuertos = new Subject();
		
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
		//double ajuste = 0.25/60.0;
		//calculadora.getCanvas().setImageName("ImagenUPMeINSIA.PNG");
		//calculadora.establecimientoCoordIniciales(338.067, "W", 4023.550, "N", 337.300, "W", 4023.033,"N", ajuste, 0.0, 30, 30);
		// Mapa INSIA
		double ajuste = 1.0/60.0;
		calculadora.getCanvas().setImageName("ImagenINSIA.PNG");
		calculadora.establecimientoCoordIniciales(338.0166666666666 + 1.0/60.0, "W", 4023.25, "N", 337.8333333333, "W", 4023.1333333333,"N", ajuste,  0.0, 30, 30);
		calculadora.setPrimeraVez(false);
		losPuertos.start();
		
	}
}


