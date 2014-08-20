import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Decryption {

	public static String decryptFile(File f)
	{
		byte[] encryptedData;
		String decryptedData;
		
		encryptedData = Decryption.readFromFile(f);//read in from database file and store in encryptedData.
		String password = SellerBackEnd.getPassword();
		SellerBackEnd.setSymmetricKey(Decryption.toSHA(password));//read in and save symmetric key
		decryptedData = Decryption.decryptByteArray(encryptedData);//decrypt data using symmetric key
		
		return decryptedData;
	}
	
	public static ArrayList<String> decryptNewData(ArrayList<String> newData)
	{
		ArrayList<String> decryptedData = new ArrayList<String>();
		
		PrivateKey key = SellerBackEnd.getPrivateKey();
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			for (int i=0; i<newData.size(); i++)
			{
				byte[] dd = cipher.doFinal(newData.get(i).getBytes());
				decryptedData.add(new String (dd, "UTF-8"));
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return decryptedData;
	}
	
	public static PrivateKey decryptPrivateKeyFile()
	{
		File encryptedFile = new File ("EncPrivKey.txt");
		byte[] eprivateKeyBytes = Decryption.readFromFile(encryptedFile);
		byte[] clearText=null;
		
		//decrypt privateKeyBytes
		Cipher aes;
		try {
			aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, SellerBackEnd.getSymmetricKey());
			clearText = aes.doFinal(eprivateKeyBytes);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("Error in decryptByteArray");
				e.printStackTrace();
			}
		//convert from byte[] to privateKey
		PrivateKey privateKey= null;
		try {
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(clearText));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return privateKey;
	}
	
	public static SecretKeySpec toSHA (String password)
	{
		String passphrase = password + password;
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.update(passphrase.getBytes());
		SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
		
		return key;
	}
	
	public static byte[] readFromFile(File encryptedFile)
	  {
		//Reading from a file into an array of bytes
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) encryptedFile.length()];
		try {
			fileInputStream = new FileInputStream(encryptedFile);
			fileInputStream.read(bFile);
			fileInputStream.close();
		}catch(Exception e){
	        e.printStackTrace();
	    }
		
		return bFile;
	  }
	  
	  public static String decryptByteArray(byte[] bArray)
	  {
		  Cipher aes;
		  String clearText=null;

			try {
				aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
				aes.init(Cipher.DECRYPT_MODE, SellerBackEnd.getSymmetricKey());
				clearText = new String(aes.doFinal(bArray));
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("Error in decryptByteArray");
				e.printStackTrace();
			}
			return clearText;
	  }
}
