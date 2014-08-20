import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


public class SellerDriver {
	public static void main (String[] args)
	{	
		JFrame frame = new JFrame("eSell Seller System");
		SellerFrontEnd panel = new SellerFrontEnd();
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (SellerBackEnd.getDatabaseDecrypted())
				{
					SellerBackEnd.saveDatabaseInformation();
				}
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
		
		SellerBackEnd.readConfirmNumbers();
		
		/////////////Comment out the following line to test program without sockets/////////////////
		//SellerBackEnd.openSocket();
		/////////////Comment out the above line to test program without sockets/////////////////////
	}

}
