package beyond_the_basics;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class UDPEchoServerSelector {

	private static final int TIMEOUT = 3000; // wait timeout

	private static final int ECHOMAX = 255; // maximum size of echo datagram

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s): <Port>");
		}

		int serverPort = Integer.parseInt(args[0]);

		// create a selector to multiplex client connections
		Selector selector = Selector.open();

		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(new InetSocketAddress(serverPort));
		channel.register(selector, SelectionKey.OP_READ, new ClientRecord());

		while (true) { // run forever, receiving and echoing datagrams
			if (selector.select(TIMEOUT) == 0) {
				System.out.println(".");
				continue;
			}

			// get iterator of key set with I/O to process
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();

				// client socket has pending data
				if (key.isReadable()) {
					handleRead(key);
				}

				// client socket is available for writing
				if (key.isValid() && key.isWritable()) {
					handleWrite(key);
				}
			}
		}
	}

	public static void handleRead(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		ClientRecord clientRecord = (ClientRecord) key.attachment();
		clientRecord.buffer.clear();
		clientRecord.clientAddress = channel.receive(clientRecord.buffer);
		if (clientRecord.clientAddress != null) {
			key.interestOps(SelectionKey.OP_WRITE);
		}
	}

	public static void handleWrite(SelectionKey key) throws IOException {
		DatagramChannel channel = (DatagramChannel) key.channel();
		ClientRecord clientRecord = (ClientRecord) key.attachment();
		clientRecord.buffer.flip();
		int byteSent = channel.send(clientRecord.buffer,
				clientRecord.clientAddress);
		if (byteSent != 0) {
			key.interestOps(SelectionKey.OP_READ);
		}
	}

	static class ClientRecord {
		public SocketAddress clientAddress;
		public ByteBuffer buffer = ByteBuffer.allocate(ECHOMAX);
	}

}
