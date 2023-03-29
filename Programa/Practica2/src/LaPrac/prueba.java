package LaPrac;

public class prueba {
	public static void main(String[] args) {
		MyFrame frame = new MyFrame("src/gpsMAP.jpg", "src/point.png");
		Subject miSujeto = new Subject(0);
		Observer calculadora = new Observer(30, "GPS", frame);
		
		calculadora.addObservable(miSujeto);
		miSujeto.leerPuertos();
		miSujeto.start();
	}
}