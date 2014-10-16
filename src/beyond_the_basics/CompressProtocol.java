package beyond_the_basics;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class CompressProtocol implements Runnable {

    private static final int BUFSIZE = 1024; // size of receive buffer
    private Socket clnSocket;
    private Logger logger;
    

    public CompressProtocol(Socket clnSocket, Logger logger) {
        this.clnSocket = clnSocket;
        this.logger = logger;
    }
    
    @Override
    public void run() {
        try {
            // Get input and output stream from socket
            InputStream in = clnSocket.getInputStream();
            GZIPOutputStream out = new GZIPOutputStream(clnSocket.getOutputStream());
            
            byte[] buffer = new byte[BUFSIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.finish();  // flush bytes from GZIPOutputStream
            logger.info("Client " + clnSocket.getRemoteSocketAddress() + " finished");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception in echo protocol", e);
        }
        
        try {
            clnSocket.close();
        } catch (IOException e) {
            logger.info("Exception: " + e.getMessage());
        }
    }
}
