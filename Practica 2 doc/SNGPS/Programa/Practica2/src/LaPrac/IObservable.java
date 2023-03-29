package LaPrac;

public interface IObservable {

	void addObserver(IObserver observador);

	void removeObserver(IObserver observador);

	void notifyObservers();
	
	public String getMensaje();

}