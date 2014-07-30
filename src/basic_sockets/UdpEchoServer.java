package basic_sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpEchoServer {

	// Maximum size of echo datagram
	private static final int ECHOMAX = 255;

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("parameter(s): <Port>");
		}

		int serverPort = Integer.parseInt(args[0]);
		System.out.println("Port is " + serverPort);

		DatagramSocket socket = new DatagramSocket(serverPort);
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

		while (true) { // run forever, receiving and echoing datagram
			socket.receive(packet);
			System.out.println("Handle client at "
					+ packet.getAddress().getHostAddress() + " on port "
					+ packet.getPort());
			socket.send(packet);
			packet.setLength(ECHOMAX);
		}
	}
}
