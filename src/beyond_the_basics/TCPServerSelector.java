package beyond_the_basics;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TCPServerSelector {

	private static final int BUFSIZE = 256;
	private static final int TIMEOUT = 300;

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			throw new IllegalArgumentException("Parameter<s>: <Port> ...");
		}

		// create a selector to multiplex listening socket and channel
		Selector selector = Selector.open();

		// creating listening socket channel for each port and register selector
		for (String arg : args) {
			ServerSocketChannel listenSocketChannel = ServerSocketChannel
					.open();
			listenSocketChannel.socket().bind(
					new InetSocketAddress(Integer.parseInt(arg)));
			listenSocketChannel.configureBlocking(false);
			// register selector with channel
			listenSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}

		// create a handler that implements protocol
		TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);

		while (true) { // run forever, processing available I/O operations
			// wait for some channel to be ready (or timeout)
			if (selector.select(TIMEOUT) == 0) {
				System.out.print(".");
				continue;
			}
			// get iteration of key set with I/O to process
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				// ServerSocket has pending connection request
				if (key.isAcceptable()) {
					protocol.handleAccept(key);
				}
				// client socket channel has pending data
				if (key.isReadable()) {
					protocol.handleRead(key);
				}
				// client socket channel is available for writing and key is
				// valid
				if (key.isValid() && key.isWritable()) {
					protocol.handleWrite(key);
				}
				keyIter.remove();

			}
		}

	}

}
