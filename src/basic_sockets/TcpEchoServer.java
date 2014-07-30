package basic_sockets;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpEchoServer {

	private static final int BUFFSIZE = 32;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int serverPort = Integer.parseInt(args[0]);
		System.out.println("Port is " + serverPort);

		// create a server socket to accept client connection request
		ServerSocket serverSocket = new ServerSocket(serverPort);

		int recvMsgSize; // size of received message
		byte[] receiveBuf = new byte[BUFFSIZE]; // receive buffer

		while (true) { // run forever, accepting and servicing connections
			Socket clientSocket = serverSocket.accept();

			// client info
			SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
			System.out.println("Handling client at " + clientAddress);

			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();

			// receive until client close connection, indicated by -1 return
			while ((recvMsgSize = in.read(receiveBuf)) != -1) {
				out.write(receiveBuf, 0, recvMsgSize);
			}

			clientSocket.close();
		}
	}
}
