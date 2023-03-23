package LaPrac;

import java.util.ArrayList;

import java.util.Arrays;

import javax.swing.JFrame;

public class Observer implements IObserver {
<<<<<<< HEAD
	IObservable miSujeto;
=======
	ArrayList<IObservable> misSujetos;
>>>>>>> c7658fcbed252e9ffd7a99be76e68e8afeb451bb
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

	private int huso1;
	private int huso2;

	private double signoLongitud = 0.0; // -1 W, +1 E
	private double lambda0 = 0.0;
	private double lambda0rad = 0.0;
	
	MyCanvas canvas;

	MyFrame frame;	
	
	boolean primeraVez = true;
	
	double coordInicialesEste;
	double coordInicialesNorte;
	
	double coordFinalesEste;
	double coordFinalesNorte;
	
	double diferenciaEste;
	double diferenciaNorte;
	
	double ajusteErrorLatitud = 0.0; // Para el canvas, si se hace a ojímetro. -0.5/60
	double ajusteErrorLongitud = 0.0; // Para el canvas, si se hace a ojímetro. 0.0
	
	public Observer(IObservable miSujeto, int huso, String tipo) {
		this.miSujeto = miSujeto;

		this.misSujetos = new ArrayList<>();
		this.misSujetos.add(miSujeto);

		if (tipo.equalsIgnoreCase("Europeo")) {
			calculosIniciales(aEuropeo, fEuropeo, e2Europeo, huso);
		} else {
			calculosIniciales(aWGS84, fWGS84, e2WGS84, huso);
		}
		this.miSujeto.addObserver(this);
		this.canvas = new MyCanvas();
	}
		
	public Observer(IObservable miSujeto, int huso, String tipo, MyCanvas canvas) {
		this.miSujeto = miSujeto;

		this.misSujetos = new ArrayList<>();
		this.misSujetos.add(miSujeto);
		if (tipo.equalsIgnoreCase("Europeo")) {
			calculosIniciales(aEuropeo, fEuropeo, e2Europeo, huso);
		} else {
			calculosIniciales(aWGS84, fWGS84, e2WGS84, huso);
		}
<<<<<<< HEAD
		this.miSujeto.addObserver(this);
		this.canvas = canvas;
	}
	
	public MyCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(MyCanvas canvas) {
		this.canvas = canvas;
	}

	public double getAjusteErrorLatitud() {
		return ajusteErrorLatitud;
	}

	public void setAjusteErrorLatitud(double ajusteErrorLatitud) {
		this.ajusteErrorLatitud = ajusteErrorLatitud;
	}

	public double getAjusteErrorLongitud() {
		return ajusteErrorLongitud;
	}

	public void setAjusteErrorLongitud(double ajusteErrorLongitud) {
		this.ajusteErrorLongitud = ajusteErrorLongitud;
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}

	public Observer(int huso, String tipo, MyFrame frame) {
		if (tipo.equalsIgnoreCase("Europeo")) {
			calculosIniciales(aEuropeo, fEuropeo, e2Europeo, huso);
		} else {
			calculosIniciales(aWGS84, fWGS84, e2WGS84, huso);
		}
		this.misSujetos = new ArrayList<>();
		this.frame = frame;
	}

	public void addObservable(IObservable sujeto)
	{
		sujeto.addObserver(this);
		this.misSujetos.add(sujeto);
	}

	public void calculosIniciales(double a, double f, double e2, int huso) {
		this.a = a;
		this.b = a * (1 -f);
		this.f = f;
		this.e2 = e2;
		this.huso = huso;

		this.huso1 = huso; // Esto está por si acaso a alguien se le olvida mandar husos luego, tomo la misma área por defecto
		this.huso2 = huso;

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
		System.out.println("a " +a+ " e2 " +e2+" phi "+phi );
		double N = a / Math.sqrt(1.0 - e2 * Math.pow(Math.sin(phi), 2));
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
		double elUMT_Norting = 0.9996 * (M + N * Math.tan(phi) * ( Math.pow(A, 2)/2.0 + (5.0 - T + 9* C + 4 * Math.pow(C, 2)) * Math.pow(A, 4)/24.0 + ((61.0 - 58.0 * T + Math.pow(T, 2) + 600.0 * C - 330.0 * e2) * Math.pow(A, 6))/720.0 ) );
		return elUMT_Norting;
	}
	
	public double calcularRho (double a, double e2, double phi) {
		double N = a * (1 - e2) / Math.cbrt(1 - e2 * Math.pow(Math.sin(phi), 2));
		return N;
	}
	
	public double[] deWGS84aED50(double lambda, double phi, double e2, double a) {
		double N = calcularN(a, e2, phi);
		double rho = calcularRho(a, e2, phi);
		double dX = 0.0, dY = 0.0, dZ = 0.0; // Desplazamientos de las elipsoides consultar con profesor
		double da = Observer.aEuropeo - Observer.aWGS84, df = Observer.fEuropeo - Observer.fWGS84; // Variacion en el semieje mayor y aplanamiento de las elipsoides
		double dPhi = (-dX * Math.sin(phi)*Math.cos(lambda) - dY * Math.sin(phi)*Math.cos(lambda) + dZ * Math.cos(phi) + (a * df + f *da) * Math.sin(2 * phi))/(rho * Math.sin(1));
		double dLambda = (-dX * Math.sin(lambda) + dY * Math.cos(lambda))/(N * Math.cos(phi)*Math.cos(1));
		// todos esos cos(1) y sin(1) son 1'', 1 segundo de arco
		return new double[] {dPhi, dLambda};
	} 
	
	public double[] calculosSiguientes(double longitud, double latitud, double altura, String oesteEste, String norteSur, int huso) {
		
		double latitudphi     = ( norteSur.equalsIgnoreCase("S") ? -1 : 1 ) * gradaRadianes(latitud);
		double longitudlambda = ( oesteEste.equalsIgnoreCase("W") ? -1 : 1 ) * gradaRadianes(longitud);
		System.out.println("LatitudPhi: " + latitudphi + " LongitudLambda: " + longitudlambda);
		
		double lambda0 = huso * 6.0 - restaAlSignoLongitud;
		double lambda0rad = gradaRadianes(lambda0);
	
		this.e = calculare(this.e2);

		double N = calcularN(this.a, this.e2, latitudphi);
		double T = calcularT(latitudphi);
		double C = calcularC(this.e, latitudphi);
		double A = calcularA(longitudlambda, lambda0rad, latitudphi); // phi es latitud, lambda es longitud

		double laM = calcularM(this.a, this.e2, latitudphi);

		System.out.println("Lambda0 " + lambda0);
		System.out.println("Lambda0 (radianes) " + lambda0rad);
		System.out.println("e'2 " + this.e2);

		System.out.println("e' " + this.e);
		System.out.println("N " + N);
		System.out.println("T " + T);
		System.out.println("C " + C);
		System.out.println("A " + A);
		System.out.println("M " + laM);
		double elUTM_Easting = calcularUMT_Easting(N, A, T, C, this.e2);
		double elUTM_Norting = calcularUTM_Norting(laM, N, A, T, C, this.e2, latitudphi);
		/* ESTO DE ABAJO NO HACE FALTA HACERLO 
		double[] ajusteaEuropeo = deWGS84aED50(longitud, latitud, this.e2, this.a);
		*/
		double[] result = new double[] {elUTM_Easting, elUTM_Norting};
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
	
	private ArrayList<String> getParsedStringArrayList(String cadena, char delimitador)
	{
		ArrayList<String> parseado = new ArrayList<>(Arrays.asList(cadena.split(",")));
		parseado.set(parseado.size()-1, parseado.get(parseado.size()-1).split("\r")[0]);

		return parseado;
	}
	
#<<<<<<< HEAD
	/*
	 * Tomo el dato de mi Sujeto
	 * Parseo el dato
	 * Realizo las operaciones pertinentes de ajuste de coordenadas
	 * 
	 * */
#	public void actualizar() {
#		String cadena = miSujeto.getMensaje();
#=======
	public void actualizar(int issuer) {
		if(issuer >= this.misSujetos.size())
			return;

		String cadena = misSujetos.get(issuer).getMensaje();
#>>>>>>> c7658fcbed252e9ffd7a99be76e68e8afeb451bb
		
		System.out.println("Cadena GPS: " + cadena);
		ArrayList<String> parseada = getParsedStringArrayList(cadena, ',');
		if(!parseada.get(0).equals("$GPGGA") || parseada.size() != 15)
			return;
		System.out.println("Campos parseo :"+ parseada);

		// Acá queda bien filtrarlo, separarlos por comas, solo nos interesan tramas $GPGGA de momento
		
		// De lo filtrado sacamos esto
		// Ej. $GPGGA,092831.567,4023.413,N,00337.529,W,1,12,1.0,0.0,M,0.0,M,,*7
		//     [ 0   ,     1    ,   [2] ,[3],   [4] ,[5],6, 7, 8,[9],A, B ,C,D,E]
		String oesteEste = parseada.get(5);
		if (oesteEste.length() == 0) {
			oesteEste = "E";
		}
		String norteSur = parseada.get(3);
		if (norteSur.length() == 0) {
			norteSur = "N";
		}
		double valor = 0.0;
		if (parseada.get(4).length() > 0) {
			valor = Double.parseDouble(parseada.get(4));
		}
		double longitud = longitudaGrados(valor);
		
		valor = 0.0;
		if (parseada.get(2).length() > 0) {
			valor = Double.parseDouble(parseada.get(2));
		}
		double latitud = latitudaGrados(valor);

		double altura = 0.0;
		
		if (parseada.get(9).length() > 0)
		    altura = Double.parseDouble(parseada.get(9));
		
		int huso = 30; // No hay que complicarse calculando desde la hora UTC, el profe nos ha dicho que podemos hacerlo así
		
		double[] resultado = calculosSiguientes(longitud, latitud, altura, oesteEste, norteSur, huso);
		
		System.out.println("UTM Norte: " + resultado[1] + "\nUTM Este : " + resultado[0]);
		
		double ajuste = 0.5/60; // TEMP TO-DO QUÍTALO
		if (this.primeraVez) {
			this.primeraVez = false;
			establecimientoCoordIniciales(338.067, "W", 4023.550, "N", 337.300, "W", 4023.033,"N", ajuste, 0.0, this.huso, this.huso);
			
		}
		

		double[] diferenciaDeCoordenadas = {resultado[0] -this.coordInicialesEste, resultado[1] -this.coordInicialesNorte}; // Este, Norte
		
		this.canvas.setFactorCanvasX(this.diferenciaEste);
		this.canvas.setFactorCanvasY(this.diferenciaNorte);
		this.canvas.setX((int) (diferenciaDeCoordenadas[0]));
		this.canvas.setY((int) -(diferenciaDeCoordenadas[1]));
		this.canvas.repaint();
		
		
		
	}
	public void establecimientoCoordIniciales(double longitudInicial, String esteOesteInicial, double latitudInicial, String norteSurInicial, double longitudFinal, String esteOesteFinal, double latitudFinal, String norteSurFinal, double ajusteLatitud, double ajusteLongitud, int husoInicial, int husoFinal) {
		this.setAjusteErrorLatitud(ajusteLatitud);
		this.setAjusteErrorLongitud(ajusteLongitud);
		System.out.println("COORD INICIALES");
		double[] coordIniciales = this.coordenadasInicialesCanvas(longitudInicial, esteOesteInicial, latitudInicial+ajusteErrorLatitud, norteSurInicial+ajusteLongitud, husoInicial);
		this.coordInicialesEste = coordIniciales[0];
		this.coordInicialesNorte = coordIniciales[1];
		this.huso1 = husoInicial;
		System.out.println("COORD FINALES");
		double[] coordFinales = this.coordenadasInicialesCanvas(longitudFinal, esteOesteFinal, latitudFinal+ajusteErrorLatitud, norteSurFinal+ajusteLongitud, husoFinal);
		this.coordFinalesEste = coordFinales[0];
		this.coordFinalesNorte = coordFinales[1];
		this.huso2 = husoFinal;
		double[] DifDeInicialAFinal = {coordFinales[0] -coordIniciales[0], -(coordFinales[1] -coordIniciales[1])}; // Este, Norte
		this.diferenciaEste = DifDeInicialAFinal[0];
		this.diferenciaNorte = DifDeInicialAFinal[1];
		
	}
	
	public void test() { // Esto es para pruebas de cálculos según el ejemplo de errata, debería salir lo mismo
		// Tomo el dato, y lo parseo
		// Luego ajusto con fórmulas
		String cadena = miSujeto.get(0).getMensaje();
		System.out.println("Cadena GPS: " + cadena);
		
		// Acá queda bien filtrarlo, separarlos por comas, solo nos interesan tramas $GPGGA de momento
		
		// De lo filtrado sacamos esto
		String oesteEste = "W";
		String norteSur = "N";
		
		//double longitud = longitudaGrados(337.619); // Lo de la práctica 1, el ejemplo, se ve que funciona
		//double latitud = latitudaGrados(4023.429); 
		
		//double longitud = longitudaGrados(337.7013); // Lo de la práctica 1, el ejemplo, se ve que funciona
		//double latitud = latitudaGrados(4023.3004);
		double ajuste = 0.5/60; // El ajuste es porque España se ve desde el plano ecuatorial y hace que algunas lectura en el eje norte se vean descompesadas casi medio segundo, además de que las coordenadas del mapa se hicieron un poco a ojímetro
		//double ajuste = 0.0;

		double longitud = longitudaGrados(337.9666666); // Coordenadas punto rojo de INSIA
		double latitud = latitudaGrados(4023.16666667);
		
		
		System.out.println("LongGrad: " + longitud + " LatGrad " + latitud);
		
		double altura = 0.0;

		int huso = 30; // No hay que complicarse calculando desde la hora UTC, el profe nos ha dicho que podemos hacerlo asi
		
		double[] resultado = calculosSiguientes(longitud, latitud, altura, oesteEste, norteSur, huso);
		
		System.out.println("UTM Norte: " + resultado[1] + "\nUTM Este : " + resultado[0]);
		
		//this.canvas = new MyCanvas();
		//this.canvas.setImageName("ImagenINSIA.PNG");
		//this.canvas.setImageName("ImagenUPMeINSIA.PNG");

		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.add(this.canvas);
		f.setSize(955, 870);
		f.setLocation(300, 0);
		f.setVisible(true);
		System.out.println("Imagen");
		f.addWindowListener(this.canvas);
		
		this.canvas.setX(huso);
		this.canvas.setY(huso);
		this.canvas.repaint();
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// Según la foto comenzamos en 40º 23' 33'' N 3º 38' 04 '' W, o sea, 4023,55 N y 338,067 W
		// Y terminamos en 40º 23' 02'' N 3º 37' 18 '' W, o sea, 4023,033 N y 337,300 W
		
		// Para el INSIA son 40º 23' 15'' N, 3º 38' 1'' W -> 4023.25 N, 338.0166666666666 W
		// 40º 23' 08'' N, 3º 37' 50'' W -> 4023.1333333333 N, 337.8333333333 W
		//if (this.primeraVez) {
		//	this.primeraVez = false;
			
			//establecimientoCoordIniciales(338.0166666666666, "W", 4023.25, "N", 337.8333333333, "W", 4023.1333333333,"N", ajuste, 0.0, 30, 30);
		//	establecimientoCoordIniciales(338.067, "W", 4023.550, "N", 337.300, "W", 4023.033,"N", ajuste, 0.0, 30, 30);
			
			/*
			System.out.println("COORD INICIALES");
			//double[] coordIniciales = this.coordenadasInicialesCanvas(338.067, "W", 4023.550, "N");
			double[] coordIniciales = this.coordenadasInicialesCanvas(338.0166666666666, "W", 4023.25-ajuste, "N");
			this.coordInicialesEste = coordIniciales[0];
			this.coordInicialesNorte = coordIniciales[1];
			System.out.println("COORD FINALES");
			//double[] coordFinales = this.coordenadasInicialesCanvas(337.300, "W", 4023.033, "N");
			double[] coordFinales = this.coordenadasInicialesCanvas(337.8333333333, "W", 4023.1333333333-ajuste, "N");
			this.coordFinalesEste = coordFinales[0];
			this.coordFinalesNorte = coordFinales[1];
			double[] DifDeInicialAFinal = {coordFinales[0] -coordIniciales[0], -(coordFinales[1] -coordIniciales[1])}; // Este, Norte
			this.diferenciaEste = DifDeInicialAFinal[0];
			this.diferenciaNorte = DifDeInicialAFinal[1];
			*/
			
		//}
		

		double[] diferenciaDeCoordenadas = {resultado[0] -this.coordInicialesEste, resultado[1] -this.coordInicialesNorte}; // Este, Norte
		
		this.canvas.setFactorCanvasX(this.diferenciaEste);
		this.canvas.setFactorCanvasY(this.diferenciaNorte);
		this.canvas.setX((int) (diferenciaDeCoordenadas[0]));
		this.canvas.setY((int) -(diferenciaDeCoordenadas[1]));
		this.canvas.repaint();
		
	}
	

	// Según la foto comenzamos en 40º 23' 33'' N 3º 38' 04 '' W
	public double[] coordenadasInicialesCanvas(double longitudOrig, String oesteEste, double latitudOrig, String norteSur, int mismoHuso) { // Esto es para pruebas de cálculos según el ejemplo de errata, debería salir lo mismo

		// Según la foto comenzamos en 40º 23' 33'' N 3º 38' 04 '' W

		//String oesteEste = "W";
		//String norteSur = "N";
		
		
		double longitud = longitudaGrados(longitudOrig); 
		double latitud = latitudaGrados(latitudOrig);
		
		System.out.println("LongGrad: " + longitud + " LatGrad " + latitud);
		
		double altura = 0.0; // TO-DO

		int huso;
		if (mismoHuso < -60 || mismoHuso > 60) {
			huso = this.huso;
		} else {
			huso = mismoHuso;
		}
		
		double[] resultado = calculosSiguientes(longitud, latitud, altura, oesteEste, norteSur, huso);
		
		System.out.println("UTM Norte: " + resultado[1] + "\nUTM Este : " + resultado[0]);
		
		return resultado;
	}

	
	public static void main(String[] a) {
		Subject losPuertos = new Subject(0);
		
		MyCanvas m = new MyCanvas();
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.add(m);
		f.setSize(955, 870);
		f.setLocation(300, 0);
		f.setVisible(true);
		System.out.println("Imagen");
		f.addWindowListener(m);
		
		Observer calculadora = new Observer(losPuertos, 30, "GPS", m);
		
		
		// Mapa UPM
		double ajuste = 0.25/60.0;
		calculadora.getCanvas().setImageName("ImagenUPMeINSIA.PNG");
		calculadora.establecimientoCoordIniciales(338.067, "W", 4023.550, "N", 337.300, "W", 4023.033,"N", ajuste, 0.0, 30, 30);
		// Mapa INSIA
		//double ajuste = 0.5/60.0;
		//calculadora.getCanvas().setImageName("ImagenINSIA.PNG");
		//calculadora.establecimientoCoordIniciales(338.0166666666666, "W", 4023.25, "N", 337.8333333333, "W", 4023.1333333333,"N", ajuste, 0.0, 30, 30);
		
		calculadora.test();
	}

}
