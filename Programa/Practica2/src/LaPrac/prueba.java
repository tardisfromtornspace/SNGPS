package LaPrac;

import java.util.EventListener;

// TO-DO a�adir el plugin de lectura de COM
import com.fazecast.jSerialComm.*;

public class prueba {
	// TO-DO A�ADIR PATRON OBSERVER al puerto serie, adem�s hacer esto bien con Maven quiz�
	
	public static void main(String[] args) {
		Subject losPuertos = new Subject();
		Observer calculadora = new Observer(losPuertos, 30, "Europeo");
		losPuertos.start(); // Ver si esto ocasiona problema
		
	}
}


