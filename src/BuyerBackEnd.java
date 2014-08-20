import java.io.FileOutputStream;
import java.io.IOException;


public class BuyerBackEnd {
	private static String confirmNum;
	private static int successState;
	
	public static void sendToBackEnd(String purchaseData, String hostName)
	{
		byte[] encryptedData = BuyerEncryption.EncryptData(purchaseData);
		
		/////////////Void the following line to test program without sockets//////////////////
		//ClientSocket.communicateWithSeller(hostName, encryptedData);
		/////////////Void the above line to test program without sockets/////////////////////
		
		//////Un-void the following line to test program without sockets///////////////////////
		BuyerBackEnd.printOrdertoFile(encryptedData);
		//////Un-void the above line to test program without sockets///////////////////////
	}

	public static String getConfirmNum() {
		return confirmNum;
	}

	public static void setConfirmNum(String confirmNum) {
		BuyerBackEnd.confirmNum = confirmNum;
	}

	public static int getSuccessState() {
		return successState;
	}

	public static void setSuccessState(int successState) {
		BuyerBackEnd.successState = successState;
	}
	
	public static void printOrdertoFile(byte[] encryptedOrderInfo)
	{
		try {
			FileOutputStream fos = new FileOutputStream("TestOrder.txt");
			fos.write(encryptedOrderInfo);
			fos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BuyerBackEnd.setConfirmNum("0238748238");
    	BuyerBackEnd.setSuccessState(2);
	}
	
}
