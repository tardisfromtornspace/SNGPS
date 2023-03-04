package LaPrac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Subject implements IObservable, Runnable {
	private ArrayList<IObserver> observers;
	private String notifyMessage;
	private MessageListener listener;
	
	public Subject() {
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
	public void notifyObservers(int notifyObject) {
		if(notifyObject != 1)
			return;
		for(IObserver io:this.observers)
			io.actualizar();
	}
	
	public void start() {
		Thread hilo = new Thread(this);
		hilo.start();
	}
	public void setNotifyMessage(String notifyMessage) {
		this.notifyMessage = notifyMessage;
	}
	public String getNotifyMessage() {
		return this.notifyMessage;
	}
	public void setMessageListener(MessageListener listener) {
		this.listener = listener;
	}
	
	public void mainActivity() {
		//SerialPort comPorts = SerialPort.getCommPort("/dev/pts/9"); // TO-DO ver si es el 3?
		/*System.out.println(SerialPort.getCommPorts().length);
		for (SerialPort s: comPorts) {
			System.out.println("Puerto disponible: " + s);
			// TO-DO verificar que es el del GPS
		}*/
		
		// Suponemos que solo hay uno y es el primero
		
		SerialPort comPort = SerialPort.getCommPort("/dev/pts/7");
		/*if(SerialPort.getCommPorts().length != 0) {
			comPort = SerialPort.getCommPorts()[0];*/
		System.out.println("Puerto usado: " + comPort);
		comPort.openPort();
		comPort.setParity(0);
		comPort.setBaudRate(4800);
		comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);

		System.out.println("Puerto abierto: "+ comPort.isOpen());
		comPort.addDataListener(listener);
	}
	
	public void run() {
		synchronized (this) {
			mainActivity();
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
