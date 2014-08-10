package beyond_the_basics;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TcpEchoServerThread {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s)");
		}

		int serverPort = Integer.parseInt(args[0]);
		System.out.println("Server port is " + serverPort);

		// create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);

		Logger logger = Logger.getLogger("pratical");
		while (true) {
			Socket clntSocket = serverSocket.accept(); // block...
			// spawn thread to handle new connection
			Thread thread = new Thread(new EchoProtocol(clntSocket, logger));
			thread.start();
			logger.info("Created and Started Thread " + thread.getName());
		}
	}
}
