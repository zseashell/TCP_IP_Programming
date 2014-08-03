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
		// encoding
		Framer framer = new LengthFramer(socket.getInputStream());
		
		// create inquiry request
		VoteMsg voteMsg = new VoteMsg(false, true, CANDIDATEID, 0);
		byte[] encodeMsg = coder.toWire(voteMsg);
		
		// send inquiry request
		System.out.println("Sending inquiry message " + encodeMsg.length + " bytes");
		System.out.println(voteMsg);
		framer.frameMsg(encodeMsg, out);
		
		// send a vote
		voteMsg.setInquiry(false);
		encodeMsg = coder.toWire(voteMsg);
		System.out.println("Sending a vote " + encodeMsg.length + " bytes");
		System.out.println(voteMsg);
		framer.frameMsg(encodeMsg, out);
		
		// receive inquiry response
		encodeMsg = framer.nextMsg();
		voteMsg = coder.fromWire(encodeMsg);
		System.out.println("Received response message " + encodeMsg.length + " bytes");
		System.out.println(voteMsg);
		
		// receive vote response
		encodeMsg = framer.nextMsg();
		voteMsg = coder.fromWire(encodeMsg);
		System.out.println("Received response message " + encodeMsg.length + " bytes");
		System.out.println(voteMsg);
		
		socket.close();
		
		
		
		
	}

}
