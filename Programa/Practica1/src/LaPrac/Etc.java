package LaPrac;

import com.fazecast.jSerialComm.*;
import java.util.EventListener;

public class Etc {

	static public void main(String[] args)
	{
	   SerialPort comPort = SerialPort.getCommPort("/dev/pts/13");
	   comPort.openPort();
	   MessageListener listener = new MessageListener();
	   while(true) {
		   comPort.addDataListener(listener);
		   try { Thread.sleep(5000); } catch (Exception e) { e.printStackTrace(); }
	   }
	// Ac� queda bien filtrarlo, separarlos por comas, solo nos interesan tramas $GPGGA de momentosss
		
			// De lo filtrado sacamos esto
			/*String oesteEste = parseada.get(5);
			String norteSur = parseada.get(3);
			
			
			double longitud = longitudaGrados(Double.parseDouble(parseada.get(4))); // TO-DO
			double latitud = latitudaGrados(Double.parseDouble(parseada.get(2)));  // TO-DO
			double altura = Double.parseDouble(parseada.get(9)); // TO-DO
			int huso = 30;*/ // TO-DO calcular con hora UTC
	}
}
