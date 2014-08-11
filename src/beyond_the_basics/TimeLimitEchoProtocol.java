package beyond_the_basics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeLimitEchoProtocol implements Runnable {

	private static final int BUFSIZE = 32; // size (bytes) of buffer
	private static final String TIMELIMIT = "10000"; // default limit (ms)
	private static final String TIMELIMITPROP = "Timelimit"; // property

	private static int timelimit;

	private Socket clntSocket;
	private Logger logger;

	private TimeLimitEchoProtocol(Socket clntSocket, Logger logger) {
		this.clntSocket = clntSocket;
		this.logger = logger;
		// get the time_limit from system property or take default
		timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP,
				TIMELIMIT));
	}

	@Override
	public void run() {

		try {
			// get the input and output I/O streams from socket
			InputStream in = clntSocket.getInputStream();
			OutputStream out = clntSocket.getOutputStream();

			int revcvMsgSize; // size of received message
			int totalMsgSize = 0; // size of all received message from client
			byte[] buffer = new byte[BUFSIZE]; // buffer for receiving message

			long endTime = System.currentTimeMillis() + timelimit;
			int timeBoundMillis = timelimit;

			clntSocket.setSoTimeout(timeBoundMillis);
			while ((timeBoundMillis > 0)
					&& ((revcvMsgSize = in.read(buffer)) != -1)) {
				out.write(buffer, 0, revcvMsgSize);
				totalMsgSize += revcvMsgSize;
				timeBoundMillis = (int) (endTime - System.currentTimeMillis());
				clntSocket.setSoTimeout(timeBoundMillis);
			}
			logger.info("Client " + clntSocket.getRemoteSocketAddress()
					+ ", echoed " + totalMsgSize + " bytes.");

		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		}
	}
}
