package beyond_the_basics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoProtocol implements Runnable {

	private final int BUFSIZE = 32;

	private Socket clntSocket;

	private Logger logger;

	public EchoProtocol(Socket clntSocket, Logger logger) {
		this.clntSocket = clntSocket;
		this.logger = logger;
	}

	@Override
	public void run() {
		try {
			// Get the input and output I/O streams from socket
			InputStream in = clntSocket.getInputStream();
			OutputStream out = clntSocket.getOutputStream();

			int recvMsgSize; // size of received message
			int totalMsgSize = 0; // size of all bytes received from client
			byte[] echoBuffer = new byte[BUFSIZE]; // receive buffer
			while ((recvMsgSize = in.read(echoBuffer)) != -1) {
				out.write(echoBuffer, 0, recvMsgSize);
				totalMsgSize += recvMsgSize;
			}
			logger.info("Client " + clntSocket.getRemoteSocketAddress()
					+ " echoed " + totalMsgSize + " bytes.");
		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception in echo protocol", e);
		} finally {
			try {
				clntSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
