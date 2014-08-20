import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SellerEncryption {

	public static void encryptDatabase(ArrayList<String> storedTransactions)
	{
		String databaseString = storedTransactions.get(0);
		for (int i=1; i<storedTransactions.size(); i++)
		{
			databaseString = databaseString + "\n" + storedTransactions.get(i);
		}
		String databaseHash = SellerEncryption.hash(databaseString);//hash storedTransactions
		SellerBackEnd.setDatabaseHash(databaseHash);
		
		byte[] databaseBytes = SellerEncryption.encryptString(databaseString);//encrypt storedTransactions using symmetric key
		File encryptedFile = new File ("TransactionDatabase.txt");
		SellerEncryption.writeToFile(databaseBytes, encryptedFile);
		
	}
	
	public static void encryptWaitingOrders(ArrayList<String> waitingOrders)
	{
		String waitingOrdersString = waitingOrders.get(0);
		for (int i=1; i<waitingOrders.size(); i++)
		{
			waitingOrdersString = waitingOrdersString + "\n" + waitingOrders.get(i);
		}
				
		byte[] waitingOrdersBytes = SellerEncryption.encryptString(waitingOrdersString);//encrypt waitingOrders using symmetric key
		File encryptedFile = new File ("WaitingTransactionsDatabase.txt");
		SellerEncryption.writeToFile(waitingOrdersBytes, encryptedFile); //save to file "WaitingTransactionsDatabase.txt"
	}
	
	public static String hash(String databaseString)
	{
		String hashedString=null;
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(databaseString.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			hashedString = sb.toString();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		
		return hashedString;
	}
	
	public static byte[] encryptString(String clearText)
	{
		Cipher aes;
		byte[] cipherText = null;
			try {
				aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
				aes.init(Cipher.ENCRYPT_MODE, SellerBackEnd.getSymmetricKey());
				cipherText = aes.doFinal(clearText.getBytes());
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("error in encryptString");
				e.printStackTrace();
			}
		return cipherText;
	}
	
	private static void writeToFile(byte[] cipherText, File encryptedFile)
	{
		//taking an array of bytes(cipherText) and writing to a file
			FileOutputStream fileOutputStream;
			try {
				fileOutputStream = new FileOutputStream(encryptedFile);
			    fileOutputStream.write(cipherText);
			    fileOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
	 }
}
