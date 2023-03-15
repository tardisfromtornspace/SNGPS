package LaPrac;

import javax.swing.JFrame;

//import java.util.EventListener;

// TO-DO añadir el plugin de lectura de COM
import com.fazecast.jSerialComm.*;

public class prueba {
	
	public static void main(String[] args) {
		Subject losPuertos = new Subject();
		
		MyCanvas m = new MyCanvas();
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955, 870);
		f.setLocation(300, 0);
		f.setVisible(true);
		
		Observer calculadora = new Observer(losPuertos, 30, "GPS", m);
		losPuertos.start();
		
	}
}


