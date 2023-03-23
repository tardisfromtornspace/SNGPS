package LaPrac;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JLabel;

public class Credits extends JLabel {
	Credits(int x, int y)
	{
		this.setBackground(Color.black);
		System.out.println(credits());
		this.setText(credits());
		this.setBounds(x, y, 1000, 1000);
		this.setVisible(true);
	}
	private String credits()
	{
		StringBuilder stb = new StringBuilder();
		ArrayList<String> names = new ArrayList<>(
				Arrays.asList(
						"Diego",
						"Juan",
						"Alex",
						"Ivan",
						"Gabriel",
						"Miguel"
				)
		);
		Collections.shuffle(names);
		stb.append("Realizado por:");
		for(String n : names)
			stb.append("\n + "+ n);
		return stb.toString();
	}
}
