package LaPrac;

public class Prueba {
	public static void main(String[] args) {
		MyFrame frame = new MyFrame("src/gpsMAP.jpg", "src/pointt.png", "src/vel_ind.png");
		Subject miSujeto = new Subject(0);
		Observer calculadora = new Observer(30, "GPS", frame, 1e-8d);
		calculadora.establecimientoCoordIniciales(338.083, "W", 4023.500, "N", 337.417, "W", 4023.100,"N", 0.0, 0.0, 30, 30);
		
		frame.addRotablePoint(255,0,0);
		calculadora.addObservable(miSujeto);
		miSujeto.leerPuertos();
		miSujeto.start();
		calculadora.startSpeedTracer();
	}
}