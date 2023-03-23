package LaPrac;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.util.ArrayList;

public class MyFrame extends JFrame
{
    Draggable draggable;
    Point imageDimensions;
    ArrayList<JLabel> points;
    String mapDir, pointDir;
    
    MyFrame(String mapDir, String pointDir)
    {
    	this.mapDir = mapDir;
    	this.pointDir = pointDir;
    	
    	//Credits credit = new Credits(0, 0);
    	points = new ArrayList<>();
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
        draggable.add(getDefaultJSlider(), Integer.valueOf(1));
        draggable.add(new Credits(10000,4000), Integer.valueOf(1));
        /*
        for(JLabel p : points)
        	draggable.linkToMainComponent(p,1);
        */
        // Add main Draggable Object to main JFrame and set preferences
        
        this.add(draggable);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(960,520);
        this.setVisible(true);
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
    public void addPoint()
    {
    	this.points.add(getDefaultJLabelPoint());
        draggable.linkToMainComponent(this.points.get(this.points.size()-1),1);
    }
    private JLabel getDefaultJLabelPoint()
    {
    	JLabel point = new JLabel();
    	point.setIcon(new ImageIcon(this.pointDir));
    	point.setVerticalAlignment(JLabel.TOP);
    	point.setHorizontalAlignment(JLabel.LEFT);
        point.setBounds(0,0,30,30);
        
        return point;
    }
    private JSlider getDefaultJSlider()
    {
    	JSlider slider = new JSlider(5, 80, 80);
    	slider.setPreferredSize(new Dimension(400,200));
    	slider.setPaintTicks(true);
    	slider.setMinorTickSpacing(10);
    	slider.setOrientation(SwingConstants.VERTICAL);
    	slider.setBounds(20, 20, 50, 200);
    	slider.setBackground(new Color(50,50,50,195));
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
}
