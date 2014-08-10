package beyond_the_basics;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class TcpEchoServerExecutor {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int serverPort = Integer.parseInt(args[0]);
		System.out.println("Server port is " + serverPort);

		// create a server socket to accept client connection requests
		ServerSocket serverSocket = new ServerSocket(serverPort);
		Logger logger = Logger.getLogger("pratical");
		Executor executor = Executors.newCachedThreadPool();
		// run forever and dispatch request
		while (true) {
			Socket clntSocket = serverSocket.accept(); // blocking...
			executor.execute(new EchoProtocol(clntSocket, logger));
		}
	}
}
