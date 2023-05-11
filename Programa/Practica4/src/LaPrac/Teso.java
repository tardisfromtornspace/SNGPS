package LaPrac;

import javax.swing.JFrame;

public class Teso {
	public static void main(String[] args) {
		int [][] matriz = {{1,2,3},{4,5,6},{7,8,9}};
		System.out.println(matriz[0][1]);
		double[] vecVel = {(0), (+5)};
		MyCanvas canvas = new MyCanvas();
		canvas.setImageName("ImagenINSIA.PNG");
		/*JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(canvas);
		f.setSize(955 + 200, 950);
		f.setLocation(300, 0);
		f.setVisible(true);
		System.out.println("Imagen");
		f.addWindowListener(canvas);*/
		
		double lai = 40.36005333333333;
		double log = -3.5510916666666663;
		canvas.setLatitudGrados(lai);
		canvas.setLongitudGrados(log); //+0.002 cuando vaya a cambiar de mapa
		
		for (int i= 0; i<200; i++) {
			canvas.addDesplazamiento(vecVel);
			canvas.setPrimeraVez(false);
			canvas.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Iteración "+i);
			if ((i+1)%10 == 0) {
				log = log + 0.0005; //-0.002
				lai = lai; //- 0.0005; //-0.002
				canvas.setLatitudGrados(lai);
 				canvas.setLongitudGrados(log);
			}
		}
	}
}
/*
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
	
	desplazamiento[0] = 0.0; // Reseteamos el desplazamiento N
	//desplazamiento[1] = 0.0;
	
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
	//desplazamiento[1] = 0.0;
	
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
	//desplazamiento[0] = 0.0;
	
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
	desplazamiento[0] = 0.0;
}
*/