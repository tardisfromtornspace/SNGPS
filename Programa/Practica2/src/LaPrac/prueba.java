package LaPrac;

/*
 * Esta es la prueba con el JFrame Draggable
 */
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