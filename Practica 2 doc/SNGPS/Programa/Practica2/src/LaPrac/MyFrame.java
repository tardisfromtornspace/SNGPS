package LaPrac;

import javax.swing.*;
import javax.swing.event.*;

import LaPrac.VecUtil.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class MyFrame extends JFrame
{
	VecUtil<Double> vu;
    Draggable draggable;
    Point imageDimensions;
    ArrayList<JLabel> points;
    ArrayList<CompositeSpeedometer> speeds;
    String mapDir, pointDir, speedDir;
    
    MyFrame(String mapDir, String pointDir, String speedDir)
    {
    	vu = new VecUtil<>(1e-8);
    	this.mapDir = mapDir;
    	this.pointDir = pointDir;
    	this.speedDir = speedDir;
    	
    	//Credits credit = new Credits(0, 0);
    	points = new ArrayList<>();
    	speeds = new ArrayList<>();
    	// Create main Draggable
    	draggable = new Draggable(mapDir);
    	// Get image dimensions from main Draggable
        imageDimensions = draggable.getCurrentImageDim();
    	
       // {
        	/* Add Point(JLabel) object and set preferences */
    	/*	
        	points.add(getDefaultJLabelPoint());
    	}*/
        
        // Add all JComponent-like object instances to draggable JLayeredPanel-derived like instance
        draggable.rescaleRelativeImage(0.8d);
        draggable.add(getDefaultJSlider(), Integer.valueOf(2));
        draggable.add(new Credits(20,5), Integer.valueOf(0));
        /*
        for(JLabel p : points)
        	draggable.linkToMainComponent(p,1);
        */
        // Add main Draggable Object to main JFrame and set preferences
       
       // this.speedometerBase = getDefaultCompositeSpeedometerBase(0, 0);
       // this.speedometerBase.add(getDefaultCompositeSpeedometer(0,0));
        //this.add(this.speedometerBase);
        //draggable.add(this.speedometerBase, Integer.valueOf(2));
        this.add(draggable);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(960,520);
        this.setVisible(true);
    }
    public void updateSpeedometerAll(ArrayList<Double> spds, double div)
    {
    	if(spds == null)
    		return;
    	int len = this.speeds.size();
    	
    	if(spds.size() < len)
    		return;
    	
    	for(int i = 0; i < len; i++)
    		updateSpeedometer(i, spds.get(i) /div);
    }
    public void updateSpeedometer(int idx, Double spd)
    {
    	if(idx >= this.speeds.size())
    		return;
    	this.speeds.get(idx).setSpeed(spd);
    }
    public void setLinkedPointRotation(int idx, double rot)
    {
    	if(idx >= this.points.size())
    		return;
    	this.draggable.setLinkedComponentRotation(idx, rot);
    }
    public void setLinkedPointRelativeLocation(int idx, double rX, double rY)
    {
    	if(idx >= this.points.size())
    		return;
    	this.draggable.setLinkedComponentRelativeLocation(idx,
    			new Point(
    					(int)((rX * this.imageDimensions.x) - this.points.get(idx).getWidth() / 2),
    					(int)((rY * this.imageDimensions.y) - this.points.get(idx).getHeight() / 2))
    			);
    }
    public void addSpeedometer(int x, int y)
    {
    	this.speeds.add(getDefaultCompositeSpeedometer(x,y));
        this.draggable.add(this.speeds.get(this.speeds.size()-1), Integer.valueOf(2));
    }
    public void addPoint()
    {
    	this.points.add(getDefaultJLabelPoint());
        draggable.linkToMainComponent(this.points.get(this.points.size()-1),1);
    	addSpeedometer(1600, 300 * this.points.size());
    }
    public void addRotablePoint()
    {
    	this.points.add(getDefaultJLabelRotablePoint());
        draggable.linkToMainComponent(this.points.get(this.points.size()-1),1);
    	addSpeedometer(0, 0);
    }
    public void addRotablePoint(int R, int G, int B)
    {
    	this.points.add(getDefaultJLabelRotablePoint(R, G, B));
        draggable.linkToMainComponent(this.points.get(this.points.size()-1),1);
    	addSpeedometer(1600, 200 * this.points.size()-1);
    }
    private JLabel getDefaultJLabelPoint()
    {
    	ImageIcon img = new ImageIcon(this.pointDir);
    	JLabel point = new JLabel();
    	point.setIcon(img);
    	point.setVerticalAlignment(JLabel.TOP);
    	point.setHorizontalAlignment(JLabel.LEFT);
        point.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
        
        return (JLabel) point;
    }
    private JLabel getDefaultJLabelRotablePoint()
    {
    	ImageIcon img = new ImageIcon(this.pointDir);
    	JLabelRotable point = new JLabelRotable(0.d,0.d, this.pointDir);
        point.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
        
        return (JLabel) point;
    }
    private JLabel getDefaultJLabelRotablePoint(int R, int G, int B)
    {
    	ImageIcon img = new ImageIcon(this.pointDir);
    	JLabelRotable point = new JLabelRotable(0.d,0.d,this.pointDir, R, G, B, false);
        point.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
        
        return (JLabel) point;
    }
    private JSlider getDefaultJSlider()
    {
    	JSlider slider = new JSlider(30, 70, 70);
    	slider.setPreferredSize(new Dimension(400,200));
    	slider.setPaintTicks(true);
    	slider.setMinorTickSpacing(10);
    	slider.setOrientation(SwingConstants.VERTICAL);
    	slider.setBounds(20, 20, 50, 200);
    	slider.setBackground(new Color(60,60,60,200));
    	slider.setOpaque(true);
    	slider.addChangeListener(new ChangeListener() {
    		@Override
    		public void stateChanged(ChangeEvent changeEvent)
    		{
    			draggable.repaint();
    			draggable.rescaleRelativeImage((float) slider.getValue() / 100);
    		}
    		});
    	
    	return slider;
    	
    }
    private CompositeSpeedometer getDefaultCompositeSpeedometer(int x, int y)
    {
    	CompositeSpeedometer sp = new CompositeSpeedometer(x,y, this.speedDir, this.speedDir, 200);
    	return sp;
    }
}