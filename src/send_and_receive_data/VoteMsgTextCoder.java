package send_and_receive_data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/*
 * Write Format "VOTEPROTO" < "v" | "i" > [<RESPFLAG>] <CANDIDATE>
 * [<VOTECNT>] CharSet is fixed by the wire format
 */

public class VoteMsgTextCoder implements VoteMsgCoder {

	public static final String MAGIC = "Voting";
	public static final String VOTESTR = "v";
	public static final String INQSTR = "i";
	public static final String RESPONSESTR = "R";

	public static final String DELIMSTR = " ";

	public static final String CHARSETNAME = "US-ASCII";
	public static final int MAX_WIRE_LENGTH = 2000;

	@Override
	public byte[] toWire(VoteMsg msg) throws IOException {
		String msgStr = MAGIC + DELIMSTR + (msg.isInquiry() ? INQSTR : VOTESTR)
				+ DELIMSTR + (msg.isResponse() ? RESPONSESTR + DELIMSTR : "")
				+ Integer.toString(msg.getCandidateID()) + DELIMSTR
				+ Long.toString(msg.getVoteCount());
		byte[] data = msgStr.getBytes(CHARSETNAME);
		return data;
	}

	@Override
	public VoteMsg fromWire(byte[] input) throws IOException {
		ByteArrayInputStream msgStream = new ByteArrayInputStream(input);
		Scanner s = new Scanner(new InputStreamReader(msgStream, CHARSETNAME));

		boolean isInquiry;
		boolean isResponse;
		int candidateID;
		long voteCount;

		String token;
		try {
			token = s.next();
			if (!token.equals(MAGIC)) {
				throw new IOException("bad magic string: " + token);
			}

			token = s.next();
			if (token.equals(VOTESTR)) {
				isInquiry = false;
			} else if (token.equals(INQSTR)) {
				isInquiry = true;
			} else {
				throw new IOException("bad vote/inquiry indicator: " + token);
			}

			token = s.next();
			if (token.equals(RESPONSESTR)) {
				isResponse = true;
				token = s.next();
			} else {
				isResponse = false;
			}

			// current token is candidateID
			candidateID = Integer.parseInt(token);
			if (isResponse) {
				token = s.next();
				voteCount = Long.parseLong(token);
			} else {
				voteCount = 0;
			}
		} catch (IOException ioe) {
			throw new IOException("parsing error...");
		}
		return new VoteMsg(isResponse, isInquiry, candidateID, voteCount);
	}

}
