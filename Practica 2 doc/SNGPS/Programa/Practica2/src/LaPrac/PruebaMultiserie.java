package LaPrac;

import java.util.ArrayList;

public class PruebaMultiserie {
	public static void main(String[] args) {
		String[] serialDirs = {
				// Windows directories
				// "COM2",
				// "COM3",
				
				/*	Unix-like directories
				 *	Just because they are virtual & not in the default
				 *	dir its mandatory to specify them manually
				 * */
				
//				"default"
				"/dev/pts/2",
				"/dev/pts/6"
			/*	"/dev/pts/3"
				"/dev/pts/11",
				"/dev/pts/13",*/
		};
		
		MyFrame frame = new MyFrame("src/gpsMAP.jpg", "src/pointt.png", "src/vel_ind.png");
		ArrayList<Subject> multiSer = new ArrayList<>();
		Observer calculadora = new Observer(30, "GPS", frame, 1e-8d);
		calculadora.establecimientoCoordIniciales(338.083, "W", 4023.500, "N", 337.417, "W", 4023.100,"N", 0.0, 0.0, 30, 30);

		{
			int i;
			int f[];
			for(i = 0; i < serialDirs.length; i++) {
				f = get8Picker((int)(Math.random() * 10));
				frame.addRotablePoint(255*f[0],255*f[1],127*f[2]);
				multiSer.add(new Subject(i));
				calculadora.addObservable(multiSer.get(i));
			}
			for(i = 0; i < multiSer.size(); i++)
				multiSer.get(i).leerPuertos(serialDirs[i]);
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(Subject s : multiSer)
			s.start();
	
		calculadora.startSpeedTracer();
	}
	private static int[] get8Picker(int sel)
	{
		int[][] fact = {
				{0,0,0},
				{0,0,1},
				{0,1,0},
				{0,1,1},
				{1,0,0},
				{1,0,1},
				{1,1,0},
				{1,1,1}
				};
		return fact[sel % 8];
	}
}


