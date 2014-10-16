package beyond_the_basics;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiCastSender {
    
    public static void main(String[] args) throws Exception {
        if ((args.length < 2) || (args.length > 3)) {   //Test # of args
            throw new IllegalArgumentException("Parameter(s): <Multicast Address> <Port> [TTL]");
        }
        
        InetAddress destAddress = InetAddress.getByName(args[0]);   //Destination IP
        if (!destAddress.isMulticastAddress()) {
            throw new IllegalArgumentException("Not a multicast address!");
        }
        
        int destPort = Integer.parseInt(args[1]);  // Destination Port
        int TTL = (args.length == 3) ? Integer.parseInt(args[2]) : 1;  // Set TTL
        
        MulticastSocket socket = new MulticastSocket();
        socket.setTimeToLive(TTL);  // Set TTL for all datagrams
        
        // Just send Hello
        String message = "Hello!";
        byte[] msgBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msgBytes, msgBytes.length, destAddress, destPort);
        System.out.println("Sending greeting request (" + msgBytes.length + " bytes): " + message);
        socket.send(packet);
        
        socket.close();
    }

}
