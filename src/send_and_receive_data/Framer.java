package send_and_receive_data;

import java.io.IOException;
import java.io.OutputStream;

public interface Framer {

	void frameMsg(byte[] message, OutputStream out) throws IOException;

	byte[] nextMsg() throws IOException;

}
