package send_and_receive_data;

import java.io.IOException;

public interface VoteMsgCoder {

	byte[] toWire(VoteMsg msg) throws IOException;

	VoteMsg fromWire(byte[] input) throws IOException;

}
