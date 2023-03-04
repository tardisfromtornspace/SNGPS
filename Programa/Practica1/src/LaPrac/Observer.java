package LaPrac;

import java.util.ArrayList;

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
	
	public double calcularRho (double a, double e2, double phi) {
		double N = a * (1 - e2) / Math.cbrt(1 - e2 * Math.pow(Math.sin(phi), 2));
		return N;
	}
	
	public double[] deWGS84aED50(double lambda, double phi, double e2, double a) {
		// TO-DO preguntar el profe sobre esto
		double N = calcularN(a, e2, phi);
		double rho = calcularRho(a, e2, phi);
		double dX = 0.0, dY = 0.0, dZ = 0.0; // TO-DO desplazamientos de las elipsoides consultar con profesor
		double da = Observer.aEuropeo - Observer.aWGS84, df = Observer.fEuropeo - Observer.fWGS84; // Variacion en el semieje mayor y aplanamiento de las elipsoides TO-DO consultar con profe
		double dPhi = (-dX * Math.sin(phi)*Math.cos(lambda) - dY * Math.sin(phi)*Math.cos(lambda) + dZ * Math.cos(phi) + (a * df + f *da) * Math.sin(2 * phi))/(rho * Math.sin(1));
		double dLambda = (-dX * Math.sin(lambda) + dY * Math.cos(lambda))/(N * Math.cos(phi)*Math.cos(1));
		// TO-DO , todos esos cos(1) y sin(1) son 1'', 1 segundo de arco TO-DO revisar
		return new double[] {dPhi, dLambda};
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
		double elUTM_Easting = calcularUMT_Easting(N, A, T, C, this.e2);
		double elUTM_Norting = calcularUTM_Norting(laM, N, A, T, C, this.e2, latitudphi);
		// TO-DO preguntar al profe si debemos hacer esto tambi�n Y PASARLO A EUROPEAS
		double[] ajusteaEuropeo = deWGS84aED50(longitud, latitud, this.e2, this.a);
		
		double[] result = new double[] {elUTM_Easting, elUTM_Norting};
		return result;
	}
	
	public double longitudaGrados(double valor) {
		double grados = Math.floor(valor/100.d);
		double minutos = valor - grados * 100;
		return grados + minutos/60.0;
	}
	
	public double latitudaGrados(double valor) {
		double grados = Math.floor(valor/100.d);
		double minutos = valor - grados * 100;
		return grados + minutos/60.0;
	}
	private ArrayList<String> getParsedStringArrayList(String cadena, char delimitador)
	{
		ArrayList<String> parseado = new ArrayList<>();
		StringBuilder n = new StringBuilder();
		for(char i:cadena.toCharArray()) {
			if(i == delimitador) {
				parseado.add(n.toString());
				n = new StringBuilder();
			} else {
				n.append(i);
			}
		}
			
		return parseado;
	}
	public void actualizar() {
		// Tomo el dato, y lo parseo
		// Luego ajusto con f�rmulas
		String cadena = miSujeto.getNotifyMessage();
		ArrayList<String> parseada = getParsedStringArrayList(cadena, ',');
		for(int i = 0; i < parseada.size(); i++) {
			// Por ahora unicamente tramas GPGGA
			if(!parseada.get(i).equals("$GPGGA") && (i == 0))
				return;
		}
		
		System.out.print("Entrada GPS (Solo tratar GPGGA):\n"+ miSujeto.getNotifyMessage());
		System.out.println("Campos parseo :"+ parseada);
		// Ac� queda bien filtrarlo, separarlos por comas, solo nos interesan tramas $GPGGA de momento
		
		// De lo filtrado sacamos esto
		// Ej. $GPGGA,092831.567,4023.413,N,00337.529,W,1,12,1.0,0.0,M,0.0,M,,*7
		//     [ 0   ,     1    ,   [2] ,[3],   [4] ,[5],6, 7, 8,[9],A, B ,C,D,E]
		String oesteEste = parseada.get(5);
		String norteSur = parseada.get(3);
		double longitud = longitudaGrados(Double.parseDouble(parseada.get(4)));
		double latitud = latitudaGrados(Double.parseDouble(parseada.get(2)));
		double altura = Double.parseDouble(parseada.get(9));
		int huso = 30; // TO-DO calcular con hora UTC
		
		double[] resultado = calculosSiguientes(longitud, latitud, altura, oesteEste, norteSur, huso);
		
		System.out.println("UTM Norte: " + resultado[1] + "\nUTM Este: " + resultado[0] +"\n");
	}

}
