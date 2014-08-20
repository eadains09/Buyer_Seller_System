
public class BuyerSocketProtocol {
	private static final int WAITING_TO_SEND_DATA = 0;
    private static final int WAITING_FOR_CONFIRM_NUM = 1;
    private byte[] encryptedData;
    private int state;


    
    BuyerSocketProtocol(byte[] encryptedData)
    {
    	this.encryptedData = encryptedData;
        state = WAITING_TO_SEND_DATA;
    }

    public byte[] processInput(String theInput) {
        byte[] theOutput = null;

        if (state == WAITING_TO_SEND_DATA) {
        	theOutput = encryptedData;
            state = WAITING_FOR_CONFIRM_NUM;
        } else if (state == WAITING_FOR_CONFIRM_NUM) {
        	BuyerBackEnd.setConfirmNum(theInput);
        	BuyerBackEnd.setSuccessState(2);
        	theOutput = "Bye.".getBytes();
            state = WAITING_TO_SEND_DATA;
        }
        
        return theOutput;
    }
}
