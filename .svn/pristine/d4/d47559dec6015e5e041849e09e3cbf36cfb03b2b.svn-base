import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientSocket {

	public static void communicateWithSeller(String hostName, byte[] encryptedData)
	{
		int portNumber = 4848;

	    try (
	        Socket buyerSocket = new Socket(hostName, portNumber);
	        PrintWriter out = new PrintWriter(buyerSocket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(buyerSocket.getInputStream()));
	    ){
	    	String fromServer;
	    	byte[] toServer;
    		BuyerSocketProtocol bsp = new BuyerSocketProtocol(encryptedData);
    		
	    	while ((fromServer = in.readLine()) != null) {

	    		if (fromServer.equals("Bye."))
	    			break;
	    		
	            toServer = bsp.processInput(fromServer);
	            out.println(toServer);
	    	}
	    }catch (UnknownHostException e) {
	    	BuyerBackEnd.setSuccessState(0);
	    } catch (IOException e1) {
			BuyerBackEnd.setSuccessState(1);
		}
	}
}
