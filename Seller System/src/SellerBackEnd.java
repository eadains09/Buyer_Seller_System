import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SellerBackEnd {
	private static ArrayList<String> allConfirmNumbers = new ArrayList<String>();
	private static ArrayList<String> tempDataStorage = new ArrayList<String>();//encrypted
	private static ArrayList<String> tempConfirmNumbers = new ArrayList<String>();
	private static ArrayList<String> database = new ArrayList<String>();
	private static ArrayList<String> waitingData = new ArrayList<String>();
	
	private static String password;
	private static String databaseHash, testHash;
	private static PrivateKey privateKey;
	private static SecretKeySpec symmetricKey;
	private static boolean databaseDecrypted=false, fileRead=false;

	public static void openSocket()//called as soon as program opens
	{
		SellerServerSocket.communicateWithBuyer();
	}
	
	public static void readConfirmNumbers()//called as soon as program opens
	{	
		try {
			BufferedReader br = new BufferedReader (new FileReader ("AllConfirmNumbers.txt"));
			
			while (br.ready())
			{
				allConfirmNumbers.add(br.readLine());
			}
			br.close();
			
			SellerBackEnd.setDatabaseHash(allConfirmNumbers.get(allConfirmNumbers.size()-1));
			allConfirmNumbers.remove(allConfirmNumbers.size()-1);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void storeTempData(String encryptedData, String confirmNum)//called by socketProtocol
	{
		tempDataStorage.add(encryptedData);
		tempConfirmNumbers.add(confirmNum);
	}
	
	public static void decryptTempData()//called by front end when user clicks waitingOrders Button
	{
		if (!tempDataStorage.isEmpty())
		{
			ArrayList<String> newData = Decryption.decryptNewData(tempDataStorage);
		
			for (int i=0; i<newData.size(); i++)
			{
				waitingData.add(tempConfirmNumbers.get(i) + " " + newData.get(i));
			}
		}
	}
	
	public static boolean checkPassword()//called when user hits enter password button
	{
		boolean correct = false;
		if (databaseHash.equals(testHash))
		{
			correct = true;
		}
		
		return correct;
	}
	
	public static void addtoDatabase(String currItem)//called by front end when user clicks clearItem button
	{
		database.add(currItem);
	}
	
	public static ArrayList<String> searchDatabase(String currSearchKey)//called when user clicks Search button
	{
		ArrayList<String> foundItems = new ArrayList<String>();
		
		for (int i=0; i<database.size(); i++)
		{
			if (database.get(i).contains(currSearchKey))
			{
				foundItems.add(database.get(i));
			}
		}
		
		return foundItems;
	}
	
	public static ArrayList<String> getDatabase()//called when user clicks all Orders button
	{
		ArrayList<String> databaseCopy = new ArrayList<String>();
		
		for (int i=0; i<database.size(); i++)
		{
			databaseCopy.add(database.get(i));
		}
		return databaseCopy;
	}
	
	public static String generateConfirmNum()//called by socketProtocol
	{
		boolean newConfirmNum = false;
		String confirmNum = SellerBackEnd.generateRandom();
		
		while(newConfirmNum == false)
		{	
			if (allConfirmNumbers.contains(confirmNum))
			{
				confirmNum = SellerBackEnd.generateRandom();
			}else{
				allConfirmNumbers.add(confirmNum);
				newConfirmNum = true;
			}
		}
		
		return confirmNum;
	}
	
	public static String generateRandom()//called by generateConfirmNum
	{
		Random rnd = new Random();
		String number = Integer.toString(rnd.nextInt(10));
		
		for (int i=1; i<10; i++)
		{
			number = number + Integer.toString(rnd.nextInt(10));
		}
		
		return number;
	}
	
	public static void decryptDatabase()//called once user enters password
	{
		File f = new File ("TransactionDatabase.txt");
		String databaseString = Decryption.decryptFile(f);
		testHash = SellerEncryption.hash(databaseString);
		String[] databaseArray = databaseString.split("\n");
		
		for (int i=0; i<databaseArray.length; i++)
		{
			database.add(databaseArray[i]);
		}
		
		File f2 = new File ("WaitingTransactionsDatabase.txt");
		String waitingDataString = Decryption.decryptFile(f2);
		String[] waitingDataArray = waitingDataString.split("\n");
		
		for (int i=0; i<waitingDataArray.length; i++)
		{
			waitingData.add(waitingDataArray[i]);
		}

		databaseDecrypted=true;
	}
	
	public static void saveDatabaseInformation()//called when program closes
	{
		SellerEncryption.encryptDatabase(database);
		if (!waitingData.isEmpty())
		{
			SellerEncryption.encryptWaitingOrders(waitingData);
		}else{
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter("WaitingTransactionsDatabase.txt"));
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SellerBackEnd.saveConfirmationNumbers();
	}
	
	public static void saveConfirmationNumbers()//called by saveDatabaseInformation
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter ("AllConfirmNumbers.txt"));
			
			for (int i=0; i<allConfirmNumbers.size(); i++)
			{
				bw.write(allConfirmNumbers.get(i));
				bw.newLine();
			}
			bw.write(databaseHash);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void decryptKey()//called once user enters password
	{
		
		PrivateKey serverPrivateKey = Decryption.decryptPrivateKeyFile();
		SellerBackEnd.setPrivateKey(serverPrivateKey);
	}
	
	public static String getDatabaseHash() {
		return databaseHash;
	}

	public static void setDatabaseHash(String databaseHash) {
		SellerBackEnd.databaseHash = databaseHash;
	}

	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static void setPrivateKey(PrivateKey privateKey) {
		SellerBackEnd.privateKey = privateKey;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SellerBackEnd.password = password;
	}
	
	public static String getWaitingData(int i)
	{
		return waitingData.get(i);
	}
	
	public static ArrayList<String> getWaitingData()
	{
		ArrayList<String> waitingDataCopy = new ArrayList<String>();
		
		for (int i=0; i<waitingData.size(); i++)
		{
			waitingDataCopy.add(waitingData.get(i));
		}
		return waitingDataCopy;
	}
	
	public static int getWaitingDataLength()
	{
		return waitingData.size();
	}
	
	public static void deleteWaitingItem(String itemToDelete)//called when user clicks clear item button
	{
		for (int i=0; i<waitingData.size(); i++)
		{
			if (itemToDelete.equals(waitingData.get(i)));
			{
				waitingData.remove(i);
			}
		}
	}

	public static SecretKeySpec getSymmetricKey() {
		return symmetricKey;
	}

	public static void setSymmetricKey(SecretKeySpec symmetricKey) {
		SellerBackEnd.symmetricKey = symmetricKey;
	}
	
	public static void setAllConfirmNumbers (ArrayList<String> confirmNumbers)
	//this method is only used by PrepDriver
	{
		allConfirmNumbers = confirmNumbers;
	}
	
	public static boolean getDatabaseDecrypted()
	{
		return databaseDecrypted;
	}
	
	public static void readOrderFromFile()
	{
		File f = new File("TestOrder.txt");

		if (fileRead==false)
		{
			if (f.exists())
			{
				byte[] testBytes = Decryption.readFromFile(f);
		
				Cipher cipher;
				String decryptedData = null;
				byte[] clearText = null;
				try {
					cipher = Cipher.getInstance("RSA");
					cipher.init(Cipher.DECRYPT_MODE, privateKey);
					clearText = cipher.doFinal(testBytes);
				} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
					e.printStackTrace();
				}
		
				try {
					decryptedData = new String (clearText, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		
				fileRead = true;
				tempConfirmNumbers.add("0238748238");
		
				waitingData.add(tempConfirmNumbers.get(0) + " " + decryptedData);
			}
		}
	}
}
