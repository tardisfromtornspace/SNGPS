package LaPrac;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class MyCanvas extends Canvas implements WindowListener{
	
	int x = 0;
	int y = 0;
	
	String imageName = "ImagenUPMeINSIA.PNG"; // Imagen por defecto
	int imageHeight = 0;
	int imageWidth = 0;
	
	double factorCanvasX = 1.0;
	double factorCanvasY = 1.0;
	
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

		// Image i = t.getImage("ImagenUPMeINSIA.PNG");
		// Image i = t.getImage("ImagenINSIA.PNG");
		Image i = t.getImage(imageName);

		imageHeight = i.getHeight(null);
		imageWidth = i.getWidth(null);
		System.out.println("Imagen de dimensiones "+ imageWidth +"x"+ imageHeight);
		g.drawImage(i, 0, 0, this);
		
		// Ahora dibujamos el punto
		g.setColor(Color.red);  
		g.fillOval(this.x-3, this.y-3, 6, 6);     
        g.setColor(Color.red);   
        g.drawOval(this.x-6, this.y-6, 12, 12); 
	}
	
	public static void main(String[] losArgumentos) {
		MyCanvas m = new MyCanvas();
		m.setImageName("ImagenINSIA.PNG");
		JFrame f = new JFrame("Practica 2: Sistema de Geolocalizacion UPM-INSIA");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(m);
		f.setSize(955, 870);
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
