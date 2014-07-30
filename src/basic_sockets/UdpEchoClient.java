package basic_sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpEchoClient {

	private static final int TIMEOUT = 3000;
	private static final int MAXTRIES = 5;

	public static void main(String[] args) throws Exception {
		// check for correctness of args
		if ((args.length < 2) || (args.length > 3)) {
			throw new IllegalArgumentException(
					"Parameter(s): <Server> <Word> [<Port>]");
		}

		InetAddress serverAddress = InetAddress.getByName(args[0]);
		System.out.println("Server Address is " + serverAddress);

		byte[] bytesToSend = args[1].getBytes();
		System.out.println("Message is " + new String(bytesToSend));

		int serverPort = (args.length == 3) ? Integer.parseInt(args[2]) : 9999;
		System.out.println("Port is " + serverPort);

		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(TIMEOUT);

		// sending packet
		DatagramPacket sendPacket = new DatagramPacket(bytesToSend,
				bytesToSend.length, serverAddress, serverPort);
		// receiving packet
		DatagramPacket receivePacket = new DatagramPacket(
				new byte[bytesToSend.length], bytesToSend.length);

		// packet may be lost
		int tries = 0;
		boolean receivedResonse = false;
		do {
			// send echo string
			socket.send(sendPacket);

			try {
				socket.receive(receivePacket);

				if (!receivePacket.getAddress().equals(serverAddress)) {
					throw new IOException(
							"Received packet from an unkonwn source!");
				}
				receivedResonse = true;
			} catch (Exception e) {
				tries++;
				System.out.println("Error, " + (MAXTRIES - tries)
						+ " more tries...");
			}

		} while ((!receivedResonse) && (tries < MAXTRIES));

		if (receivedResonse) {
			System.out.println("Received: "
					+ new String(receivePacket.getData()));
		} else {
			System.out.println("No response and giving up...");
		}
		socket.close();
	}

}
