package LaPrac;


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class JLabelRotable extends JLabel {
	private BufferedImage img;
	private double rotation;
	private double rotationOffset;
	private boolean ignoreBorders;
	
	public JLabelRotable(double rotation, double rotationOffset, String imgDir) {
		try {
			this.img = ImageIO.read(new File(imgDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.rotation = rotation;
		this.rotationOffset = rotationOffset;
	}
	public JLabelRotable(double rotation, double rotationOffset, String imgDir, int R, int G, int B, boolean ignoreBorders) {
		this.ignoreBorders = ignoreBorders;
		try {
			this.img = getColorImage(
					ImageIO.read(new File(imgDir)),
					R,
					G,
					B);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.rotation = rotation;
		this.rotationOffset = rotationOffset;
	}
	
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		AffineTransform at = AffineTransform.getTranslateInstance(0, 0);		
		at.rotate(Math.toRadians(rotation + rotationOffset),img.getWidth() / 2, img.getHeight() / 2);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(img, at, null);
	}
	
	private BufferedImage getColorImage(BufferedImage image, int R, int G, int B)
	{
	    int width = image.getWidth();
	    int height = image.getHeight();
	    int x, y;
	    WritableRaster r = image.getRaster();

	    for(x = 0; x < width; x++) {
	      for(y = 0; y < height; y++) {
	        int[] pixels = r.getPixel(x, y, (int[]) null);
	        if(ignoreBorders || pixels[0] > 100 || pixels[1] > 100 || pixels[2] > 100) {
	        	pixels[0] = R;
	        	pixels[1] = G;
	        	pixels[2] = B;
	        	r.setPixel(x, y, pixels);
	        }
	      }
	    }
	    return image;
	}
}