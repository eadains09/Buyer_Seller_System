import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class PrepDriver {
	public static void main(String[] args)
	{
		SellerBackEnd.setSymmetricKey(Decryption.toSHA("CS480Project5"));
		
		ArrayList<String> prepDatabase = new ArrayList<String>();
		prepDatabase.add("2857298571 Item: necklace Buyer Info: Jane Locke jlocke@gmail.com 383-292-8488 389 Main St SanFrancisco CA 94117 Credit Card #: 4527 2749 2983 2834 2938 Exp. Date: 04/16 Security Code: 489");
		prepDatabase.add("2859285973 Item: TV Buyer Info: Tommy Nighton tnighton@gmail.com 792-384-2934 2938 1st St Oakland CA 92384 Credit Card #: 4509 2903 1099 2943 2898 Exp.Date: 12/18 Security Code: 392");
		SellerEncryption.encryptDatabase(prepDatabase);
		
		ArrayList<String> prepWaiting = new ArrayList<String>();
		prepWaiting.add("3745178374 Item: Veronica Mars Season 1 Buyer Info: Omar Nomael oNomael@gmail.com 782-829-2288 2898 Holloway Ave SanFrancisco CA 94115 Credit Card #: 2734 2374 2837 1498 4893 Exp. Date: 08/17 Security Code: 843");
		prepWaiting.add("3847573984 Item: Chalkboard Buyer Info: Stiev Tchon stchon@gmail.com 309-293-1848 3498 Rennelwood Pleasanton CA 94881 Credit Card #: 8928 2848 8349 2939 9183 Exp.Date: 07/20 Security Code: 382");
		SellerEncryption.encryptWaitingOrders(prepWaiting);
		
		ArrayList<String> prepConfirm = new ArrayList<String>();
		prepConfirm.add("2857298571");
		prepConfirm.add("2859285973");
		prepConfirm.add("3745178374");
		prepConfirm.add("3847573984");
		SellerBackEnd.setAllConfirmNumbers(prepConfirm);
		SellerBackEnd.saveConfirmationNumbers();
		
		System.out.println("done");
	}
	
	public static byte[] encryptString(PrivateKey clearText)
	{
		Cipher aes;
		byte[] cipherText = null;
			try {
				aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
				aes.init(Cipher.ENCRYPT_MODE, SellerBackEnd.getSymmetricKey());
				cipherText = aes.doFinal(clearText.getEncoded());
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("error in encryptString");
				e.printStackTrace();
			}
		return cipherText;
	}
	
	@SuppressWarnings("unused")
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
