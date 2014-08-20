
public class SellerSocketProtocol {
	private static final int WAITING_TO_RECEIVE_DATA = 0;
    private static final int WAITING_TO_SEND_CONFIRM_NUM = 1;
    private static final int CONFIRM_NUM_SENT = 2;
    private int state = WAITING_TO_RECEIVE_DATA;

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING_TO_RECEIVE_DATA) {
            theOutput = "Please send purchase data";
            state = WAITING_TO_SEND_CONFIRM_NUM;
        } else if (state == WAITING_TO_SEND_CONFIRM_NUM) {
        	theOutput = SellerBackEnd.generateConfirmNum();
        	SellerBackEnd.storeTempData(theInput, theOutput);
            state = CONFIRM_NUM_SENT;
        } else if (state == CONFIRM_NUM_SENT){
        	theOutput = "Bye.";
        	state = WAITING_TO_RECEIVE_DATA;
        }
        
        return theOutput;
    }
}
