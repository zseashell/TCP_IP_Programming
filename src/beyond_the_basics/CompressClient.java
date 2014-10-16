package beyond_the_basics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/*
 * WARNING: this code can dead lock if a large file is sent.
 */
public class CompressClient {
    
    private static final int BUFSIZE = 256;   // size of read buffer
    
    public static void main(String[] args) throws Exception {
        
        if (args.length != 3) {
            throw new IllegalArgumentException("Parameter(s): <Server> <Port> <File>");
        }
        
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String fileName = args[2];
        
        // open input and output file
        FileInputStream fin = new FileInputStream(fileName);
        FileOutputStream fou = new FileOutputStream(fileName + ".gz");
        
        // create socket
        Socket socket = new Socket(ip, port);
        
        // send uncompressed byte stream to server
        OutputStream socketOutputStream = socket.getOutputStream();
        int bytesReadFromFile;
        byte[] bufferForFile = new byte[BUFSIZE];
        while ((bytesReadFromFile = fin.read(bufferForFile)) != -1) {
            socketOutputStream.write(bufferForFile, 0, bytesReadFromFile);
            System.out.println("Writing...");
        }
        socket.shutdownOutput();  // finish sending
        
        // receive compressed byte stream from server
        InputStream socketInputStream = socket.getInputStream();
        int bytesReadFromServer;
        byte[] bufferForServer = new byte[BUFSIZE];
        while ((bytesReadFromServer = socketInputStream.read(bufferForServer)) != -1) {
            fou.write(bufferForServer, 0, bytesReadFromServer);
            System.out.println("Read...");
        }
        
        socket.close();
        fin.close();
        fou.close();
    }

}
