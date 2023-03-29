package LaPrac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Draggable extends JLayeredPane
{
    ImageIcon constImage;
    ArrayList<Component> linked;
    ArrayList<Double> linkedRotation;
    ArrayList<Point> linkedRelative;
    ArrayList<CompositeSpeedometer> speeds;
    ImageIcon image;
    int WIDTH;
    int HEIGHT;
    double scale;
    Point imageCorner;
    Point prevPoint;

    Draggable(String dir)
    {
    	constImage = new ImageIcon(dir);
        linked = new ArrayList<>();
        linkedRelative = new ArrayList<>();
        linkedRotation = new ArrayList<>();
        scale = 0.5d;
        WIDTH = constImage.getIconWidth();
        HEIGHT = constImage.getIconHeight();
        image = new ImageIcon(constImage.getImage());
        imageCorner = new Point(0, 0);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }
    
    public Point getCurrentImageDim()
    {
    	return new Point(this.WIDTH, this.HEIGHT);
    }
    
    public double getCurrentScale()
    {
    	return this.scale;
    }
    public void linkToMainComponent(Component component, int level)
    {
        this.linkedRelative.add(component.getLocation());
        component.setBounds(
                (int)(component.getX() + imageCorner.getX()),
                (int)(component.getY() + imageCorner.getY()),
                component.getWidth(),
                component.getHeight()
        );
        component.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent)
            {
                repaint();
            }
        });
        this.linked.add(component);
        this.linkedRotation.add(0.0d);
        this.add(component, Integer.valueOf(level));
    }
    
    public void setLinkedComponentRotation(int idx, double rotation)
    {
    	if(idx >= linked.size())
    		return;
    	if(!(this.linked.get(idx) instanceof JLabelRotable))
    		return;
    	
    	((JLabelRotable)this.linked.get(idx)).setRotation(rotation);
    }
    public void setLinkedComponentRelativeLocation(int idx, Point location)
    {
        if(idx >= linked.size())
            return;
        linkedRelative.get(idx).setLocation(location);
        transformRelativeLinkLocation(idx);
    }
    
    public Point getLinkedComponentRelativeLocation(int idx)
    {
        return idx >= linked.size() ? new Point(0,0) : linkedRelative.get(idx);
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) imageCorner.getX(), (int) imageCorner.getY());
    }

    public void rescaleRelativeImage(double scale)
    {
        image.setImage(
        		getScaledImage(constImage.getImage(),
        				(int) (WIDTH * scale),
        				(int) (HEIGHT * scale))
        		);
        transformRelativePosition(scale, true);
        this.scale = scale;
    }
    private void transformRelativeLinkLocation(int idx)
    {
        if(idx >= linked.size())
        	return;
        Point point = linkedRelative.get(idx);
        Component comp = linked.get(idx);
        comp.setBounds(
        		(int) (this.imageCorner.getX() + point.getX() * scale),
        		(int) (this.imageCorner.getY() + point.getY() * scale),
        		comp.getWidth(),
        		comp.getHeight()
        		);
    }
    
    private void transformRelativeLinkLocation()
    {
        for(int i=0; i < this.linked.size(); i++)
        	transformRelativeLinkLocation(i);
    }

    private void transformRelativePosition(double scale, boolean link)
    {
        int offsetX = (int)(((this.scale-scale) * WIDTH / 2));
        int offsetY = (int)(((this.scale-scale) * HEIGHT / 2));
        imageCorner.translate(offsetX, offsetY);
        if(!link)
            return;
        transformRelativeLinkLocation();
    }

    public static Image getScaledImage(Image srcImg, int w, int h)
    {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.SCALE_DEFAULT);
        Graphics2D g2d = resizedImg.createGraphics();
        g2d.drawImage(srcImg, 0, 0, w, h, null);
        g2d.dispose();

        return resizedImg;
    }
    private class ClickListener extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            prevPoint = e.getPoint();
        }
    }
    private class DragListener extends MouseMotionAdapter
    {
        public void mouseDragged(MouseEvent e)
        {
            Point currentPoint = e.getPoint();
            double dragX = currentPoint.getX() - prevPoint.getX();
            double dragY = currentPoint.getY() - prevPoint.getY();
            for(Component l : linked)
                l.setBounds((int) (l.getX() + dragX), (int) (l.getY() + dragY), l.getWidth(), l.getHeight());
            imageCorner.translate((int)dragX,(int)dragY);
            prevPoint = currentPoint;
            repaint();
        }
    }
}