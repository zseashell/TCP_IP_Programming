package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class EchoSelectorProtocol implements TCPProtocol {
	
	private int bufSize;
	
	public EchoSelectorProtocol(int bufSize) {
		this.bufSize = bufSize;
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
		socketChannel.configureBlocking(false);
		// register the selector with new channel for read and attach bytebuffer
		socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		// client socket has pending data
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
		long bytesRead = socketChannel.read(byteBuffer);
		if (bytesRead == -1) {
			socketChannel.close();
		} else if (bytesRead > 0) {
			// indicate via key that reading/writing are both of interest now
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		// channel is available for writing and key is valid
		ByteBuffer buf = (ByteBuffer) key.attachment();
		buf.flip();  // prepare for writing
		SocketChannel socketChannel = (SocketChannel) key.channel();
		socketChannel.write(buf);
		if (!buf.hasRemaining()) {
			// nothing left , no longer interest in writing
			key.interestOps(SelectionKey.OP_READ);
		}
		buf.compact();
	}

}
