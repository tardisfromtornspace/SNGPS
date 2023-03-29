package LaPrac;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Credits extends JLabel {
	Credits(int x, int y)
	{
		JTextField text = new JTextField();
		
		text.setFont(new Font("Consolas", Font.BOLD, 10));
        text.setForeground(new Color(0,0,0,200));
        text.setBackground(new Color(100,100,100,200));
        text.setCaretColor(Color.white);
		text.setText(credits());
        text.setPreferredSize(new Dimension(1230, 10));
        text.setBounds(0, 0, 1230, 10);
        text.setEditable(false);
        text.setBorder(null);
		this.add(text);
		this.setBounds(x, y, 1230, 10);
		this.setVisible(true);
	}
	private String credits()
	{
		StringBuilder stb = new StringBuilder();
		ArrayList<String> names = new ArrayList<>(
				Arrays.asList(
						"Alejandro Serrano López[bq0100]",
						"Diego Torres Aranha[bq0383]",
						"Gabriel Gil García[bq0162]",
						"Juan José Urioste Zunino[bt0492]",
						"Miguel Laredo Barbadillo[br0449]",
						"Iván Lumbano Vivar[br0097]"		
				)
		);
		Collections.shuffle(names);
		stb.append("Realizado por:");
		for(String n : names)
			stb.append('\n' +" :: "+ n);
		return stb.toString();
	}
}
