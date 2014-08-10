package beyond_the_basics;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TcpEchoClient {

	public static void main(String[] args) throws Exception {
		if ((args.length < 2) || (args.length > 3)) {
			throw new IllegalArgumentException(
					"Pamater(s): <Server> <Word> [<Port>]");
		}

		String server = args[0];
		System.out.println("Server IP/Name is " + server);

		byte[] data = args[1].getBytes();
		System.out.println("Word is " + new String(data));

		int serverPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;
		System.out.println("Prot is " + serverPort);

		// create socket that is connected to server on specific port
		Socket socket = new Socket(server, serverPort);
		System.out.println("Connected to server...");

		// receive by reading data from InputStream
		InputStream in = socket.getInputStream();
		// send by writing data to OutpustStream
		OutputStream out = socket.getOutputStream();

		out.write(data); // send encoded string to server

		// receive the same string back from server
		int totalBytesRcvd = 0;
		int bytesRcvd = 0;
		while (totalBytesRcvd < data.length) {
			if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length
					- totalBytesRcvd)) == -1) {
				throw new SocketException("Connection closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;
		} // data array is full
		System.out.println("Received: " + new String(data));

		// close the socket and its streams
		socket.close();
	}
}
