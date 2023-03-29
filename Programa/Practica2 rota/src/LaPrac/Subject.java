package LaPrac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Subject implements IObservable, Runnable{

	private int id;
	private String mode;

	private ArrayList<IObserver> observers;
	private MessageListener listener;
	private String mensaje = new String();

	public Subject(int id) {
		this.id = id;
		this.observers = new ArrayList<IObserver>();
	}
	
	public ArrayList<IObserver> getObservers() {
		return observers;
	}
	public void setObservers(ArrayList<IObserver> observers) {
		this.observers = observers;
	}
	
	@Override
	public void addObserver(IObserver observador) {
		this.observers.add(observador);
	}
	@Override
	public void removeObserver(IObserver observador) {
		this.observers.remove(observador);
	}
	@Override
	public void notifyObservers() {
		for (Iterator<IObserver> it = observers.iterator(); it.hasNext();) {
			IObserver iObserver = it.next();
			iObserver.actualizar(this.id);
		}
	}
	
	public MessageListener getListener() {
		return listener;
	}

	public void setListener(MessageListener listener) {
		this.listener = listener;
	}
	
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void start() {
		Thread hilo = new Thread(this);
		hilo.start();
	}
#<<<<<<< HEAD
	
	public void leerPuertos() {
#=======
#	public void leerPuertos() {
#		this.mode = "default";
#	}
#	public void leerPuertos(String dir) {
#		this.mode = dir;
#	}
#	
#	private void leerPuertosPriv(String dir) {
#>>>>>>> c7658fcbed252e9ffd7a99be76e68e8afeb451bb
		SerialPort[] comPorts = SerialPort.getCommPorts();
		for (SerialPort s: comPorts) {
			System.out.println("Puerto disponible: " + s);
		}
		
#<<<<<<< HEAD
		// Suponemos que solo hay uno y es el primero
		SerialPort comPort = SerialPort.getCommPorts()[0];
# TO-DO Primero cambia los # por // y luego elimina los que no uses, el modo comentado acá parece mejor pero ve si funciona con el ordenador
#=======
#		SerialPort comPort;
#		// Suponemos que solo hay uno y es el primero
#		if(dir.equals("default"))
#			comPort = SerialPort.getCommPorts()[0];
#		comPort = SerialPort.getCommPort(dir);
#>>>>>>> c7658fcbed252e9ffd7a99be76e68e8afeb451bb
		System.out.println("Puerto usado: " + comPort);
		comPort.openPort();
		comPort.setParity(0);
		comPort.setBaudRate(4800);
		comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
		System.out.println("Puerto abierto: "+ comPort.isOpen());
		
		Subject cosilla = this;
		
        comPort.addDataListener(
				
				new MessageListener() {

		   @Override
		   public void serialEvent(SerialPortEvent event)
		   {
		      byte[] delimitedMessage = event.getReceivedData();
		      
		      StringBuilder mensaje = new StringBuilder();
		      
		      for (int i = 0; i < delimitedMessage.length; ++i) {
			         //System.out.print((char)delimitedMessage[i]);
			         mensaje.append(Character.toString((char)delimitedMessage[i]));
		      }
		      
		      cosilla.setMensaje(mensaje.toString());
		      cosilla.notifyObservers();
		   }
		});
		
	}
	
	public void run() {
		synchronized (this) {
#<<<<<<< HEAD
			leerPuertos();
#=======
#			leerPuertosPriv(this.mode);
#>>>>>>> c7658fcbed252e9ffd7a99be76e68e8afeb451bb
		}
	}

	@Override
	public String toString() {
		return "Subject [observers=" + observers + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(observers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		return Objects.equals(observers, other.observers);
	}

}
