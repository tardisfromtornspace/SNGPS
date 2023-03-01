package LaPrac;

public class Observer implements IObserver {
	IObservable miSujeto;
	public static double aEuropeo  = 6378388.0;
	public static double aWGS84    = 6378137.0;
	
	public static double fEuropeo  = 1.0/297.0;
	public static double fWGS84    = 1.0/298.25722;
	
	public static double e2Europeo = 0.00672267;
	public static double e2WGS84   = 0.00669437999013;
	
	public static double cEuropeo  = 6399936.608;
	public static double cWGS84    = 6399593.626;
	
	public static double restaAlSignoLongitud = 183.0;
	
	private double a;
	private double f;
	private double b;
	private double e;
	private double e2;
	private int huso;
	private double signoLongitud = 0.0; // -1 W, +1 E
	private double lambda0 = 0.0;
	private double lambda0rad = 0.0;
	
	public Observer(IObservable miSujeto, int huso, String tipo) {
		this.miSujeto = miSujeto;
		if (tipo.equalsIgnoreCase("Europeo")) {
			calculosIniciales(aEuropeo, fEuropeo, e2Europeo, huso);
		} else {
			calculosIniciales(aWGS84, fWGS84, e2WGS84, huso);
		}
		this.miSujeto.addObserver(this);
	}
	
	public void calculosIniciales(double a, double f, double e2, int huso) {
		this.a = a;
		this.b = a * (1 -f);
		this.f = f;
		this.e2 = e2;
		this.huso = huso;
		this.lambda0 = huso * 6.0 - restaAlSignoLongitud;
		this.lambda0rad = gradaRadianes(this.lambda0);
	}
	
	public double calcularf(double a, double b) {
		double f =  (a - b)/ a;
		return f;
	}
	
	public double calculare2(double a, double b) {
		double e2 = Math.pow(a, 2) - Math.pow(b, 2)/Math.pow(b, 2);
		return e2;
	}
	
	public double calcularc(double a, double b) {
		double c = Math.pow(a, 2) / b;
		return c;
	}
	
	public double gradaRadianes(double valor) {
		return valor * Math.PI / 180.0;
	}
	
	public double calculare(double e2) {
		return e2 / (1.0 -e2);
	}
	
	public double calcularN (double a, double e2, double phi) {
		double N = a / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
		return N;
	}
	
	public double calcularT (double phi) {
		return Math.pow(Math.tan(phi), 2);
	}
	
	public double calcularC (double e, double phi) {
		return e * Math.pow(Math.cos(phi), 2);
	}
	
	public double calcularA (double lambda, double lambda0, double phi) {
		return Math.cos(phi) * (lambda - lambda0);
	}	
	
	public double calcularM(double a, double e2, double phi) {
		double laM = a * ( (1 - e2/4.0 - 3.0/64.0 * Math.pow(e2, 2) - 5.0/256.0 * Math.pow(e2, 3)) * phi - (3.0/8.0 * e2 + 3.0/32.0 * Math.pow(e2, 2) + 45.0/1024.0 * Math.pow(e2, 3)) * Math.sin(2 * phi) + (15.0/256.0 * Math.pow(e2, 2) +45.0/1024.0 * Math.pow(e2, 3)) * Math.sin(4 * phi) -( 35.0/3072.0 * Math.pow(e2, 3))* Math.sin(6 * phi) );
		return laM;
	}
	
	public double calcularUMT_Easting(double N, double A, double T, double C, double e2) {
		double elUMT_Easting = 0.9996 * N * (A +  ((1-T+C) * Math.pow(A, 3))/6.0 + (5.0 - 18.0 * T + Math.pow(T, 2) + 72.0 * C -58.0 * e2) * Math.pow(A, 5)/120.0) + 500000;
		return elUMT_Easting;
	}
	
	public double calcularUTM_Norting(double M, double N, double A, double T, double C, double e2, double phi) {
		double elUMT_Norting = 0.9996 * (M * N * Math.tan(phi) * ( Math.pow(A, 2)/2.0 + (5.0 - T + 9* C + 4 * Math.pow(C, 2)) * Math.pow(A, 4)/24.0 + ((61.0 - 58.0 * T + Math.pow(T, 2) + 600.0 * C - 330.0 * e2) * Math.pow(A, 6))/720.0 ) );
		return elUMT_Norting;
	}
	
	public double[] calculosSiguientes(double longitud, double latitud, double altura, String oesteEste, String norteSur, int huso) {
		
		double latitudphi     = ( oesteEste.equalsIgnoreCase("W") ? -1 : 1 ) * gradaRadianes(latitud);
		double longitudlambda = ( norteSur.equalsIgnoreCase("S") ? -1 : 1 ) * gradaRadianes(longitud);
		
		this.lambda0 = huso * 6.0 - restaAlSignoLongitud;
		this.lambda0rad = gradaRadianes(this.lambda0);
		this.e = calculare(this.e2);
		double N = calcularN(this.a, this.e2, this.lambda0rad);
		double T = calcularT(lambda0rad);
		double C = calcularC(this.e, lambda0rad);
		double A = calcularA(longitudlambda, lambda0rad, latitudphi); // phi es latitud, lambda es longitud
		double laM = calcularM(this.a, this.e2, latitudphi) ;
		double elUMT_Easting = calcularUMT_Easting(N, A, T, C, this.e2);
		double elUMT_Norting = calcularUTM_Norting(laM, N, A, T, C, this.e2, latitudphi);
		
		double[] result = new double[] {elUMT_Easting, elUMT_Norting};
		return result;
	}
	
	public double longitudaGrados(double valor) {
		double grados = Math.floor(valor/100);
		double minutos = valor - grados * 100;
		return grados + minutos/60.0;
	}
	
	public double latitudaGrados(double valor) {
		double grados = Math.floor(valor/100);
		double minutos = valor - grados * 100;
		return grados + minutos/60.0;
	}
	
	public void actualizar() {
		// Tomo el dato, y lo parseo
		// Luego ajusto con f�rmulas
		String oesteEste = "";
		String norteSur = "";
		
		
		double longitud = longitudaGrados(0.0); // TO-DO
		double latitud = latitudaGrados(0.0);  // TO-DO
		double altura = 0.0; // TO-DO
		int huso = 30; // TO-DO calcular con hora UTC
		
		calculosSiguientes(longitud, latitud, altura, oesteEste, norteSur, huso);
		
		
		
	}

}