import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SellerServerSocket {
	
	public static void communicateWithBuyer()
	{
		int portNumber = 4848;

		try (
				ServerSocket serverSocket = new ServerSocket(portNumber);
			    Socket clientSocket = serverSocket.accept();
			    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			) {
				String inputLine, outputLine;
            
				// Initiate conversation with client
				SellerSocketProtocol ssp = new SellerSocketProtocol();
				outputLine = ssp.processInput(null);
				out.println(outputLine);

				while ((inputLine = in.readLine()) != null) {
			        outputLine = ssp.processInput(inputLine);
			        out.println(outputLine);
			        if (outputLine.equals("Bye."))
			            break;
				}
            } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
	}

}
