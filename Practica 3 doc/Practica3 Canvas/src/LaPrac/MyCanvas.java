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
import java.net.URL;

import javax.swing.JFrame;

public class MyCanvas extends Canvas implements WindowListener{
	
	private int x = 0;
	private int y = 0;
	
	private String imageName = "ImagenUPMeINSIA.PNG"; // Imagen por defecto
	private int imageHeight = 0;
	private int imageWidth = 0;
	
	private double factorCanvasX = 1.0;
	private double factorCanvasY = 1.0;
	
	private Color miColor = Color.GREEN;
	
	private String velocidadTexto = "TEST km/h";
	
	private double angulo = Math.PI;
	
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
		Toolkit t = Toolkit.getDefaultToolkit();
		Image i = t.getImage(imageName);
		
		imageHeight = i.getHeight(null);
		imageWidth = i.getWidth(null);
		System.out.println("Imagen de dimensiones "+ imageWidth +"x"+ imageHeight);
		g.drawImage(i, 0, 0, this);
		
		// Ahora dibujamos el punto
		//g.setColor(Color.red);  
		//g.fillOval(this.x-3, this.y-3, 6, 6);     
        //g.setColor(Color.red); 
        //g.drawOval(this.x-6, this.y-6, 12, 12);
        Image img = t.getImage("pointIII.png");
        
        AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
        //at.scale(0.05, 0.05);
        at.translate(this.x-img.getWidth(null) / 2, this.y-img.getHeight(null) / 2);
		at.rotate(angulo,img.getWidth(null) / 2, img.getHeight(null) / 2); //Math.toRadians(angulo)
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(img, at, null);
        
        
        g.setColor(miColor);
        g.fillRect(imageWidth + 25, 0, 140, 75);

        g.setColor(Color.black); 
        Font fuente = g.getFont();
        Font fuente2 = fuente.deriveFont(200);
        g.setFont(fuente2);
        g.drawChars(velocidadTexto.toCharArray(), 0, velocidadTexto.length(), imageWidth + 50, 25);
	}
	
	public static void main(String[] losArgumentos) {
		MyCanvas m = new MyCanvas();
		m.setImageName("ImagenINSIA.PNG");
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955 + 200, 870);
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
