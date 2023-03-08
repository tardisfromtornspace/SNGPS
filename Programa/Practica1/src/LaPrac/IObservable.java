package LaPrac;

public interface IObservable {

	void addObserver(IObserver observador);

	void removeObserver(IObserver observador);
//
	void notifyObservers(int notifyObject);

	void setNotifyMessage(String mensaje);
	String getNotifyMessage();

}