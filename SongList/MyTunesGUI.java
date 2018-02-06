import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Driver class for the MyTunesGUI project that will
 * create and launches a GUI for a play list of songs.
 * 
 * @author MaryJo Foster
 *
 */
public class MyTunesGUI {

	public static void main(String[] args) {
		JFrame frame = new JFrame("MJ's Tunes!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MyTunesGUIPanel());
		frame.setPreferredSize(new Dimension(1300,700));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // centers frame on screen
	}
}
