package send_and_receive_data;

import java.io.OutputStream;
import java.net.Socket;

public class VoteClientTCP {
	
	public static final int CANDIDATEID = 888;
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
		}
		
		String serverAddress = args[0];
		System.out.println("Server Address is " + serverAddress);
		int port = Integer.parseInt(args[1]);
		System.out.println("Server Port is " + port);
		
		Socket socket = new Socket(serverAddress, port);
		OutputStream out = socket.getOutputStream();
		
		// change bin to text for a different framing strategy
		VoteMsgCoder coder = new VoteMsgBinCoder();
	}

}
