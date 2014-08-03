package send_and_receive_data;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LengthFramer implements Framer {

	public static final int MAX_MESSAGE_LENGTH = 65535;
	public static final int BYTE_MASK = 0xff;
	public static final int SHORT_MASK = 0xffff;
	public static final int BYTE_SHIFT = 8;

	private DataInputStream in;

	public LengthFramer(InputStream in) {
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if (message.length > MAX_MESSAGE_LENGTH) {
			throw new IOException("message too long");
		}

		// write length prefix
		out.write((message.length >> BYTE_SHIFT) & BYTE_MASK);
		out.write(message.length & BYTE_MASK);
		// write message
		out.write(message);
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		int length;
		try {
			length = in.readUnsignedShort();
		} catch (EOFException eof) {
			return null;
		}
		// 0 < length < 65535
		byte[] msg = new byte[length];
		in.readFully(msg);
		return msg;
	}

}
