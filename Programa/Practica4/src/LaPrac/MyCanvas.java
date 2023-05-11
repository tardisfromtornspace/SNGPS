package LaPrac;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

public class MyCanvas /*extends Canvas*/ implements WindowListener{
	
	private int x = 0;
	private int y = 0;
	
	private String imageName = "ImagenUPMeINSIA.PNG"; // Imagen por defecto
	private int imageHeight = 0;
	private int imageWidth = 0;
	
	private double factorCanvasX = 1.0;
	private double factorCanvasY = 1.0;
	
	private double latitudGrados = 0.0; //40.36005333333333; //40.3866;
	private double longitudGrados = 0.0; //-3.5510916666666663; //-3.6319; 
	
	private Color miColor = Color.GREEN;
	
	private String velocidadTexto = "TEST km/h";
	
	private String velocidadMapa = "0.0";
	
	private String UTMTexto = "XXXX";
	
	private String latText = "Latitud (grados)";
	private String lonText = "Longitud (grados)";
	private String coordGeoText = "COORDENADAS GEOGRAFICAS";
	private String[] autores = {
			"Alejandro Serrano López, bq0100",
			"Diego Torres Aranha, bq0383",
			"Gabriel Gil García, bq0162",
			"Juan José Urioste, bt0492",
			"Miguel Laredo Barbadillo, br0449",
			"Iván Lumbano Vivar, br0097"
			};
	
	private String autorText = "AUTORES";
	
	private double angulo = Math.PI;
	
	private double anchoAlto = 950; // Número de píxeles de ancho por alto
	
	// Map resolution = 156543.04 meters/pixel * cos(latitude) / (2 ^ zoomlevel)
	private double zoomLevel = 18;
	
	private double offset = 0;
	
	private Image[][] mapa = {
			{null, null, null},
			{null, null, null},
			{null, null, null}
			};
	
	/*
	 *            No olvidemos que desplazamiento[0] positivo implica que la imagen debe moverse hacia el Sur y desplazamieno[1] positivo es irse al este
	 * 00 01 02
	 * 10 11 12   12 es el centro
	 * 20 21 22
	 */
	
	private double[] desplazamiento = {0.0, 0.0}; // Norte  y Este
	private double pixelMetro = 1.0;
	
	private boolean primeraVez = true;
	DrawingPanel panel = new DrawingPanel(955 + 200, 950);
	
	public void MyCanvas() {
		this.panel = new DrawingPanel(950 + 180, 950);
	}
	
	public String getVelocidadMapa() {
		return velocidadMapa;
	}



	public void setVelocidadMapa(String velocidadMapa) {
		this.velocidadMapa = velocidadMapa;
	}

	public void setVelocidadMapa(double velocidadMapa) {
		this.velocidadMapa = String.valueOf((int)velocidadMapa);
	}

	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}

	public double getLatitudGrados() {
		return latitudGrados;
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public double resolucionMapa() {
		return (156543.04  * Math.cos(latitudGrados) / (Math.pow(2, this.zoomLevel) ))* (1/1.1); // Según BingMaps * ajuste
		// Objetivo: sabiendo la resolucion, si movemos el mapa arriba debe moverlo adecuadamente
	}

	public void addDesplazamiento(double[] desplazamiento) {
		this.desplazamiento[0] += desplazamiento[0];
		this.desplazamiento[1] += desplazamiento[1];
	}

	public void setLatitudGrados(double latitudGrados) {
		this.latitudGrados = latitudGrados;
	}

	public double getLongitudGrados() {
		return longitudGrados;
	}

	public void setLongitudGrados(double longitudGrados) {
		this.longitudGrados = longitudGrados;
	}

	public double getAngulo() {
		return angulo;
	}

	public void setAngulo(double angulo) {
		this.angulo = angulo%(2*Math.PI);
	}

	public Color getMiColor() {
		return miColor;
	}

	public void setMiColor(Color miColor) {
		this.miColor = miColor;
	}

	public String getVelocidadTexto() {
		return velocidadTexto;
	}

	public void setVelocidadTexto(String velocidadTexto) {
		this.velocidadTexto = velocidadTexto;
	}

	public int getX() {
		return x;
	}

	public void setX(double x) {
		this.x = (int) (x * factorCanvasX);
	}

	public int getY() {
		return y;
	}

	public void setY(double y) {
		this.y = (int) (y * factorCanvasY);
	}

	public String getUTMTexto() {
		return UTMTexto;
	}

	public void setUTMTexto(String uTMTexto) {
		UTMTexto = uTMTexto;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public double getFactorCanvasX() {
		return factorCanvasX;
	}

	public void setFactorCanvasX(double escalaOriginalX) {
		this.factorCanvasX = imageWidth/escalaOriginalX;
	}

	public double getFactorCanvasY() {
		return factorCanvasY;
	}

	public void setFactorCanvasY(double escalaOriginalY) {
		this.factorCanvasY = imageHeight/escalaOriginalY;
	}
	
	public void repaint() {
		this.paint();
	}
	public void paint() {
		
		Graphics g = this.panel.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		
		
		if(this.mapa[1][1] == null && this.primeraVez == false) { // Si la imagen central no está o no nos han mandado pintar antes, pues la cargamos

			try {
				//System.out.println("CARGANDO COORDENADAS");
				
				//this.mapa[1][1] = t.getImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/40.3866,-3.6319?mapSize=1900,1900&zoomlevel=18&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
				// La de abajo aprece funcionar, pero si pongo el zoom como parámetro se rompe
				
				if (mapa[1][1] != null) this.mapa[1][1].flush();
				Toolkit t = Toolkit.getDefaultToolkit();
				
				String url = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=18&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt";
				Image temp;
				
				temp = t.createImage(new URL(url));
				//System.out.println("Pidiendo imagen de "+url);
				this.mapa[1][1] = temp;
				this.desplazamiento[0] = 0.0;
				this.desplazamiento[1] = 0.0;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    pixelMetro = resolucionMapa();
		} else {
			System.out.println("EL CENTRAL NO ES NULL");
		}
		
		//System.out.println("Mi desplazamiento es"+(desplazamiento[0] * pixelMetro));
		
		//Toolkit t = Toolkit.getDefaultToolkit();
		
		if (desplazamiento[0] / pixelMetro +offset < -anchoAlto/2 || desplazamiento[0] / pixelMetro -offset > anchoAlto/2 || desplazamiento[1] / pixelMetro -offset < -anchoAlto/2 || desplazamiento[1] / pixelMetro +offset > anchoAlto/2) {
			try {
				Toolkit t = Toolkit.getDefaultToolkit();
				AffineTransform atB2o = AffineTransform.getTranslateInstance(0, 0);
				atB2o.scale(2, 2);
				atB2o.translate((this.desplazamiento[1]/pixelMetro -this.anchoAlto/2), (-this.desplazamiento[0]/pixelMetro -this.anchoAlto/2));
				g2d.drawImage(this.mapa[1][1], atB2o, null);
				//g.drawImage(this.mapa[1][1], (int)(this.desplazamiento[1]/pixelMetro -this.anchoAlto/2), (int)(-this.desplazamiento[0]/pixelMetro -this.anchoAlto/2), null);
				this.mapa[1][1] = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=18&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			pixelMetro = resolucionMapa();
			desplazamiento[0] = 0.0; // Reseteamos el desplazamiento N
			desplazamiento[1] = 0.0;
			System.out.println("Cambio contexto");
		}
		
		AffineTransform atB2 = AffineTransform.getTranslateInstance(0, 0);
		atB2.scale(2, 2);
		atB2.translate((this.desplazamiento[1]/pixelMetro -this.anchoAlto/2), (-this.desplazamiento[0]/pixelMetro -this.anchoAlto/2));
		//atB2.rotate(this.angulo,this.mapa[1][1].getWidth(null) / 2, this.mapa[1][1].getHeight(null) / 2);

        if (this.mapa[1][1] != null && this.mapa[1][1].getWidth(null) != -1) { // Si la imagen está apropiadamente cargada
        	g2d.drawImage(this.mapa[1][1], atB2, null);
        	//g.drawImage(this.mapa[1][1], (int)(this.desplazamiento[1]/pixelMetro -this.anchoAlto/2), (int)(-this.desplazamiento[0]/pixelMetro -this.anchoAlto/2), null);
        }
        
		// Flecha
        Toolkit t = Toolkit.getDefaultToolkit();
		Image img = t.getImage("pointIII.png");
        
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.translate(this.anchoAlto / 2 +40, this.anchoAlto / 2 + 20);
		at.rotate(this.angulo,img.getWidth(null) / 2, img.getHeight(null) / 2);
		
		g2d.drawImage(img, at, null);
        
		// UI
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int)this.anchoAlto, 0, 230, (int)this.anchoAlto);
        g.fillRect(0, 0, 40, (int)this.anchoAlto);
        g.fillRect(0, 0, (int)this.anchoAlto, 40);
        g.fillRect(0, (int)this.anchoAlto-40, (int)this.anchoAlto, 40);
        
        g.setColor(Color.BLACK);
        g.fillRect((int)this.anchoAlto, 130, 230, 10);
        g.fillRect((int)this.anchoAlto, 170, 230, 10);
        
        g.fillRect((int)this.anchoAlto, 270, 230, 10);
        g.fillRect((int)this.anchoAlto, 310, 230, 10);
        
        g.setColor(miColor);
        g.fillRect((int)this.anchoAlto + 10, 15, 140 + 50, 40);
        
        if (miColor == Color.RED) t.beep();
        
        g.setColor(Color.red);  
		g.fillOval((int)this.anchoAlto +30, 55, 70, 70);
		g.setColor(Color.white);  
		g.fillOval((int)this.anchoAlto +30 + 10, 55 + 10, 50, 50);
        
		
				
        g.setColor(Color.black); 
        Font fuente = g.getFont();
        Font fuente2 = fuente.deriveFont(200);
        g.setFont(fuente2);
        g.drawChars(velocidadTexto.toCharArray(), 0, velocidadTexto.length(), (int)(this.anchoAlto) + 15, 40);
        g.drawChars(velocidadMapa.toCharArray(), 0, velocidadMapa.length(), (int)(this.anchoAlto) + 50, 95);
        g.drawChars(UTMTexto.toCharArray(), 0, UTMTexto.length(), 40, 30);
        String lat = String.valueOf(this.latitudGrados);
        String lon = String.valueOf(this.longitudGrados);
        
        g.drawChars(coordGeoText.toCharArray(), 0, coordGeoText.length(), (int)(this.anchoAlto) + 15, 160);
        g.drawChars(latText.toCharArray(), 0, latText.length(), (int)(this.anchoAlto) + 15, 200);
        
        g.drawChars(lat.toCharArray(), 0, lat.length(), (int)(this.anchoAlto) + 15, 220);
        
        g.drawChars(lonText.toCharArray(), 0, lonText.length(), (int)(this.anchoAlto) + 15, 240);
        g.drawChars(lon.toCharArray(), 0, lon.length(), (int)(this.anchoAlto) + 15, 260);
        
        g.drawChars(autorText.toCharArray(), 0, autorText.length(), (int)(this.anchoAlto) + 15, 300);
        
        for (int i = 0; i < autores.length; i++) {
        	g.drawChars(autores[i].toCharArray(), 0, autores[i].length(), (int)(this.anchoAlto) + 15, 340 + 15 * i);
        }
        
        
        //System.out.println("Primera vez = "+this.primeraVez);
        panel.copyGraphicsToScreen();
	}
	
	public static void main(String[] losArgumentos) {
		MyCanvas m = new MyCanvas();
		m.setImageName("ImagenINSIA.PNG");
		m.paint();
		/*JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955 + 200, 950);
		f.setLocation(300, 0);
		f.setVisible(true);
		System.out.println("Imagen");
		f.addWindowListener(m);*/
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana abierta");
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana cerrandose, cerrando programa");
		System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana cerrada, cerrando programa");
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

/*
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

public class MyCanvasOld extends Canvas implements WindowListener{
	
	private int x = 0;
	private int y = 0;
	
	private String imageName = "ImagenUPMeINSIA.PNG"; // Imagen por defecto
	private int imageHeight = 0;
	private int imageWidth = 0;
	
	private double factorCanvasX = 1.0;
	private double factorCanvasY = 1.0;
	
	private double latitudGrados = 0.0; //40.36005333333333; //40.3866;
	private double longitudGrados = 0.0; //-3.5510916666666663; //-3.6319; 
	
	private Color miColor = Color.GREEN;
	
	private String velocidadTexto = "TEST km/h";
	
	private String UTMTexto = "XXXX";
	
	private double angulo = Math.PI;
	
	private double anchoAlto = 950; // Número de píxeles de ancho por alto
	
	// Map resolution = 156543.04 meters/pixel * cos(latitude) / (2 ^ zoomlevel)
	private double zoomLevel = 20;
	
	private Image[][] mapa = {
			{null, null, null},
			{null, null, null},
			{null, null, null}
			};
	
	/ *
	 *            No olvidemos que desplazamiento[0] positivo implica que la imagen debe moverse hacia el Sur y desplazamieno[1] positivo es irse al este
	 * 00 01 02
	 * 10 11 12   12 es el centro
	 * 20 21 22
	 * /
	
	private double[] desplazamiento = {0.0, 0.0}; // Norte  y Este
	private double pixelMetro = 1.0;
	
	private boolean primeraVez = true;
	
	public boolean isPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(boolean primeraVez) {
		this.primeraVez = primeraVez;
	}

	public double getLatitudGrados() {
		return latitudGrados;
	}
	
	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public double resolucionMapa() {
		return (156543.04  * Math.cos(latitudGrados) / (Math.pow(2, this.zoomLevel) )); // Según BingMaps
		// Objetivo: sabiendo la resolucion, si movemos el mapa arriba debe moverlo adecuadamente
	}

	public void addDesplazamiento(double[] desplazamiento) {
		this.desplazamiento[0] += desplazamiento[0];
		this.desplazamiento[1] += desplazamiento[1];
	}

	public void setLatitudGrados(double latitudGrados) {
		this.latitudGrados = latitudGrados;
	}

	public double getLongitudGrados() {
		return longitudGrados;
	}

	public void setLongitudGrados(double longitudGrados) {
		this.longitudGrados = longitudGrados;
	}

	public double getAngulo() {
		return angulo;
	}

	public void setAngulo(double angulo) {
		this.angulo = angulo%(2*Math.PI);
	}

	public Color getMiColor() {
		return miColor;
	}

	public void setMiColor(Color miColor) {
		this.miColor = miColor;
	}

	public String getVelocidadTexto() {
		return velocidadTexto;
	}

	public void setVelocidadTexto(String velocidadTexto) {
		this.velocidadTexto = velocidadTexto;
	}

	public int getX() {
		return x;
	}

	public void setX(double x) {
		this.x = (int) (x * factorCanvasX);
	}

	public int getY() {
		return y;
	}

	public void setY(double y) {
		this.y = (int) (y * factorCanvasY);
	}

	public String getUTMTexto() {
		return UTMTexto;
	}

	public void setUTMTexto(String uTMTexto) {
		UTMTexto = uTMTexto;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public double getFactorCanvasX() {
		return factorCanvasX;
	}

	public void setFactorCanvasX(double escalaOriginalX) {
		this.factorCanvasX = imageWidth/escalaOriginalX;
	}

	public double getFactorCanvasY() {
		return factorCanvasY;
	}

	public void setFactorCanvasY(double escalaOriginalY) {
		this.factorCanvasY = imageHeight/escalaOriginalY;
	}

	public void paint(Graphics g) {
		
		pixelMetro = resolucionMapa();
		
		if(this.mapa[1][1] == null && this.primeraVez == false) { // Si la imagen central no está o no nos han mandado pintar antes, pues la cargamos
			try {
				System.out.println("CARGANDO COORDENADAS");
				
				//this.mapa[1][1] = t.getImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/40.3866,-3.6319?mapSize=1900,1900&zoomlevel=18&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
				// La de abajo aprece funcionar, pero si pongo el 18 como parámetro se rompe
				
				if (mapa[1][1] != null) this.mapa[1][1].flush();
				Toolkit t = Toolkit.getDefaultToolkit();
				
				String url = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt";
				Image temp;
				
				temp = //t.getImage(new URL(url));
				t.createImage(new URL(url));
				System.out.println("Pidiendo imagen de "+url);
				this.mapa[1][1] = temp;
				this.desplazamiento[0] = 0.0;
				this.desplazamiento[1] = 0.0;
				//g.drawImage(temp, 180, 180, this);
				//this.mapa[1][1] = t.getImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel="+this.zoomLevel+"&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} else {
			System.out.println("EL CENTRAL NO ES NULL");
		}
		
		System.out.println("Mi desplazamiento es"+(desplazamiento[0] * pixelMetro));
		
		//Toolkit t = Toolkit.getDefaultToolkit();
		
		if (desplazamiento[0] / pixelMetro < -anchoAlto/2) {
			System.out.println("21 es centro ahora");
			/ *            
			 * 00 01 02
			 * 10 11 12   
			 * 20 21 22   21 es el centro ahora
			 *  n  n  n
			 * /
			Image centro = null;
			
			if(this.mapa[2][1] == null) {
				try {
					Toolkit t = Toolkit.getDefaultToolkit();
					centro = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else centro = this.mapa[2][1];
			
			Image[][] mapaAux = {
					{this.mapa[1][0], this.mapa[1][1], this.mapa[1][2]},
					{this.mapa[2][0], centro, this.mapa[2][2]},
					{null, null, null}
					};
			this.mapa = mapaAux; // TO-DO
			
			/ *
			
			this.mapa[0][0] = this.mapa[1][0];
			this.mapa[0][1] = this.mapa[1][1];
			this.mapa[0][2] = this.mapa[1][2];
			
			this.mapa[1][0] = mapa[2][0];
			if(mapa[2][1] == null) {
				try {
					Toolkit t = Toolkit.getDefaultToolkit();
					this.mapa[1][1] = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else this.mapa[1][1] = this.mapa[2][1];
			this.mapa[1][2] = this.mapa[2][2];
			
			this.mapa[0][0] = null;
			this.mapa[0][1] = null;
			this.mapa[0][2] = null;
			* /
			desplazamiento[0] = 0.0; // Reseteamos el desplazamiento N
			
		} else if (desplazamiento[0] / pixelMetro > anchoAlto/2) {
			System.out.println("01 es centro ahora");
			/ *  n  n  n           
			 * 00 01 02   01 es el centro ahora
			 * 10 11 12   
			 * 20 21 22
			 * /
			Image centro = null;
			
			if(this.mapa[0][1] == null) {
				try {
					Toolkit t = Toolkit.getDefaultToolkit();
					centro = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else centro = this.mapa[0][1];
			
			Image[][] mapaAux = {
					{null, null, null},
					{this.mapa[0][0], centro, this.mapa[0][2]},
					{this.mapa[1][0], this.mapa[1][1], this.mapa[1][2]}
					};
			this.mapa = mapaAux; // TO-DO
			
			desplazamiento[0] = 0.0; // Reseteamos el desplazamiento N
			
		}
		// ESTE-OESTE
		if (desplazamiento[1] / pixelMetro < -anchoAlto/2) {
			System.out.println("10 es centro ahora");
			/ *            
			 *  n 00 01 02   
			 *  n 10 11 12   10 es el centro ahora
			 *  n 20 21 22
			 * 
			 * /
			Image centro = null;
			
			if(this.mapa[1][0] == null) {
				try {
					Toolkit t = Toolkit.getDefaultToolkit();
					centro = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else centro = this.mapa[1][0];
			
			Image[][] mapaAux = {
					{null, this.mapa[0][0], this.mapa[0][1]},
					{null, centro, this.mapa[1][1]},
					{null, this.mapa[2][0], this.mapa[2][1]}
					};
			this.mapa = mapaAux; // TO-DO
			
			desplazamiento[1] = 0.0; // Reseteamos el desplazamiento E
			
		} else if (desplazamiento[1] / pixelMetro > anchoAlto/2) {
			System.out.println("12 es centro ahora");
			/ *            
			 * 00 01 02 n  
			 * 10 11 12 n   12 es el centro ahora
			 * 20 21 22 n
			 * 
			 * /
			Image centro = null;
			
			if(this.mapa[1][2] == null) {
				try {
					Toolkit t = Toolkit.getDefaultToolkit();
					centro = t.createImage(new URL("https://dev.virtualearth.net/REST/v1/Imagery/Map/Aerial/"+this.getLatitudGrados()+","+this.getLongitudGrados()+"?mapSize=1900,1900&zoomlevel=20&key=AlG9KnK2iGsV9RmAXQVPnHvmIyz3m8jeLoe3SgwFRB5NY5uFnH2G5vbeubzEsBYt"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} else centro = this.mapa[1][2];
			
			Image[][] mapaAux = {
					{this.mapa[0][1], this.mapa[0][2], null},
					{this.mapa[1][1], centro, null},
					{this.mapa[2][1], this.mapa[2][2], null}
					};
			this.mapa = mapaAux; // TO-DO
			
			desplazamiento[1] = 0.0; // Reseteamos el desplazamiento E
		}
		
		// Para cada imagen del cuadrado transformamos
		Graphics2D g2d = (Graphics2D) g;
		// at.translate(this.x-img.getWidth(null) / 2, this.y-img.getHeight(null) / 2);
		AffineTransform atA1 = AffineTransform.getTranslateInstance(0, 0);
		atA1.translate(-this.desplazamiento[1]/pixelMetro - this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro -this.anchoAlto / 2);
		AffineTransform atA2 = AffineTransform.getTranslateInstance(0, 0);
		atA2.translate(-this.desplazamiento[1]/pixelMetro, this.desplazamiento[0]/pixelMetro -this.anchoAlto / 2);
		AffineTransform atA3 = AffineTransform.getTranslateInstance(0, 0);
		atA3.translate(-this.desplazamiento[1]/pixelMetro + this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro -this.anchoAlto / 2);
		
		AffineTransform atB1 = AffineTransform.getTranslateInstance(0, 0);
		atA1.translate(-this.desplazamiento[1]/pixelMetro - this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro);
		AffineTransform atB2 = AffineTransform.getTranslateInstance(0, 0);
		atB2.translate((this.desplazamiento[1]/pixelMetro -this.anchoAlto/2), (-this.desplazamiento[0]/pixelMetro -this.anchoAlto/2));
		AffineTransform atB3 = AffineTransform.getTranslateInstance(0, 0);
		atA1.translate(-this.desplazamiento[1]/pixelMetro + this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro);
		
		AffineTransform atC1 = AffineTransform.getTranslateInstance(0, 0);
		atC1.translate(-this.desplazamiento[1]/pixelMetro - this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro +this.anchoAlto / 2);
		AffineTransform atC2 = AffineTransform.getTranslateInstance(0, 0);
		atA2.translate(-this.desplazamiento[1]/pixelMetro, this.desplazamiento[0]/pixelMetro +this.anchoAlto / 2);
		AffineTransform atC3 = AffineTransform.getTranslateInstance(0, 0);
		atC3.translate(-this.desplazamiento[1]/pixelMetro + this.anchoAlto / 2, this.desplazamiento[0]/pixelMetro +this.anchoAlto / 2);
		
		// Luego las pintamos de tal forma que la imagen central esté por encima y le sigan las demás por distancia ortogonal
        if (this.mapa[0][0] != null) g2d.drawImage(this.mapa[0][0], atA1, null); else System.out.println("E");
        if (this.mapa[0][2] != null) g2d.drawImage(this.mapa[0][2], atA3, null); else System.out.println("E");
        if (this.mapa[2][0] != null) g2d.drawImage(this.mapa[2][0], atC1, null); else System.out.println("E");
        if (this.mapa[2][2] != null) g2d.drawImage(this.mapa[2][2], atC3, null); else System.out.println("E");
		
        if (this.mapa[0][1] != null) g2d.drawImage(this.mapa[0][1], atA2, null); else System.out.println("E");
        if (this.mapa[1][0] != null) g2d.drawImage(this.mapa[1][0], atB1, null); else System.out.println("E");
        if (this.mapa[1][2] != null) g2d.drawImage(this.mapa[1][2], atB3, null); else System.out.println("E");
        if (this.mapa[2][1] != null) g2d.drawImage(this.mapa[2][1], atC2, null); else System.out.println("E");
        
        if (this.mapa[1][1] != null) {
        	//g2d.drawImage(this.mapa[1][1], atB2, null);
        	
        	g.drawImage(this.mapa[1][1], (int)(this.desplazamiento[1]/pixelMetro -this.anchoAlto/3), (int)(-this.desplazamiento[0]/pixelMetro -this.anchoAlto/3), this);
        	//MyCanvas yo = this;
			//Thread thr = new Thread(new Runnable() {
            //    public void run() {
            //    	g.drawImage(this.mapa[1][1], (int)(this.desplazamiento[1]/pixelMetro-this.anchoAlto/2), (int)(-this.desplazamiento[0]/pixelMetro-this.anchoAlto/2), this);
            //    }
			//}
			//);
        }
		
		// Flecha
        Toolkit t = Toolkit.getDefaultToolkit();
		Image img = t.getImage("pointIII.png");
        
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        at.translate(this.anchoAlto / 2, this.anchoAlto / 2);
		at.rotate(this.angulo,img.getWidth(null) / 2, img.getHeight(null) / 2);
		
		g2d.drawImage(img, at, null);
        
		// UI
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect((int)this.anchoAlto, 0, 200, (int)this.anchoAlto);
        g.setColor(miColor);
        g.fillRect((int)this.anchoAlto, 0, 140 + 50, 100);

        g.setColor(Color.black); 
        Font fuente = g.getFont();
        Font fuente2 = fuente.deriveFont(200);
        g.setFont(fuente2);
        g.drawChars(velocidadTexto.toCharArray(), 0, velocidadTexto.length(), (int)(this.anchoAlto) + 15, 25);
        g.drawChars(UTMTexto.toCharArray(), 0, UTMTexto.length(), (int)(this.anchoAlto) + 15, 125);
        String lat = String.valueOf(this.latitudGrados);
        String lon = String.valueOf(this.longitudGrados);
        
        g.drawChars(lat.toCharArray(), 0, lat.length(), (int)(this.anchoAlto) + 15, 200);
        g.drawChars(lon.toCharArray(), 0, lon.length(), (int)(this.anchoAlto) + 15, 220);
        System.out.println("Primera vez = "+this.primeraVez);
	}
	
	public static void main(String[] losArgumentos) {
		MyCanvas m = new MyCanvas();
		m.setImageName("ImagenINSIA.PNG");
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955 + 200, 950);
		f.setLocation(300, 0);
		f.setVisible(true);
		System.out.println("Imagen");
		f.addWindowListener(m);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana abierta");
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana cerrandose, cerrando programa");
		System.exit(0);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Ventana cerrada, cerrando programa");
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
*/
