package beyond_the_basics;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class TCPEchoNonBlockingClient {

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			throw new IllegalArgumentException("Parameter(s): <IP> <Port> <Message>");
		}
		
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		byte[] msgBytes = args[2].getBytes();
		
		// create socket channel and make it non-block
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		
		// initiate connection to server and repeatedly poll until complete
		if (!socketChannel.connect(new InetSocketAddress(ip, port))) {
			while (!socketChannel.finishConnect()) {
				System.out.println("Connecting...");
			}
		}
		System.out.println();
		
		// buffer used to write to server
		ByteBuffer writeBuf = ByteBuffer.wrap(msgBytes);
		
		// buffer used to read from server
		ByteBuffer readBuf = ByteBuffer.allocate(msgBytes.length);
		int bytesRcvd;
		int totalBytesRcvd = 0; 
		while (totalBytesRcvd < msgBytes.length) {
			if (writeBuf.hasRemaining()) {
				socketChannel.write(writeBuf);
			}
			if ((bytesRcvd = socketChannel.read(readBuf)) == -1) {
				throw new SocketException("Socket being closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;
			System.out.print("...");
		}
		
		System.out.println();
		System.out.println("Received: " + new String(readBuf.array(), 0, totalBytesRcvd));
		socketChannel.close();
	}

}
