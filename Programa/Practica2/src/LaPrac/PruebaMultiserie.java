package LaPrac;

import java.util.ArrayList;

public class PruebaMultiserie {
	public static void main(String[] args) {
		String[] serialDirs = {
				// Windows directories
				"COM2",
				"COM3",
				
				/*	Unix-like directories
				 *	Just because they are virtual & not in the default
				 *	dir its mandatory to specify them manually
				 * */
				
				"/dev/pts/10",
				"/dev/pts/12",
				"/dev/pts/14",
				"/dev/pts/16",
				"/dev/pts/25",
				"/dev/pts/27",
				"/dev/pts/29",
				"/dev/pts/31",
				"/dev/pts/41",
				"/dev/pts/43",
				"/dev/pts/45",
				"/dev/pts/47",
		};
		
		MyFrame frame = new MyFrame("src/gpsMAP.jpg", "src/point.png");
		ArrayList<Subject> multiSer = new ArrayList<>();

		{
			int i;
			for(i = 0; i < serialDirs.length; i++) {
				frame.addPoint();
				multiSer.add(new Subject(i));
				Observer calculadora = new Observer(30, "GPS", frame);
				calculadora.addObservable(multiSer.get(i));
			}
			for(i = 0; i < multiSer.size(); i++)
				multiSer.get(i).leerPuertos(serialDirs[i]);
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Subject s : multiSer)
			s.start();		
	}
}

