package LaPrac;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CompositeSpeedometer extends JLayeredPane {
	private double speed = 0.0d;
	private double speedFormat = 0.0d;
	private double maxSpeed = 1;
	private JTextField text = new JTextField();
    JLabelRotable ind;
	
	public CompositeSpeedometer(int x, int y, String imgDir, String imgDirI, double maxSpeed) {
		JPanel speedometerPanel = new JPanel();
		speedometerPanel.setBounds(0, 0, 500, 500);
		speedometerPanel.setBackground(new Color(0,0,0,100));
		text.setFont(new Font("Consolas", Font.BOLD, 40));
        text.setForeground(new Color(255,255,255,200));
        text.setBackground(new Color(50,50,50,0));
        text.setCaretColor(Color.white);
		text.setText(Double.toString(speed));
        text.setPreferredSize(new Dimension(300, 300));
        text.setBounds(0, 0, 300, 300);
        text.setEditable(false);
        text.setBorder(null);
		this.add(text, 1);
		this.ind = getRotableInd(imgDirI);
		this.add(ind, 5);
		this.add(speedometerPanel, 2);
		this.setBounds(x, y, 700, 300);
		this.setVisible(true);
		this.maxSpeed = maxSpeed / 3.6;
	}
	public void setSpeed(double speed)
	{
		if(speed > maxSpeed)
			speed = maxSpeed;
		this.speed = speed;
		text.setText(new DecimalFormat("#.#").format(speed * 3.6d) +"Km/h");
		this.ind.setRotation(180 * speed / maxSpeed);
		this.ind.repaint();
		this.repaint();
	}
	public JLabelRotable getRotableInd(String imgDir)
	{
		ImageIcon img = new ImageIcon(imgDir);
    	JLabelRotable ind = new JLabelRotable(0.d,-90.d,imgDir, 255,0,0, true);
        ind.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
        
        return ind;
	}
}
