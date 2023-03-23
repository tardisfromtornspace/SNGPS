package LaPrac;

import java.util.EventListener;

// TO-DO añadir el plugin de lectura de COM
import com.fazecast.jSerialComm.*;

public class prueba {
	// TO-DO AÑADIR PATRON OBSERVER al puerto serie, además hacer esto bien con Maven quizá
	
	public static void main(String[] args) {
		Subject losPuertos = new Subject();
		Observer calculadora = new Observer(losPuertos, 30, "Europeo");
		losPuertos.start(); // Ver si esto ocasiona problema
		
	}
}


