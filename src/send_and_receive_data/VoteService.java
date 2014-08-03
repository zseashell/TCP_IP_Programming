package send_and_receive_data;

import java.util.HashMap;
import java.util.Map;

public class VoteService {

	// map of candidates to number of votes
	private Map<Integer, Long> results = new HashMap<>();

	public VoteMsg handleRequest(VoteMsg msg) {
		if (msg.isResponse()) { // if response, just send it back
			return msg;
		}

		msg.setResponse(true);

		// get candidate Id and vote count
		int candidate = msg.getCandidateID();
		Long count = results.get(candidate);
		if (count == null) {
			count = 0L;
		}
		if (!msg.isInquiry()) {
			results.put(candidate, ++count); // if vote, increment count
		}
		msg.setVoteCount(count);
		return msg;
	}
}
