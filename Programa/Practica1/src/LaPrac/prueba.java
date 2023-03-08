package LaPrac;

import java.util.EventListener;

// TO-DO a�adir el plugin de lectura de COM
import com.fazecast.jSerialComm.*;

public class prueba {
	// TO-DO A�ADIR PATRON OBSERVER al puerto serie, adem�s hacer esto bien con Maven quiz�
	public static MessageListener getDefaultListener(Subject instance) {
		return new MessageListener() {
			@Override
			   public void serialEvent(SerialPortEvent event) {
			      byte[] delimitedMessage = event.getReceivedData();
			      StringBuilder mensajeSerie = new StringBuilder();
			      
			      for(byte i:delimitedMessage)
			    	  // System.out.print((char)i);
			    	  mensajeSerie.append(Character.toString((char)i));
			      
			      instance.setNotifyMessage(mensajeSerie.toString());
			      // El identificador de la razon u objeto de la llamada a actualizacion esta notificado cuando tiene lugar el evento
			      // en este caso por ej. 0x1 sera llamada por recepcion de trama, en otro caso se descarta dicha llamadas
			      instance.notifyObservers(1);
			   }
		};
	}
	
	public static void main(String[] args) {
		Subject losPuertos = new Subject();
		losPuertos.setMessageListener(getDefaultListener(losPuertos));
		Observer calculadora = new Observer(losPuertos, 30, "Europeo");
		losPuertos.start(); // Ver si esto ocasiona problema
		
	}
}


