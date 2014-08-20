import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class BuyerEncryption {
	private static PublicKey serverPublicKey;
	
	public static byte[] EncryptData(String plainTextData)
	{
		BuyerEncryption.getPublicKey();
		byte[] dataToEncrypt = plainTextData.getBytes();
		byte[] encryptedBytes = null;
		
		//encrypt plainTextData using serverPublicKey
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey); //pubKey stored earlier
			encryptedBytes = cipher.doFinal(dataToEncrypt);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		return encryptedBytes;
	}
	
	private static void getPublicKey()
	{
		FileInputStream keyfis = null;
		byte[] encKey;
		try {
			//read in from file
			keyfis = new FileInputStream("publicKey.txt");
			encKey = new byte[keyfis.available()];
			keyfis.read(encKey);

			keyfis.close();
			
			//convert bytes to PublicKey
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			serverPublicKey = keyFactory.generatePublic(pubKeySpec);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

}
